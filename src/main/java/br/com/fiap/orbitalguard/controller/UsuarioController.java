package br.com.fiap.orbitalguard.controller;

import br.com.fiap.orbitalguard.dto.usuario.UsuarioResponseDTO;
import br.com.fiap.orbitalguard.dto.usuario.UsuarioUpdateDTO;
import br.com.fiap.orbitalguard.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gerenciamento de usuarios")
@SecurityRequirement(name = "bearerAuth")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final HateoasAssembler hateoasAssembler;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar todos os usuarios")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponseDTO>>> listar() {
        return ResponseEntity.ok(hateoasAssembler.toUsuarioCollection(usuarioService.listar()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Buscar usuario por ID")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hateoasAssembler.toModel(usuarioService.buscarPorId(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Atualizar usuario")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto
    ) {
        return ResponseEntity.ok(hateoasAssembler.toModel(usuarioService.atualizar(id, dto)));
    }
}
