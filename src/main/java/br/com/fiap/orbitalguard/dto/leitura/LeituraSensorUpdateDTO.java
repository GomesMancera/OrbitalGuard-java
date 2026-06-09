package br.com.fiap.orbitalguard.dto.leitura;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LeituraSensorUpdateDTO(
        @NotNull @Min(0) Double valor,
        @NotBlank @Size(max = 20) String unidade,
        @Size(max = 500) String observacao
) {
}
