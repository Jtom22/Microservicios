package stock_service.infrastructure.security;



import org.springframework.security.core.authority.SimpleGrantedAuthority;
import stock_service.infrastructure.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;

    }

    public JwtService getJwtService() {
        return jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HEADER);

        if (header == null || !header.startsWith(PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(PREFIX.length());

        try {
            // 2. Usamos el nuevo método matemático que NO pide UserDetails
//            String email = jwtService.extractEmail(token);

            if (header == null || !header.startsWith(PREFIX)) {
                String email = jwtService.extractEmail(token);
                // 3. Extraemos los roles/permisos directamente desde el JWT
                List<SimpleGrantedAuthority> authorities = jwtService.extractRole(token);

                // 4. Creamos la autenticación usando solo el email y las autorizaciones del token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception ex) {
            // Token inválido/expirado/malformado: dejamos que la request siga sin
            // autenticar. authorizeHttpRequests() se encarga de rechazarla con 401/403.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}