package br.com.fiap.orbitalguard.service;

import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorRequestDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorResponseDTO;
import br.com.fiap.orbitalguard.dto.leitura.LeituraSensorUpdateDTO;
import br.com.fiap.orbitalguard.exception.ResourceNotFoundException;
import br.com.fiap.orbitalguard.mapper.EntityMapper;
import br.com.fiap.orbitalguard.model.LeituraSensor;
import br.com.fiap.orbitalguard.model.LeituraSensorId;
import br.com.fiap.orbitalguard.model.Sensor;
import br.com.fiap.orbitalguard.repository.LeituraSensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeituraSensorService {

    private final LeituraSensorRepository leituraSensorRepository;
    private final SensorService sensorService;

    @Transactional(readOnly = true)
    public List<LeituraSensorResponseDTO> listar() {
        return leituraSensorRepository.findAll().stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LeituraSensorResponseDTO> listarPorSensor(Long sensorId) {
        sensorService.findEntity(sensorId);
        return leituraSensorRepository.findBySensorId(sensorId).stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public LeituraSensorResponseDTO buscarPorId(Long sensorId, LocalDateTime dataHora) {
        return EntityMapper.toDto(findEntity(sensorId, dataHora));
    }

    @Transactional
    public LeituraSensorResponseDTO criar(LeituraSensorRequestDTO dto) {
        Sensor sensor = sensorService.findEntity(dto.sensorId());
        LeituraSensor leitura = EntityMapper.toEntity(dto, sensor);
        return EntityMapper.toDto(leituraSensorRepository.save(leitura));
    }

    @Transactional
    public LeituraSensorResponseDTO atualizar(Long sensorId, LocalDateTime dataHora, LeituraSensorUpdateDTO dto) {
        LeituraSensor leitura = findEntity(sensorId, dataHora);
        EntityMapper.updateLeitura(leitura, dto);
        return EntityMapper.toDto(leituraSensorRepository.save(leitura));
    }

    @Transactional
    public void deletar(Long sensorId, LocalDateTime dataHora) {
        LeituraSensor leitura = findEntity(sensorId, dataHora);
        leituraSensorRepository.delete(leitura);
    }

    public LeituraSensor findEntity(Long sensorId, LocalDateTime dataHora) {
        LeituraSensorId id = LeituraSensorId.builder()
                .sensorId(sensorId)
                .dataHoraLeitura(dataHora)
                .build();
        return leituraSensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Leitura nao encontrada para sensor " + sensorId + " em " + dataHora));
    }
}
