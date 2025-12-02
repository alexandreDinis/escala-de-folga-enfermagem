package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.app.dto.colaborador.AuditoriaInfoDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorUpdateDTO;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.AcaoAuditoriaEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.infrastructure.repository.AuditoriaColaboradorRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import com.oroboros.EscalaDeFolga.app.mapper.ColaboradorMapper;
import com.oroboros.EscalaDeFolga.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;

    private final AuditoriaColaboradorRepository auditoriaRepository;

    private final AuditoriaColaboradorService auditoriaService;

    private final ColaboradorMapper colaboradorMapper;


    public ColaboradorResponseDTO cadastrar(ColaboradorRequestDTO colaboradorDTO, AuditoriaInfoDTO auditor)  {

        Colaborador colaborador = colaboradorMapper.toEntity(colaboradorDTO);

        colaborador.setNome(colaborador.getNome()
                .trim()
                .replaceAll("\\s+", " ")
                .toUpperCase());

        colaboradorRepository.save(colaborador);

        //todo:dados ficticios aguardando a implementação da segurança e usuarios.

        auditoriaService.criarAuditoria(
                AcaoAuditoriaEnum.CRIACAO,
                colaborador,
                auditor,
                null,
                JsonUtil.toJson(colaborador)
        );
        return colaboradorMapper.toResponse(colaborador);
    }


    public Page<ColaboradorResponseDTO> listar(Pageable pageable) {
        return colaboradorRepository.findByAtivoTrue(pageable).map(colaboradorMapper::toResponse);
    }


    public ColaboradorResponseDTO buscarPorId(Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Colaborador", id));
        return colaboradorMapper.toResponse(colaborador);
    }


    public void inativar(Long id, AuditoriaInfoDTO auditor) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Colaborador", id));

        String dadosAnteriores = JsonUtil.toJson(colaborador);

        if (colaborador.isAtivo()) {
            colaborador.delete();
            colaboradorRepository.save(colaborador);
        }

        auditoriaService.criarAuditoria(
                AcaoAuditoriaEnum.INATIVACAO,
                colaborador,
                auditor,
                dadosAnteriores,
                JsonUtil.toJson(colaborador)
        );
    }

    public ColaboradorResponseDTO atualizar(Long id, AuditoriaInfoDTO auditor, ColaboradorUpdateDTO colaboradorUpdateDTO) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Colaborador", id));

        String dadosAnteriores = JsonUtil.toJson(colaborador);

        if (colaboradorUpdateDTO.nome() != null) {
            colaborador.setNome(colaboradorUpdateDTO.nome());
        }
        if (colaboradorUpdateDTO.cargo() != null) {
            colaborador.setCargo(colaboradorUpdateDTO.cargo());
        }
        if (colaboradorUpdateDTO.turno() != null) {
            colaborador.setTurno(colaboradorUpdateDTO.turno());
        }

        colaboradorRepository.save(colaborador);
        auditoriaService.criarAuditoria(
                AcaoAuditoriaEnum.ATUALIZACAO,
                colaborador,
                auditor,
                dadosAnteriores,
                JsonUtil.toJson(colaborador)
        );

        return colaboradorMapper.toResponse(colaborador);
    }

    /**
            * Busca colaborador como entidade (para manipulação interna)
     */
    public Colaborador buscarPorIdEntity(Long id) {
        return colaboradorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Colaborador", id));
    }

    /**
     * Atualiza última folga do colaborador manualmente
     */
    public ColaboradorResponseDTO atualizarUltimaFolga(Long id, LocalDate ultimaFolga) {
        Colaborador colaborador = buscarPorIdEntity(id);

        colaborador.setUltimaFolga(ultimaFolga);

        Colaborador atualizado = colaboradorRepository.save(colaborador);

        return colaboradorMapper.toResponse(atualizado);
    }
}
