package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EscalaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "registros", ignore = true)
    @Mapping(target = "folgas", ignore = true)
    @Mapping(target = "setor", source = "setorId", qualifiedByName = "mapSetorFromId")
    Escala toEntity(EscalaRequestDTO dto);

    @Mapping(target = "setorId", source = "setor.id")
    EscalaResponseDTO toResponse(Escala escala);

    @Named("mapSetorFromId")
    default Setor mapSetorFromId(Long setorId) {
        if (setorId == null) return null;
        Setor setor = new Setor();
        setor.setId(setorId);
        return setor;
    }
}
