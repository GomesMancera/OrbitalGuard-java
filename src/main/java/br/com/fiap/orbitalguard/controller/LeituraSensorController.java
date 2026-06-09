package br.com.fiap.orbitalguard.controller;

import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorRequestDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorResponseDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorUpdateDTO;
import br.com.fiap.orbitalguard.service.LeituraSensorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/leituras")
@RequiredArgsConstructor
@Tag(name = "Leituras", description = "Gerenciamento de leituras dos sensores")
@SecurityRequirement(name = "bearerAuth")
public class LeituraSensorController {

    private final LeituraSensorService leituraSensorService;
    private final HateoasAssembler hateoasAssembler;

    @GetMapping
    @Operation(summary = "Listar todas as leituras")
    public ResponseEntity<CollectionModel<EntityModel<LeituraSensorResponseDTO>>> listar() {
        return ResponseEntity.ok(hateoasAssembler.toLeituraCollection(leituraSensorService.listar()));
    }

    @GetMapping("/sensor/{sensorId}")
    @Operation(summary = "Listar leituras por sensor")
    public ResponseEntity<CollectionModel<EntityModel<LeituraSensorResponseDTO>>> listarPorSensor(
            @PathVariable Long sensorId
    ) {
        return ResponseEntity.ok(hateoasAssembler.toLeituraCollection(leituraSensorService.listarPorSensor(sensorId)));
    }

    @GetMapping("/{sensorId}/{dataHora}")
    @Operation(summary = "Buscar leitura por chave composta")
    public ResponseEntity<EntityModel<LeituraSensorResponseDTO>> buscarPorId(
            @PathVariable Long sensorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora
    ) {
        return ResponseEntity.ok(hateoasAssembler.toModel(leituraSensorService.buscarPorId(sensorId, dataHora)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Registrar nova leitura")
    public ResponseEntity<EntityModel<LeituraSensorResponseDTO>> criar(@Valid @RequestBody LeituraSensorRequestDTO dto) {
        LeituraSensorResponseDTO created = leituraSensorService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(hateoasAssembler.toModel(created));
    }

    @PutMapping("/{sensorId}/{dataHora}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    @Operation(summary = "Atualizar leitura existente")
    public ResponseEntity<EntityModel<LeituraSensorResponseDTO>> atualizar(
            @PathVariable Long sensorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora,
            @Valid @RequestBody LeituraSensorUpdateDTO dto
    ) {
        return ResponseEntity.ok(hateoasAssembler.toModel(
                leituraSensorService.atualizar(sensorId, dataHora, dto)));
    }

    @DeleteMapping("/{sensorId}/{dataHora}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remover leitura")
    public ResponseEntity<Void> deletar(
            @PathVariable Long sensorId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataHora
    ) {
        leituraSensorService.deletar(sensorId, dataHora);
        return ResponseEntity.noContent().build();
    }
}
