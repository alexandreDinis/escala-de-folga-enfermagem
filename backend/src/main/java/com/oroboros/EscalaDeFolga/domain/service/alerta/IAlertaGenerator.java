package com.oroboros.EscalaDeFolga.domain.service.alerta;

import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;

import java.util.List;

/**
 * Interface para geradores de alertas.
 *
 * Diferente dos validadores (que BLOQUEIAM),
 * os geradores de alertas AVISAM sem impedir a operação.
 */
public interface IAlertaGenerator {

    /**
     * Gera lista de alertas para uma folga.
     * Retorna lista vazia se não houver alertas.
     */
    List<Alerta> gerarAlertas(Folga folga);
}