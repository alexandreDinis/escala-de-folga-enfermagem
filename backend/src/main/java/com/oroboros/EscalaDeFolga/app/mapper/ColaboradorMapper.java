package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ColaboradorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "setor", source = "setorId", qualifiedByName = "mapSetorFromId")
    Colaborador toEntity(ColaboradorRequestDTO dto);

    @Named("mapSetorFromId")
    default Setor mapSetorFromId(Long setorId) {
        if (setorId == null) return null;
        Setor setor = new Setor();
        setor.setId(setorId); // Apenas vincula por ID, sem acessar o banco
        return setor;
    }

    @Mapping(target = "setorId", source = "setor.id")
    ColaboradorResponseDTO toResponse(Colaborador entity);

}

