package com.oroboros.EscalaDeFolga.app.dto.calendario;

public record ConfiguracaoCalendarioDTO(
        int folgasPermitidasPorColaborador,
        int diasTrabalhoMaximo,
        int intervaloMinimoRecomendado,
        boolean domingoObrigatorio,
        int limiteFolgasSimultaneas
) {}
