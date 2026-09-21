package stock_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import stock_service.infrastructure.security.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Inyectamos SOLAMENTE el filtro que valida el JWT.
    // NO inyectamos ni CustomUserDetailsService, ni AuthenticationManager, ni PasswordEncoder.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Definimos las reglas específicas para el stock
                        .requestMatchers(HttpMethod.GET, "/api/stock/**").permitAll() // Público para ver stock
//                        .requestMatchers(HttpMethod.GET, "/api/stock/**").authenticated() // Público para ver stock
                        .requestMatchers(HttpMethod.PUT,"/api/stock/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/stock/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/stock/**").hasRole("ADMIN") // Solo admin para crear/modificar
                        .anyRequest().authenticated()
                )
                // 2. Eliminamos el .authenticationProvider(...) ya que el stock no autentica con usuario/contraseña
                // 3. Dejamos el filtro que intercepta y valida la firma del token
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
