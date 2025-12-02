package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaResponseDTO;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper MapStruct para conversão entre DTOs e entidades de Escala.
 *
 * <p>Cria apenas uma referência simples ao Setor com o ID.
 * O Service é responsável por buscar e associar o setor completo após validação.
 *
 * @author Alexandre
 */
@Mapper(componentModel = "spring")
public interface EscalaMapper {

    /**
     * Converte DTO de request para entidade Escala.
     *
     * <p>Propriedades ignoradas (gerenciadas pelo sistema):
     * <ul>
     *   <li><b>id:</b> Gerado pelo banco</li>
     *   <li><b>status:</b> Valor padrão NOVA definido na entidade</li>
     *   <li><b>registros:</b> Coleção de relacionamento</li>
     *   <li><b>folgas:</b> Coleção de relacionamento</li>
     * </ul>
     *
     * @param dto dados da requisição
     * @return entidade Escala com setor (apenas ID)
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "registros", ignore = true)
    @Mapping(target = "folgas", ignore = true)
    @Mapping(target = "setor", source = "setorId", qualifiedByName = "mapSetorFromId")
    Escala toEntity(EscalaRequestDTO dto);

    /**
     * Converte entidade Escala para DTO de resposta.
     *
     * @param escala entidade persistida
     * @return DTO de resposta
     */
    @Mapping(target = "setorId", source = "setor.id")
    @Mapping(target = "setorNome", source = "setor.nome")
    EscalaResponseDTO toResponse(Escala escala);

    /**
     * Cria um objeto Setor apenas com ID para referência temporária.
     * O Service buscará o setor completo após validação.
     *
     * @param setorId ID do setor
     * @return Setor com apenas o ID preenchido
     */
    @Named("mapSetorFromId")
    default Setor mapSetorFromId(Long setorId) {
        if (setorId == null) {
            return null;
        }
        Setor setor = new Setor();
        setor.setId(setorId);
        return setor;
    }
}
