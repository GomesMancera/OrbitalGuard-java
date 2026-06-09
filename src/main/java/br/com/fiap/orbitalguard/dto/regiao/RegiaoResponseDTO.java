package br.com.fiap.orbitalguard.dto.regiao;

public record RegiaoResponseDTO(
        Long id,
        String nome,
        String descricao,
        Double latitude,
        Double longitude
) {
}
