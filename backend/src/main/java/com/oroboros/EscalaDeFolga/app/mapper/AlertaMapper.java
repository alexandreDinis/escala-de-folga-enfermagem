package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.alerta.AlertaDTO;
import com.oroboros.EscalaDeFolga.app.dto.alerta.AlertaResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.model.alerta.SeveridadeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper para conversão de DTOs de Alerta
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AlertaMapper {

    /**
     * Converte Alerta para DTO simples
     */
    @Mapping(target = "tipo", expression = "java(alerta.getTipo().name())")
    @Mapping(target = "severidade", expression = "java(alerta.getSeveridade().name())")
    @Mapping(target = "titulo", source = "tipo.descricao")
    @Mapping(target = "icone", expression = "java(mapSeveridadeParaIcone(alerta.getSeveridade()))")
    @Mapping(target = "cor", expression = "java(mapSeveridadeParaCor(alerta.getSeveridade()))")
    AlertaDTO toDTO(Alerta alerta);

    /**
     * Converte Alerta para ResponseDTO completo
     */
    @Mapping(target = "escalaId", source = "escala.id")
    @Mapping(target = "colaboradorId", source = "colaborador.id")
    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "folgaId", source = "folga.id")
    @Mapping(target = "tipo", expression = "java(alerta.getTipo().name())")
    @Mapping(target = "severidade", expression = "java(alerta.getSeveridade().name())")
    @Mapping(target = "titulo", source = "tipo.descricao")
    AlertaResponseDTO toResponse(Alerta alerta);

    /**
     * Mapeia severidade para ícone
     */
    default String mapSeveridadeParaIcone(SeveridadeEnum severidade) {
        return switch (severidade) {
            case CRITICA -> "🔴";
            case ALTA -> "🟠";
            case MEDIA -> "🟡";
            case BAIXA -> "🟢";
            case INFO -> "ℹ️";
        };
    }

    /**
     * Mapeia severidade para cor
     */
    default String mapSeveridadeParaCor(SeveridadeEnum severidade) {
        return switch (severidade) {
            case CRITICA -> "#F44336";
            case ALTA -> "#FF9800";
            case MEDIA -> "#FFC107";
            case BAIXA -> "#4CAF50";
            case INFO -> "#2196F3";
        };
    }
}
