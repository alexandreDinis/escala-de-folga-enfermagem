package com.oroboros.EscalaDeFolga.app.dto.calendario;

import java.util.List;

/**
 * Response completo do calendário para o frontend
 */
public record CalendarioResponseDTO(
        Long escalaId,
        int mes,
        int ano,
        String mesNome,
        String turno,
        String setorNome,
        List<CalendarioDiaDTO> dias,
        List<ColaboradorCalendarioDTO> colaboradores,
        ResumoCalendarioDTO resumo,
        ConfiguracaoCalendarioDTO configuracao
) {}
