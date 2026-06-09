package br.com.fiap.orbitalguard.repository;

import br.com.fiap.orbitalguard.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByRegiaoId(Long regiaoId);
}
