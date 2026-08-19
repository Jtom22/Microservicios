package auth_service.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import auth_service.dto.LoginRequestDTO;
import auth_service.dto.LoginResponseDTO;
import auth_service.dto.UserRegisterDTO;
import auth_service.dto.UserResponseDTO;
import auth_service.entity.User;
import auth_service.services.JwtService;
import auth_service.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegisterDTO dto) {
        UserResponseDTO created = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/users/{email}")
    public ResponseEntity<UserResponseDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

        @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        // Si el email/password no coinciden, esto lanza BadCredentialsException.
        // La capturamos en el GlobalExceptionHandler para devolver 401 en vez de 500.
        var authToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var authentication = authenticationManager.authenticate(authToken);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String token = jwtService.generateToken(userDetails);

        //recuerda que el jwrExpirations tiene valor en el application.yml
        return ResponseEntity.ok(LoginResponseDTO.of(token, jwtExpirationMs));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@RequestParam String param) {

        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }
    
    
}