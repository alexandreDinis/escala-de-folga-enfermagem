package com.oroboros.EscalaDeFolga.app.dto.calendario;

import java.time.LocalDate;
import java.util.List;

/**
 * Representa um dia no calendário com informações para o frontend
 */
public record CalendarioDiaDTO(
        int dia,
        LocalDate data,
        String diaSemana,
        String status,
        String corStatus,
        boolean clicavel,
        String motivoNaoClicavel,
        int totalFolgasNoDia,
        int colaboradoresDisponiveis,
        int limiteMaximoFolgas,
        List<FolgaResumoDiaDTO> folgasNoDia
) {}
