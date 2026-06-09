package br.com.fiap.orbitalguard.repository;

import br.com.fiap.orbitalguard.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    List<Sensor> findByRegiaoId(Long regiaoId);
}
