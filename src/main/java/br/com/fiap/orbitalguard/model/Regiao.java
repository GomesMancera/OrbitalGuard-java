package br.com.fiap.orbitalguard.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "regioes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Regiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Embedded
    private Coordenadas coordenadas;

    @OneToMany(mappedBy = "regiao")
    @Builder.Default
    private List<Sensor> sensores = new ArrayList<>();

    @OneToMany(mappedBy = "regiao")
    @Builder.Default
    private List<Alerta> alertas = new ArrayList<>();
}
