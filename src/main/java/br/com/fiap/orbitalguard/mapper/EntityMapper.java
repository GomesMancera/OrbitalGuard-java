package br.com.fiap.orbitalguard.mapper;

import br.com.fiap.orbitalguard.dto.alerta.AlertaRequestDTO;
import br.com.fiap.orbitalguard.dto.alerta.AlertaResponseDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorRequestDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorResponseDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorUpdateDTO;
import br.com.fiap.orbitalguard.dto.ocorrencia.OcorrenciaRequestDTO;
import br.com.fiap.orbitalguard.dto.ocorrencia.OcorrenciaResponseDTO;
import br.com.fiap.orbitalguard.dto.regiao.RegiaoRequestDTO;
import br.com.fiap.orbitalguard.dto.regiao.RegiaoResponseDTO;
import br.com.fiap.orbitalguard.dto.sensor.SensorRequestDTO;
import br.com.fiap.orbitalguard.dto.sensor.SensorResponseDTO;
import br.com.fiap.orbitalguard.dto.usuario.UsuarioResponseDTO;
import br.com.fiap.orbitalguard.model.Alerta;
import br.com.fiap.orbitalguard.model.Coordenadas;
import br.com.fiap.orbitalguard.model.LeituraSensor;
import br.com.fiap.orbitalguard.model.LeituraSensorId;
import br.com.fiap.orbitalguard.model.Ocorrencia;
import br.com.fiap.orbitalguard.model.Regiao;
import br.com.fiap.orbitalguard.model.Sensor;
import br.com.fiap.orbitalguard.model.SensorAgua;
import br.com.fiap.orbitalguard.model.SensorAr;
import br.com.fiap.orbitalguard.model.Usuario;
import br.com.fiap.orbitalguard.model.enums.TipoSensor;

public final class EntityMapper {

    private EntityMapper() {
    }

    public static Regiao toEntity(RegiaoRequestDTO dto) {
        return Regiao.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .coordenadas(Coordenadas.builder()
                        .latitude(dto.latitude())
                        .longitude(dto.longitude())
                        .build())
                .build();
    }

    public static void updateRegiao(Regiao regiao, RegiaoRequestDTO dto) {
        regiao.setNome(dto.nome());
        regiao.setDescricao(dto.descricao());
        regiao.setCoordenadas(Coordenadas.builder()
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .build());
    }

    public static RegiaoResponseDTO toDto(Regiao regiao) {
        return new RegiaoResponseDTO(
                regiao.getId(),
                regiao.getNome(),
                regiao.getDescricao(),
                regiao.getCoordenadas() != null ? regiao.getCoordenadas().getLatitude() : null,
                regiao.getCoordenadas() != null ? regiao.getCoordenadas().getLongitude() : null
        );
    }

    public static Sensor createSensor(SensorRequestDTO dto, Regiao regiao) {
        Coordenadas coordenadas = Coordenadas.builder()
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .build();

        if (dto.tipo() == TipoSensor.AGUA) {
            return SensorAgua.builder()
                    .nome(dto.nome())
                    .localizacao(dto.localizacao())
                    .regiao(regiao)
                    .coordenadas(coordenadas)
                    .ativo(true)
                    .phMinimo(dto.phMinimo())
                    .phMaximo(dto.phMaximo())
                    .turbidezMaxima(dto.turbidezMaxima())
                    .build();
        }

        return SensorAr.builder()
                .nome(dto.nome())
                .localizacao(dto.localizacao())
                .regiao(regiao)
                .coordenadas(coordenadas)
                .ativo(true)
                .pm25Maximo(dto.pm25Maximo())
                .co2Maximo(dto.co2Maximo())
                .umidadeRelativa(dto.umidadeRelativa())
                .build();
    }

    public static void updateSensor(Sensor sensor, SensorRequestDTO dto, Regiao regiao) {
        sensor.setNome(dto.nome());
        sensor.setLocalizacao(dto.localizacao());
        sensor.setRegiao(regiao);
        sensor.setCoordenadas(Coordenadas.builder()
                .latitude(dto.latitude())
                .longitude(dto.longitude())
                .build());

        if (sensor instanceof SensorAgua sensorAgua && dto.tipo() == TipoSensor.AGUA) {
            sensorAgua.setPhMinimo(dto.phMinimo());
            sensorAgua.setPhMaximo(dto.phMaximo());
            sensorAgua.setTurbidezMaxima(dto.turbidezMaxima());
        } else if (sensor instanceof SensorAr sensorAr && dto.tipo() == TipoSensor.AR) {
            sensorAr.setPm25Maximo(dto.pm25Maximo());
            sensorAr.setCo2Maximo(dto.co2Maximo());
            sensorAr.setUmidadeRelativa(dto.umidadeRelativa());
        }
    }

    public static SensorResponseDTO toDto(Sensor sensor) {
        TipoSensor tipo = sensor instanceof SensorAgua ? TipoSensor.AGUA : TipoSensor.AR;
        Double phMinimo = null;
        Double phMaximo = null;
        Double turbidezMaxima = null;
        Double pm25Maximo = null;
        Double co2Maximo = null;
        Double umidadeRelativa = null;

        if (sensor instanceof SensorAgua sensorAgua) {
            phMinimo = sensorAgua.getPhMinimo();
            phMaximo = sensorAgua.getPhMaximo();
            turbidezMaxima = sensorAgua.getTurbidezMaxima();
        } else if (sensor instanceof SensorAr sensorAr) {
            pm25Maximo = sensorAr.getPm25Maximo();
            co2Maximo = sensorAr.getCo2Maximo();
            umidadeRelativa = sensorAr.getUmidadeRelativa();
        }

        return new SensorResponseDTO(
                sensor.getId(),
                sensor.getNome(),
                sensor.getLocalizacao(),
                tipo,
                sensor.getAtivo(),
                sensor.getRegiao() != null ? sensor.getRegiao().getId() : null,
                sensor.getCoordenadas() != null ? sensor.getCoordenadas().getLatitude() : null,
                sensor.getCoordenadas() != null ? sensor.getCoordenadas().getLongitude() : null,
                phMinimo,
                phMaximo,
                turbidezMaxima,
                pm25Maximo,
                co2Maximo,
                umidadeRelativa
        );
    }

    public static LeituraSensor toEntity(LeituraSensorRequestDTO dto, Sensor sensor) {
        LeituraSensorId id = LeituraSensorId.builder()
                .sensorId(dto.sensorId())
                .dataHoraLeitura(dto.dataHoraLeitura())
                .build();

        return LeituraSensor.builder()
                .id(id)
                .sensor(sensor)
                .valor(dto.valor())
                .unidade(dto.unidade())
                .observacao(dto.observacao())
                .build();
    }

    public static void updateLeitura(LeituraSensor leitura, LeituraSensorUpdateDTO dto) {
        leitura.setValor(dto.valor());
        leitura.setUnidade(dto.unidade());
        leitura.setObservacao(dto.observacao());
    }

    public static LeituraSensorResponseDTO toDto(LeituraSensor leitura) {
        return new LeituraSensorResponseDTO(
                leitura.getId().getSensorId(),
                leitura.getId().getDataHoraLeitura(),
                leitura.getValor(),
                leitura.getUnidade(),
                leitura.getObservacao()
        );
    }

    public static Alerta toEntity(AlertaRequestDTO dto, Regiao regiao) {
        return Alerta.builder()
                .mensagem(dto.mensagem())
                .nivel(dto.nivel())
                .status(dto.status())
                .dataHora(dto.dataHora())
                .regiao(regiao)
                .build();
    }

    public static void updateAlerta(Alerta alerta, AlertaRequestDTO dto, Regiao regiao) {
        alerta.setMensagem(dto.mensagem());
        alerta.setNivel(dto.nivel());
        alerta.setStatus(dto.status());
        alerta.setDataHora(dto.dataHora());
        alerta.setRegiao(regiao);
    }

    public static AlertaResponseDTO toDto(Alerta alerta) {
        return new AlertaResponseDTO(
                alerta.getId(),
                alerta.getMensagem(),
                alerta.getNivel(),
                alerta.getStatus(),
                alerta.getDataHora(),
                alerta.getRegiao() != null ? alerta.getRegiao().getId() : null
        );
    }

    public static Ocorrencia toEntity(OcorrenciaRequestDTO dto, Alerta alerta, Usuario usuario) {
        return Ocorrencia.builder()
                .descricao(dto.descricao())
                .dataHora(dto.dataHora())
                .status(dto.status())
                .alerta(alerta)
                .usuarioResponsavel(usuario)
                .build();
    }

    public static void updateOcorrencia(Ocorrencia ocorrencia, OcorrenciaRequestDTO dto, Alerta alerta, Usuario usuario) {
        ocorrencia.setDescricao(dto.descricao());
        ocorrencia.setDataHora(dto.dataHora());
        ocorrencia.setStatus(dto.status());
        ocorrencia.setAlerta(alerta);
        ocorrencia.setUsuarioResponsavel(usuario);
    }

    public static OcorrenciaResponseDTO toDto(Ocorrencia ocorrencia) {
        return new OcorrenciaResponseDTO(
                ocorrencia.getId(),
                ocorrencia.getDescricao(),
                ocorrencia.getDataHora(),
                ocorrencia.getStatus(),
                ocorrencia.getAlerta() != null ? ocorrencia.getAlerta().getId() : null,
                ocorrencia.getUsuarioResponsavel() != null ? ocorrencia.getUsuarioResponsavel().getId() : null
        );
    }

    public static UsuarioResponseDTO toDto(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    }
}
