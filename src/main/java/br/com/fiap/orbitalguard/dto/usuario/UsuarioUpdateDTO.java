package br.com.fiap.orbitalguard.dto.usuario;

import br.com.fiap.orbitalguard.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @Size(max = 100) String nome,
        @Email String email,
        @Size(min = 6, max = 100) String senha,
        Role role
) {
}
