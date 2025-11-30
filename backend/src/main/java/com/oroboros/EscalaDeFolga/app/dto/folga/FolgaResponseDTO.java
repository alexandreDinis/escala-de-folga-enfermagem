package com.oroboros.EscalaDeFolga.app.dto.folga;

import com.oroboros.EscalaDeFolga.app.dto.alerta.AlertaDTO;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;

import java.time.LocalDate;
import java.util.List;

public record FolgaResponseDTO(
        Long id,
        Long escalaId,
        Long colaboradorId,
        String colaboradorNome,
        LocalDate dataSolicitada,
        String diaSemana,
        boolean isDomingo,
        String status,
        String justificativa,
        LocalDate dataCriacao,
        List<AlertaDTO> alertas,
        ProximasFolgasDTO proximasFolgas
) {}
