package br.com.fiap.orbitalguard.controller;

import br.com.fiap.orbitalguard.dto.alerta.AlertaRequestDTO;
import br.com.fiap.orbitalguard.dto.alerta.AlertaResponseDTO;
import br.com.fiap.orbitalguard.service.AlertaService;
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
@RequestMapping("/api/v1/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Gerenciamento de alertas ambientais")
@SecurityRequirement(name = "bearerAuth")
public class AlertaController {

    private final AlertaService alertaService;
    private final HateoasAssembler hateoasAssembler;

    @GetMapping
    @Operation(summary = "Listar todos os alertas")
    public ResponseEntity<CollectionModel<EntityModel<AlertaResponseDTO>>> listar() {
        return ResponseEntity.ok(hateoasAssembler.toAlertaCollection(alertaService.listar()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar alerta por ID")
    public ResponseEntity<EntityModel<AlertaResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hateoasAssembler.toModel(alertaService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Cadastrar novo alerta")
    public ResponseEntity<EntityModel<AlertaResponseDTO>> criar(@Valid @RequestBody AlertaRequestDTO dto) {
        AlertaResponseDTO created = alertaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(hateoasAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar alerta existente")
    public ResponseEntity<EntityModel<AlertaResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlertaRequestDTO dto
    ) {
        return ResponseEntity.ok(hateoasAssembler.toModel(alertaService.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover alerta")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alertaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
