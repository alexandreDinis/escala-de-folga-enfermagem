package com.oroboros.EscalaDeFolga.app.dto.folga;

import java.time.LocalDate;
import java.util.List;

/**
 * Próximas datas disponíveis para folgar
 */
public record ProximasFolgasDTO(
        LocalDate proximaDataDisponivel,
        List<LocalDate> proximasCincoDatas,
        int diasAteProximaFolga
) {}
