package br.com.fiap.orbitalguard.service;

import br.com.fiap.orbitalguard.dto.sensor.SensorRequestDTO;
import br.com.fiap.orbitalguard.dto.sensor.SensorResponseDTO;
import br.com.fiap.orbitalguard.exception.BusinessException;
import br.com.fiap.orbitalguard.exception.ResourceNotFoundException;
import br.com.fiap.orbitalguard.mapper.EntityMapper;
import br.com.fiap.orbitalguard.model.Regiao;
import br.com.fiap.orbitalguard.model.Sensor;
import br.com.fiap.orbitalguard.model.enums.TipoSensor;
import br.com.fiap.orbitalguard.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final RegiaoService regiaoService;

    @Transactional(readOnly = true)
    public List<SensorResponseDTO> listar() {
        return sensorRepository.findAll().stream()
                .map(EntityMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SensorResponseDTO buscarPorId(Long id) {
        return EntityMapper.toDto(findEntity(id));
    }

    @Transactional
    public SensorResponseDTO criar(SensorRequestDTO dto) {
        Regiao regiao = regiaoService.findEntity(dto.regiaoId());
        Sensor sensor = EntityMapper.createSensor(dto, regiao);
        return EntityMapper.toDto(sensorRepository.save(sensor));
    }

    @Transactional
    public SensorResponseDTO atualizar(Long id, SensorRequestDTO dto) {
        Sensor sensor = findEntity(id);
        validarTipoSensor(sensor, dto.tipo());
        Regiao regiao = regiaoService.findEntity(dto.regiaoId());
        EntityMapper.updateSensor(sensor, dto, regiao);
        return EntityMapper.toDto(sensorRepository.save(sensor));
    }

    @Transactional
    public void deletar(Long id) {
        Sensor sensor = findEntity(id);
        sensorRepository.delete(sensor);
    }

    public Sensor findEntity(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor nao encontrado com id: " + id));
    }

    private void validarTipoSensor(Sensor sensor, TipoSensor tipo) {
        boolean isAgua = sensor instanceof br.com.fiap.orbitalguard.model.SensorAgua;
        if ((tipo == TipoSensor.AGUA && !isAgua) || (tipo == TipoSensor.AR && isAgua)) {
            throw new BusinessException("Nao e permitido alterar o tipo do sensor");
        }
    }
}
