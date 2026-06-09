package br.com.fiap.orbitalguard.dto.ocorrencia;

import java.time.LocalDateTime;

public record OcorrenciaResponseDTO(
        Long id,
        String descricao,
        LocalDateTime dataHora,
        String status,
        Long alertaId,
        Long usuarioResponsavelId
) {
}
