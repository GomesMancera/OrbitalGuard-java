package br.com.fiap.orbitalguard.dto.ocorrencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record OcorrenciaRequestDTO(
        @NotBlank @Size(max = 1000) String descricao,
        @NotNull LocalDateTime dataHora,
        @NotBlank @Size(max = 50) String status,
        @NotNull Long alertaId,
        Long usuarioResponsavelId
) {
}
