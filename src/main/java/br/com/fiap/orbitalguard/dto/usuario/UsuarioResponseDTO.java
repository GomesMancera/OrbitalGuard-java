package br.com.fiap.orbitalguard.dto.usuario;

import br.com.fiap.orbitalguard.model.enums.Role;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        Role role
) {
}
