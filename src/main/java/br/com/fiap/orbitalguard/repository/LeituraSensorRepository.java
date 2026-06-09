package br.com.fiap.orbitalguard.repository;

import br.com.fiap.orbitalguard.model.LeituraSensor;
import br.com.fiap.orbitalguard.model.LeituraSensorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeituraSensorRepository extends JpaRepository<LeituraSensor, LeituraSensorId> {

    List<LeituraSensor> findBySensorId(Long sensorId);
}
