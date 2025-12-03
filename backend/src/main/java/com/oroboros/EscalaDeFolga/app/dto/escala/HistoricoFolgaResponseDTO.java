package com.oroboros.EscalaDeFolga.app.dto.escala;

import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorResponseDTO;
import java.util.List;

public record HistoricoFolgaResponseDTO(
        boolean faltaHistorico,
        List<ColaboradorResponseDTO> colaboradoresSemHistorico,
        int totalSemHistorico,
        int totalColaboradores
) {}
