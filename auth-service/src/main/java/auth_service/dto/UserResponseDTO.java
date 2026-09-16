package auth_service.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponseDTO(
    String id,
    String email,
    String firstName,
    String lastName,
    Set<String> roles,
    LocalDateTime createdAt
) {}