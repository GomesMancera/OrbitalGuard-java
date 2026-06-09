package br.com.fiap.orbitalguard.repository;

import br.com.fiap.orbitalguard.model.Ocorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    List<Ocorrencia> findByAlertaId(Long alertaId);
}
