package com.oroboros.EscalaDeFolga.app.mapper;


import com.oroboros.EscalaDeFolga.app.dto.setor.SetorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.setor.SetorResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface SetorMapper {

    @Mapping(target = "id", ignore = true)

    Setor toEntity(SetorRequestDTO dto);

    SetorResponseDTO toResponse(Setor setor);
}
