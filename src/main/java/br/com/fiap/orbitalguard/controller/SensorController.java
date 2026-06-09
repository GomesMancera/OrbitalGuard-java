package br.com.fiap.orbitalguard.controller;

import br.com.fiap.orbitalguard.dto.sensor.SensorRequestDTO;
import br.com.fiap.orbitalguard.dto.sensor.SensorResponseDTO;
import br.com.fiap.orbitalguard.service.SensorService;
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
@RequestMapping("/api/v1/sensores")
@RequiredArgsConstructor
@Tag(name = "Sensores", description = "Gerenciamento de sensores ambientais")
@SecurityRequirement(name = "bearerAuth")
public class SensorController {

    private final SensorService sensorService;
    private final HateoasAssembler hateoasAssembler;

    @GetMapping
    @Operation(summary = "Listar todos os sensores")
    public ResponseEntity<CollectionModel<EntityModel<SensorResponseDTO>>> listar() {
        return ResponseEntity.ok(hateoasAssembler.toSensorCollection(sensorService.listar()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar sensor por ID")
    public ResponseEntity<EntityModel<SensorResponseDTO>> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(hateoasAssembler.toModel(sensorService.buscarPorId(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Cadastrar novo sensor")
    public ResponseEntity<EntityModel<SensorResponseDTO>> criar(@Valid @RequestBody SensorRequestDTO dto) {
        SensorResponseDTO created = sensorService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(hateoasAssembler.toModel(created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar sensor existente")
    public ResponseEntity<EntityModel<SensorResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody SensorRequestDTO dto
    ) {
        return ResponseEntity.ok(hateoasAssembler.toModel(sensorService.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover sensor")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        sensorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
