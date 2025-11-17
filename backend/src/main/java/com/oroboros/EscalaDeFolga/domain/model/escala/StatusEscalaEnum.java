package com.oroboros.EscalaDeFolga.domain.model.escala;

public enum StatusEscalaEnum {
    NOVA,        // Criada mas sem dados
    PARCIAL,     // Possui escala-colaborador mas ainda sem folgas aprovadas
    PUBLICADA,   // Folgas já foram aprovadas, escala ativa
    FECHADA      // Mês encerrado
}
