package com.oroboros.EscalaDeFolga.app.mapper;


import com.oroboros.EscalaDeFolga.app.dto.escala.SetorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.SetorResposnseDTO;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SetorMapper {

    @Mapping(target = "id", ignore = true)
    Setor toEntity(SetorRequestDTO dto);

    SetorResposnseDTO toResponse(Setor setor);
}
