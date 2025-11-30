package com.oroboros.EscalaDeFolga.domain.model.alerta;

/**
 * Níveis de severidade de alertas
 */
public enum SeveridadeEnum {
    CRITICA(1, "🔴 CRÍTICO"),      // Bloqueia operação
    ALTA(2, "🟠 ALTO"),             // Requer atenção imediata
    MEDIA(3, "🟡 MÉDIO"),           // Recomendação importante
    BAIXA(4, "🟢 BAIXO"),           // Informativo
    INFO(5, "ℹ️ INFO");             // Apenas aviso

    private final int prioridade;
    private final String label;

    SeveridadeEnum(int prioridade, String label) {
        this.prioridade = prioridade;
        this.label = label;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Verifica se esta severidade é mais crítica que outra
     */
    public boolean maisCriticaQue(SeveridadeEnum other) {
        return this.prioridade < other.prioridade;
    }
}
