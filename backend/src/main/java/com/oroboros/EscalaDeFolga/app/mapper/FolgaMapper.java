package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.folga.FolgaRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.folga.FolgaResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Mapper para conversão de DTOs de Folga
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class FolgaMapper {

    @Autowired
    protected EscalaRepository escalaRepository;

    @Autowired
    protected ColaboradorRepository colaboradorRepository;

    /**
     * Converte RequestDTO para entidade Folga
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "escala", source = "escalaId", qualifiedByName = "mapEscalaFromId")
    @Mapping(target = "colaborador", source = "colaboradorId", qualifiedByName = "mapColaboradorFromId")
    public abstract Folga toEntity(FolgaRequestDTO dto);

    /**
     * Converte entidade Folga para ResponseDTO (base - sem alertas e próximas folgas)
     */
    @Mapping(target = "escalaId", source = "escala.id")
    @Mapping(target = "colaboradorId", source = "colaborador.id")
    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "diaSemana", expression = "java(folga.getDataSolicitada().getDayOfWeek().name())")
    @Mapping(target = "isDomingo", expression = "java(folga.getDataSolicitada().getDayOfWeek() == java.time.DayOfWeek.SUNDAY)")
    @Mapping(target = "status", expression = "java(folga.getStatus().name())")
    @Mapping(target = "dataCriacao", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "alertas", ignore = true)
    @Mapping(target = "proximasFolgas", ignore = true)
    public abstract FolgaResponseDTO toResponse(Folga folga);

    /**
     * Busca Escala completa do banco (com mês, ano, setor, etc.)
     */
    @Named("mapEscalaFromId")
    protected Escala mapEscalaFromId(Long escalaId) {
        if (escalaId == null) {
            throw new BusinessException("ID da escala é obrigatório");
        }

        return escalaRepository.findById(escalaId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Escala com ID %d não encontrada", escalaId)
                ));
    }

    /**
     * Busca Colaborador completo do banco (com nome, setor, turno, etc.)
     */
    @Named("mapColaboradorFromId")
    protected Colaborador mapColaboradorFromId(Long colaboradorId) {
        if (colaboradorId == null) {
            throw new BusinessException("ID do colaborador é obrigatório");
        }

        return colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new BusinessException(
                        String.format("Colaborador com ID %d não encontrado", colaboradorId)
                ));
    }
}