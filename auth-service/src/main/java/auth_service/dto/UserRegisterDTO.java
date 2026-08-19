package auth_service.dto;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


public record UserRegisterDTO(
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(message = "El formato del correo electrónico es inválido")
    String email,

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    String password,

    @NotBlank(message = "El nombre es obligatorio")
    String firstName,

    String lastName,

    Set<String> roles
) {}

