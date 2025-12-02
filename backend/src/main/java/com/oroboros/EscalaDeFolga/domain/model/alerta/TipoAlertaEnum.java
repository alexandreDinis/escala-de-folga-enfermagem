package com.oroboros.EscalaDeFolga.domain.model.alerta;



/**
 * Tipos de alertas do sistema
 */
public enum TipoAlertaEnum {

    // ========================================
    // ALERTAS DE ADVERTÊNCIA (não bloqueiam)
    // ========================================

    AVISO_DOMINGO_FALTANDO(
            "Domingo Obrigatório Pendente",
            "Colaborador ainda não possui folga em domingo neste mês",
            SeveridadeEnum.ALTA
    ),

    AVISO_INTERVALO_MINIMO_CURTO(
            "Intervalo Curto Entre Folgas",
            "Intervalo entre folgas está abaixo do recomendado",
            SeveridadeEnum.MEDIA
    ),

    AVISO_DESEQUILIBRIO_TURNO(
            "Desequilíbrio de Cobertura",
            "Turno ficará com cobertura abaixo do ideal",
            SeveridadeEnum.ALTA
    ),

    AVISO_CONCENTRACAO_FOLGAS(
            "Concentração de Folgas",
            "Muitas folgas concentradas no mesmo período",
            SeveridadeEnum.MEDIA
    ),

    AVISO_RISCO_INSUFICIENCIA(
            "Risco de Folgas Insuficientes",
            "Colaborador pode não completar todas as folgas do mês",
            SeveridadeEnum.MEDIA
    ),

    // ========================================
    // ALERTAS INFORMATIVOS
    // ========================================

    INFO_PROXIMAS_DATAS_SUGERIDAS(
            "Próximas Datas Disponíveis",
            "Sugestões de datas viáveis para próximas folgas",
            SeveridadeEnum.BAIXA
    ),

    INFO_DISTRIBUICAO_ADEQUADA(
            "Distribuição Adequada",
            "Folga alocada de forma equilibrada no mês",
            SeveridadeEnum.BAIXA
    ),

    INFO_RESUMO_FOLGAS(
            "Resumo de Folgas",
            "Informações sobre folgas do colaborador no mês",
            SeveridadeEnum.INFO
    );

    private final String titulo;
    private final String descricao;
    private final SeveridadeEnum severidadeDefault;

    TipoAlertaEnum(String titulo, String descricao, SeveridadeEnum severidadeDefault) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.severidadeDefault = severidadeDefault;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public SeveridadeEnum getSeveridadeDefault() {
        return severidadeDefault;
    }
}
