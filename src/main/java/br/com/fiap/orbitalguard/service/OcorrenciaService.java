package br.com.fiap.orbitalguard.service;

import br.com.fiap.orbitalguard.dto.ocorrencia.OcorrenciaRequestDTO;
import br.com.fiap.orbitalguard.dto.ocorrencia.OcorrenciaResponseDTO;
import br.com.fiap.orbitalguard.exception.ResourceNotFoundException;
import br.com.fiap.orbitalguard.mapper.EntityMapper;
import br.com.fiap.orbitalguard.model.Alerta;
import br.com.fiap.orbitalguard.model.Ocorrencia;
import br.com.fiap.orbitalguard.model.Usuario;
import br.com.fiap.orbitalguard.repository.OcorrenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OcorrenciaService {

    private final OcorrenciaRepository ocorrenciaRepository;
    private final AlertaService alertaService;
    private final UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<OcorrenciaResponseDTO> listar() {
        return ocorrenciaRepository.findAll().stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OcorrenciaResponseDTO buscarPorId(Long id) {
        return EntityMapper.toDto(findEntity(id));
    }

    @Transactional
    public OcorrenciaResponseDTO criar(OcorrenciaRequestDTO dto) {
        Alerta alerta = alertaService.findEntity(dto.alertaId());
        Usuario usuario = resolveUsuario(dto.usuarioResponsavelId());
        Ocorrencia ocorrencia = EntityMapper.toEntity(dto, alerta, usuario);
        return EntityMapper.toDto(ocorrenciaRepository.save(ocorrencia));
    }

    @Transactional
    public OcorrenciaResponseDTO atualizar(Long id, OcorrenciaRequestDTO dto) {
        Ocorrencia ocorrencia = findEntity(id);
        Alerta alerta = alertaService.findEntity(dto.alertaId());
        Usuario usuario = resolveUsuario(dto.usuarioResponsavelId());
        EntityMapper.updateOcorrencia(ocorrencia, dto, alerta, usuario);
        return EntityMapper.toDto(ocorrenciaRepository.save(ocorrencia));
    }

    @Transactional
    public void deletar(Long id) {
        Ocorrencia ocorrencia = findEntity(id);
        ocorrenciaRepository.delete(ocorrencia);
    }

    public Ocorrencia findEntity(Long id) {
        return ocorrenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ocorrencia nao encontrada com id: " + id));
    }

    private Usuario resolveUsuario(Long usuarioId) {
        if (usuarioId == null) {
            return null;
        }
        return usuarioService.findEntity(usuarioId);
    }
}
