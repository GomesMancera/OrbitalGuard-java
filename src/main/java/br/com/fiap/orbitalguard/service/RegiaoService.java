package br.com.fiap.orbitalguard.service;

import br.com.fiap.orbitalguard.dto.regiao.RegiaoRequestDTO;
import br.com.fiap.orbitalguard.dto.regiao.RegiaoResponseDTO;
import br.com.fiap.orbitalguard.exception.ResourceNotFoundException;
import br.com.fiap.orbitalguard.mapper.EntityMapper;
import br.com.fiap.orbitalguard.model.Regiao;
import br.com.fiap.orbitalguard.repository.RegiaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegiaoService {

    private final RegiaoRepository regiaoRepository;

    @Transactional(readOnly = true)
    public List<RegiaoResponseDTO> listar() {
        return regiaoRepository.findAll().stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegiaoResponseDTO buscarPorId(Long id) {
        return EntityMapper.toDto(findEntity(id));
    }

    @Transactional
    public RegiaoResponseDTO criar(RegiaoRequestDTO dto) {
        Regiao regiao = EntityMapper.toEntity(dto);
        return EntityMapper.toDto(regiaoRepository.save(regiao));
    }

    @Transactional
    public RegiaoResponseDTO atualizar(Long id, RegiaoRequestDTO dto) {
        Regiao regiao = findEntity(id);
        EntityMapper.updateRegiao(regiao, dto);
        return EntityMapper.toDto(regiaoRepository.save(regiao));
    }

    @Transactional
    public void deletar(Long id) {
        Regiao regiao = findEntity(id);
        regiaoRepository.delete(regiao);
    }

    public Regiao findEntity(Long id) {
        return regiaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regiao nao encontrada com id: " + id));
    }
}
