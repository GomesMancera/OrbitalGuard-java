package br.com.fiap.orbitalguard.service;

import br.com.fiap.orbitalguard.dto.alerta.AlertaRequestDTO;
import br.com.fiap.orbitalguard.dto.alerta.AlertaResponseDTO;
import br.com.fiap.orbitalguard.exception.ResourceNotFoundException;
import br.com.fiap.orbitalguard.mapper.EntityMapper;
import br.com.fiap.orbitalguard.model.Alerta;
import br.com.fiap.orbitalguard.model.Regiao;
import br.com.fiap.orbitalguard.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final RegiaoService regiaoService;

    @Transactional(readOnly = true)
    public List<AlertaResponseDTO> listar() {
        return alertaRepository.findAll().stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlertaResponseDTO buscarPorId(Long id) {
        return EntityMapper.toDto(findEntity(id));
    }

    @Transactional
    public AlertaResponseDTO criar(AlertaRequestDTO dto) {
        Regiao regiao = regiaoService.findEntity(dto.regiaoId());
        Alerta alerta = EntityMapper.toEntity(dto, regiao);
        return EntityMapper.toDto(alertaRepository.save(alerta));
    }

    @Transactional
    public AlertaResponseDTO atualizar(Long id, AlertaRequestDTO dto) {
        Alerta alerta = findEntity(id);
        Regiao regiao = regiaoService.findEntity(dto.regiaoId());
        EntityMapper.updateAlerta(alerta, dto, regiao);
        return EntityMapper.toDto(alertaRepository.save(alerta));
    }

    @Transactional
    public void deletar(Long id) {
        Alerta alerta = findEntity(id);
        alertaRepository.delete(alerta);
    }

    public Alerta findEntity(Long id) {
        return alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta nao encontrado com id: " + id));
    }
}
