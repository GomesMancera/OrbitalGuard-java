package br.com.fiap.orbitalguard.dto.auth;

public record AuthResponseDTO(
        String token,
        String type
) {
    public static AuthResponseDTO of(String token) {
        return new AuthResponseDTO(token, "Bearer");
    }
}
