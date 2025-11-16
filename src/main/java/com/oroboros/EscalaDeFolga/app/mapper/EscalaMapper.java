package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EscalaMapper {

    @Mapping(target = "id", ignore = true)
    Escala toEntity(EscalaRequestDTO dto);

    EscalaResponseDTO toResponse(Escala escala);
}
