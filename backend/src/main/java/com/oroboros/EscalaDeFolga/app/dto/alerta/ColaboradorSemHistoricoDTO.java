package com.oroboros.EscalaDeFolga.app.dto.alerta;

import java.time.LocalDate;

public record ColaboradorSemHistoricoDTO(
        Long id,
        String nome,
        String cargo,
        String turno,
        LocalDate ultimaFolga // null se nunca teve
) {}
