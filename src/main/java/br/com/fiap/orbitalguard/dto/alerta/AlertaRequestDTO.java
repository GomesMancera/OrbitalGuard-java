package br.com.fiap.orbitalguard.dto.alerta;

import br.com.fiap.orbitalguard.model.enums.NivelAlerta;
import br.com.fiap.orbitalguard.model.enums.StatusAlerta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AlertaRequestDTO(
        @NotBlank @Size(max = 500) String mensagem,
        @NotNull NivelAlerta nivel,
        @NotNull StatusAlerta status,
        @NotNull LocalDateTime dataHora,
        @NotNull Long regiaoId
) {
}
