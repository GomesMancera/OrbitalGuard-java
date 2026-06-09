package br.com.fiap.orbitalguard.dto.sensor;

import br.com.fiap.orbitalguard.model.enums.TipoSensor;

public record SensorResponseDTO(
        Long id,
        String nome,
        String localizacao,
        TipoSensor tipo,
        Boolean ativo,
        Long regiaoId,
        Double latitude,
        Double longitude,
        Double phMinimo,
        Double phMaximo,
        Double turbidezMaxima,
        Double pm25Maximo,
        Double co2Maximo,
        Double umidadeRelativa
) {
}
