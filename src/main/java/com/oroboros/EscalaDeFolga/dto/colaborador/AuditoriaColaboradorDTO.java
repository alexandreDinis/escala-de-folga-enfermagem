package com.oroboros.EscalaDeFolga.dto.colaborador;


import com.oroboros.EscalaDeFolga.model.colaborador.AcaoAuditoriaEnum;
import com.oroboros.EscalaDeFolga.model.colaborador.AuditoriaColaborador;

import java.time.LocalDateTime;

public record AuditoriaColaboradorDTO(
        Long id,
        Long colaboradorId,
        AcaoAuditoriaEnum acao,
        Long usuarioId,
        String usuarioNome,
        LocalDateTime dataHora,
        String dadosAnteriores,
        String dadosNovos,
        String ipOrigem,
        String userAgent
) {
    public static AuditoriaColaboradorDTO fromEntity(AuditoriaColaborador auditoria) {
        return new AuditoriaColaboradorDTO(
                auditoria.getId(),
                auditoria.getColaboradorId(),
                auditoria.getAcao(),
                auditoria.getUsuarioId(),
                auditoria.getUsuarioNome(),
                auditoria.getDataHora(),
                auditoria.getDadosAnteriores(),
                auditoria.getDadosNovos(),
                auditoria.getIpOrigem(),
                auditoria.getUserAgent()
        );
    }
}

