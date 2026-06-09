package br.com.fiap.orbitalguard.controller;

import br.com.fiap.orbitalguard.dto.ocorrencia.OcorrenciaRequestDTO;
import br.com.fiap.orbitalguard.dto.ocorrencia.OcorrenciaResponseDTO;
import br.com.fiap.orbitalguard.service.OcorrenciaService;
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
@RequestMapping("/api/v1/ocorrencias")
@RequiredArgsConstructor
@Tag(name = "Ocorrencias", description = "Gerenciamento de ocorrencias registradas")
@SecurityRequirement(name = "bearerAuth")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;
    private final HateoasAssembler hateoasAssembler;

    @GetMapping
    @Operation(summary = "Listar todas as ocorrencias")
    public ResponseEntity<CollectionModel<EntityModel<OcorrenciaResponseDTO>>> listar() {
        return ResponseEntity.ok(hateoasAssembler.toOcorrenciaCollection(ocorrenciaService.listar()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ocorrencia por ID")
    public ResponseEntity<EntityModel<OcorrenciaResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hateoasAssembler.toModel(ocorrenciaService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Cadastrar nova ocorrencia")
    public ResponseEntity<EntityModel<OcorrenciaResponseDTO>> criar(@Valid @RequestBody OcorrenciaRequestDTO dto) {
        OcorrenciaResponseDTO created = ocorrenciaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(hateoasAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar ocorrencia existente")
    public ResponseEntity<EntityModel<OcorrenciaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OcorrenciaRequestDTO dto
    ) {
        return ResponseEntity.ok(hateoasAssembler.toModel(ocorrenciaService.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover ocorrencia")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        ocorrenciaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
