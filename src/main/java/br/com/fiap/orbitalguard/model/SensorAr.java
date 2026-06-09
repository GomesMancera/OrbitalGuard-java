package br.com.fiap.orbitalguard.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "sensores_ar")
@DiscriminatorValue("AR")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SensorAr extends Sensor {

    @Column(name = "pm25_maximo")
    private Double pm25Maximo;

    @Column(name = "co2_maximo")
    private Double co2Maximo;

    @Column(name = "umidade_relativa")
    private Double umidadeRelativa;
}
