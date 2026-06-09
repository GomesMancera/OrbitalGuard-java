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
@Table(name = "sensores_agua")
@DiscriminatorValue("AGUA")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class SensorAgua extends Sensor {

    @Column(name = "ph_minimo")
    private Double phMinimo;

    @Column(name = "ph_maximo")
    private Double phMaximo;

    @Column(name = "turbidez_maxima")
    private Double turbidezMaxima;
}
