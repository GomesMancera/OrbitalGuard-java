package br.com.fiap.orbitalguard.dto.sensor;

import br.com.fiap.orbitalguard.model.enums.TipoSensor;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SensorRequestDTO(
        @NotBlank @Size(max = 100) String nome,
        @NotBlank @Size(max = 200) String localizacao,
        @NotNull TipoSensor tipo,
        @NotNull Long regiaoId,
        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") Double latitude,
        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") Double longitude,
        Double phMinimo,
        Double phMaximo,
        Double turbidezMaxima,
        Double pm25Maximo,
        Double co2Maximo,
        Double umidadeRelativa
) {
}
