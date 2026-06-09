package br.com.fiap.orbitalguard.service;

import br.com.fiap.orbitalguard.dto.usuario.UsuarioResponseDTO;
import br.com.fiap.orbitalguard.dto.usuario.UsuarioUpdateDTO;
import br.com.fiap.orbitalguard.exception.ResourceNotFoundException;
import br.com.fiap.orbitalguard.mapper.EntityMapper;
import br.com.fiap.orbitalguard.model.Usuario;
import br.com.fiap.orbitalguard.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return usuarioRepository.findAll().stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorId(Long id) {
        return EntityMapper.toDto(findEntity(id));
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = findEntity(id);
        if (dto.nome() != null) {
            usuario.setNome(dto.nome());
        }
        if (dto.email() != null) {
            usuario.setEmail(dto.email());
        }
        if (dto.senha() != null) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }
        if (dto.role() != null) {
            usuario.setRole(dto.role());
        }
        return EntityMapper.toDto(usuarioRepository.save(usuario));
    }

    public Usuario findEntity(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com id: " + id));
    }
}
