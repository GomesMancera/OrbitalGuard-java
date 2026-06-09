package br.com.fiap.orbitalguard.controller;

import br.com.fiap.orbitalguard.dto.regiao.RegiaoRequestDTO;
import br.com.fiap.orbitalguard.dto.regiao.RegiaoResponseDTO;
import br.com.fiap.orbitalguard.service.RegiaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/regioes")
@RequiredArgsConstructor
@Tag(name = "Regioes", description = "Gerenciamento de regioes monitoradas")
public class RegiaoController {

    private final RegiaoService regiaoService;
    private final HateoasAssembler hateoasAssembler;

    @GetMapping
    @Operation(summary = "Listar todas as regioes")
    public ResponseEntity<CollectionModel<EntityModel<RegiaoResponseDTO>>> listar() {
        return ResponseEntity.ok(hateoasAssembler.toCollection(regiaoService.listar()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar regiao por ID")
    public ResponseEntity<EntityModel<RegiaoResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hateoasAssembler.toModel(regiaoService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Cadastrar nova regiao")
    public ResponseEntity<EntityModel<RegiaoResponseDTO>> criar(@Valid @RequestBody RegiaoRequestDTO dto) {
        RegiaoResponseDTO created = regiaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(hateoasAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar regiao existente")
    public ResponseEntity<EntityModel<RegiaoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody RegiaoRequestDTO dto
    ) {
        return ResponseEntity.ok(hateoasAssembler.toModel(regiaoService.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remover regiao")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        regiaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
