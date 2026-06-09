package br.com.fiap.orbitalguard.dto.leitura;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record LeituraSensorRequestDTO(
        @NotNull Long sensorId,
        @NotNull LocalDateTime dataHoraLeitura,
        @NotNull @Min(0) Double valor,
        @NotBlank @Size(max = 20) String unidade,
        @Size(max = 500) String observacao
) {
}
