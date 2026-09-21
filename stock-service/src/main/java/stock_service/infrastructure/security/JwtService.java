package stock_service.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtService {

    private final Key signingKey;
    private final long expirationMs;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        // El secret en application.yml es texto plano; lo decodificamos como HMAC key.
        this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(toBase64(secret)));
        this.expirationMs = expirationMs;
    }

    // Permite usar un secret legible (no base64) en application.yml sin romper jjwt,
    // que espera bytes de al menos 256 bits para HS256.
    private String toBase64(String rawSecret) {
        return java.util.Base64.getEncoder().encodeToString(rawSecret.getBytes());
    }


    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token) {
        String email = extractEmail(token);
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return resolver.apply(claims);
    }

    @SuppressWarnings("unchecked")
    public List<SimpleGrantedAuthority> extractRole(String token) {
        // Usamos extractClaim pasándole una función lambda (claims -> ...)
        //le pasamos un token y hacemos la funcion sin parametros para tener la lista de roles
        return extractClaim(token, claims -> {
            // 1. Extraemos la lista de Strings bajo la clave "roles"
            List<String> roles = claims.get("roles", List.class);
            if (roles == null) return List.of();

            // 2. Los transformamos al formato que exige Spring Security
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
        });
    }
}
