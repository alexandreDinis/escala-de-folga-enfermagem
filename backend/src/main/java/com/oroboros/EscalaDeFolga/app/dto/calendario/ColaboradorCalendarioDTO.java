package com.oroboros.EscalaDeFolga.app.dto.calendario;

import java.time.LocalDate;
import java.util.List;

/**
 * Colaborador com informações para o calendário
 */
public record ColaboradorCalendarioDTO(
        Long id,
        String nome,
        String turno,
        LocalDate ultimaFolga,
        int diasDesdeUltimaFolga,
        LocalDate proximaDataDisponivel,
        int totalFolgasNoMes,
        int folgasRestantes,
        List<LocalDate> datasComFolga,
        boolean temDomingo,
        boolean emRisco,
        List<String> alertas,
        boolean podeFolgarHoje,
        String corStatus
) {}
