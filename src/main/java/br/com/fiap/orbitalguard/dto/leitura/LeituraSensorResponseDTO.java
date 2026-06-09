package br.com.fiap.orbitalguard.dto.leitura;

import java.time.LocalDateTime;

public record LeituraSensorResponseDTO(
        Long sensorId,
        LocalDateTime dataHoraLeitura,
        Double valor,
        String unidade,
        String observacao
) {
}
