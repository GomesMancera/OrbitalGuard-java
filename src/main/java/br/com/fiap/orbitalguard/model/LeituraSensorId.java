package br.com.fiap.orbitalguard.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class LeituraSensorId implements Serializable {

    @Column(name = "sensor_id")
    private Long sensorId;

    @Column(name = "data_hora_leitura")
    private LocalDateTime dataHoraLeitura;
}
