package auth_service.dto;

public record LoginResponseDTO(
    String token,
    String tokenType,
    long expiresInMs
) {
    public static LoginResponseDTO of(String token, long expiresInMs) {
        return new LoginResponseDTO(token, "Bearer", expiresInMs);
    }
}