package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "setor", source = "setor", qualifiedByName = "fromId")
    Colaborador toEntity(ColaboradorRequestDTO dto);

    ColaboradorResponseDTO toResponse(Colaborador entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "setor", source = "setor", qualifiedByName = "fromId")
    void updateEntityFromDto(ColaboradorRequestDTO dto, @MappingTarget Colaborador entity);
}

