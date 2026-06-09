package br.com.fiap.orbitalguard.controller;

import br.com.fiap.orbitalguard.dto.alerta.AlertaResponseDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorResponseDTO;
import br.com.fiap.orbitalguard.dto.ocorrencia.OcorrenciaResponseDTO;
import br.com.fiap.orbitalguard.dto.regiao.RegiaoResponseDTO;
import br.com.fiap.orbitalguard.dto.sensor.SensorResponseDTO;
import br.com.fiap.orbitalguard.dto.usuario.UsuarioResponseDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HateoasAssembler {

    public EntityModel<RegiaoResponseDTO> toModel(RegiaoResponseDTO dto) {
        return EntityModel.of(dto)
                .add(linkTo(methodOn(RegiaoController.class).buscarPorId(dto.id())).withSelfRel())
                .add(linkTo(methodOn(RegiaoController.class).atualizar(dto.id(), null)).withRel("update"))
                .add(linkTo(methodOn(RegiaoController.class).deletar(dto.id())).withRel("delete"))
                .add(linkTo(methodOn(RegiaoController.class).listar()).withRel("regioes"))
                .add(linkTo(methodOn(SensorController.class).listar()).withRel("sensores"))
                .add(linkTo(methodOn(AlertaController.class).listar()).withRel("alertas"));
    }

    public CollectionModel<EntityModel<RegiaoResponseDTO>> toCollection(List<RegiaoResponseDTO> dtos) {
        List<EntityModel<RegiaoResponseDTO>> models = dtos.stream().map(this::toModel).toList();
        CollectionModel<EntityModel<RegiaoResponseDTO>> collection = CollectionModel.of(models);
        collection.add(linkTo(methodOn(RegiaoController.class).listar()).withSelfRel());
        return collection;
    }

    public EntityModel<SensorResponseDTO> toModel(SensorResponseDTO dto) {
        EntityModel<SensorResponseDTO> model = EntityModel.of(dto)
                .add(linkTo(methodOn(SensorController.class).buscarPorId(dto.id())).withSelfRel())
                .add(linkTo(methodOn(SensorController.class).atualizar(dto.id(), null)).withRel("update"))
                .add(linkTo(methodOn(SensorController.class).deletar(dto.id())).withRel("delete"))
                .add(linkTo(methodOn(SensorController.class).listar()).withRel("sensores"))
                .add(linkTo(methodOn(LeituraSensorController.class).listarPorSensor(dto.id())).withRel("leituras"));

        if (dto.regiaoId() != null) {
            model.add(linkTo(methodOn(RegiaoController.class).buscarPorId(dto.regiaoId())).withRel("regiao"));
        }
        return model;
    }

    public CollectionModel<EntityModel<SensorResponseDTO>> toSensorCollection(List<SensorResponseDTO> dtos) {
        List<EntityModel<SensorResponseDTO>> models = dtos.stream().map(this::toModel).toList();
        CollectionModel<EntityModel<SensorResponseDTO>> collection = CollectionModel.of(models);
        collection.add(linkTo(methodOn(SensorController.class).listar()).withSelfRel());
        return collection;
    }

    public EntityModel<LeituraSensorResponseDTO> toModel(LeituraSensorResponseDTO dto) {
        return EntityModel.of(dto)
                .add(linkTo(methodOn(LeituraSensorController.class)
                        .buscarPorId(dto.sensorId(), dto.dataHoraLeitura())).withSelfRel())
                .add(linkTo(methodOn(LeituraSensorController.class)
                        .atualizar(dto.sensorId(), dto.dataHoraLeitura(), null)).withRel("update"))
                .add(linkTo(methodOn(LeituraSensorController.class)
                        .deletar(dto.sensorId(), dto.dataHoraLeitura())).withRel("delete"))
                .add(linkTo(methodOn(SensorController.class).buscarPorId(dto.sensorId())).withRel("sensor"))
                .add(linkTo(methodOn(LeituraSensorController.class).listar()).withRel("leituras"));
    }

    public CollectionModel<EntityModel<LeituraSensorResponseDTO>> toLeituraCollection(List<LeituraSensorResponseDTO> dtos) {
        List<EntityModel<LeituraSensorResponseDTO>> models = dtos.stream().map(this::toModel).toList();
        CollectionModel<EntityModel<LeituraSensorResponseDTO>> collection = CollectionModel.of(models);
        collection.add(linkTo(methodOn(LeituraSensorController.class).listar()).withSelfRel());
        return collection;
    }

    public EntityModel<AlertaResponseDTO> toModel(AlertaResponseDTO dto) {
        EntityModel<AlertaResponseDTO> model = EntityModel.of(dto)
                .add(linkTo(methodOn(AlertaController.class).buscarPorId(dto.id())).withSelfRel())
                .add(linkTo(methodOn(AlertaController.class).atualizar(dto.id(), null)).withRel("update"))
                .add(linkTo(methodOn(AlertaController.class).deletar(dto.id())).withRel("delete"))
                .add(linkTo(methodOn(AlertaController.class).listar()).withRel("alertas"));

        if (dto.regiaoId() != null) {
            model.add(linkTo(methodOn(RegiaoController.class).buscarPorId(dto.regiaoId())).withRel("regiao"));
        }
        return model;
    }

    public CollectionModel<EntityModel<AlertaResponseDTO>> toAlertaCollection(List<AlertaResponseDTO> dtos) {
        List<EntityModel<AlertaResponseDTO>> models = dtos.stream().map(this::toModel).toList();
        CollectionModel<EntityModel<AlertaResponseDTO>> collection = CollectionModel.of(models);
        collection.add(linkTo(methodOn(AlertaController.class).listar()).withSelfRel());
        return collection;
    }

    public EntityModel<OcorrenciaResponseDTO> toModel(OcorrenciaResponseDTO dto) {
        EntityModel<OcorrenciaResponseDTO> model = EntityModel.of(dto)
                .add(linkTo(methodOn(OcorrenciaController.class).buscarPorId(dto.id())).withSelfRel())
                .add(linkTo(methodOn(OcorrenciaController.class).atualizar(dto.id(), null)).withRel("update"))
                .add(linkTo(methodOn(OcorrenciaController.class).deletar(dto.id())).withRel("delete"))
                .add(linkTo(methodOn(OcorrenciaController.class).listar()).withRel("ocorrencias"));

        if (dto.alertaId() != null) {
            model.add(linkTo(methodOn(AlertaController.class).buscarPorId(dto.alertaId())).withRel("alerta"));
        }
        return model;
    }

    public CollectionModel<EntityModel<OcorrenciaResponseDTO>> toOcorrenciaCollection(List<OcorrenciaResponseDTO> dtos) {
        List<EntityModel<OcorrenciaResponseDTO>> models = dtos.stream().map(this::toModel).toList();
        CollectionModel<EntityModel<OcorrenciaResponseDTO>> collection = CollectionModel.of(models);
        collection.add(linkTo(methodOn(OcorrenciaController.class).listar()).withSelfRel());
        return collection;
    }

    public EntityModel<UsuarioResponseDTO> toModel(UsuarioResponseDTO dto) {
        return EntityModel.of(dto)
                .add(linkTo(methodOn(UsuarioController.class).buscarPorId(dto.id())).withSelfRel())
                .add(linkTo(methodOn(UsuarioController.class).atualizar(dto.id(), null)).withRel("update"))
                .add(linkTo(methodOn(UsuarioController.class).listar()).withRel("usuarios"));
    }

    public CollectionModel<EntityModel<UsuarioResponseDTO>> toUsuarioCollection(List<UsuarioResponseDTO> dtos) {
        List<EntityModel<UsuarioResponseDTO>> models = dtos.stream().map(this::toModel).toList();
        CollectionModel<EntityModel<UsuarioResponseDTO>> collection = CollectionModel.of(models);
        collection.add(linkTo(methodOn(UsuarioController.class).listar()).withSelfRel());
        return collection;
    }
}
