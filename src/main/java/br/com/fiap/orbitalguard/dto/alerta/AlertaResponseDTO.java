package br.com.fiap.orbitalguard.dto.alerta;

import br.com.fiap.orbitalguard.model.enums.NivelAlerta;
import br.com.fiap.orbitalguard.model.enums.StatusAlerta;

import java.time.LocalDateTime;

public record AlertaResponseDTO(
        Long id,
        String mensagem,
        NivelAlerta nivel,
        StatusAlerta status,
        LocalDateTime dataHora,
        Long regiaoId
) {
}
