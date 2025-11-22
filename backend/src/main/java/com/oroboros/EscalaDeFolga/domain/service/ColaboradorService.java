package com.oroboros.EscalaDeFolga.domain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.AuditoriaInfoDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorUpdateDTO;
import com.oroboros.EscalaDeFolga.app.mapper.ColaboradorMapper;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.AcaoAuditoriaEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.infrastructure.repository.AuditoriaColaboradorRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import com.oroboros.EscalaDeFolga.util.JsonUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private AuditoriaColaboradorRepository auditoriaRepository;

    @Autowired
    private AuditoriaColaboradorService auditoriaService;

    private ColaboradorMapper colaboradorMapper;


    public ColaboradorResponseDTO cadastrar(ColaboradorRequestDTO colaboradorDTO, AuditoriaInfoDTO auditor) throws JsonProcessingException {

        Colaborador colaborador = colaboradorMapper.toEntity(colaboradorDTO);
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
                .orElseThrow(() -> new EntityNotFoundException("Não há colaborador com esse id " + id + " em nosso banco de dados."));
        return colaboradorMapper.toResponse(colaborador);
    }


    public void inativar(Long id, AuditoriaInfoDTO auditor) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Não há colaborador com esse id " + id + " em nosso banco de dados."
                ));

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
                .orElseThrow(() -> new EntityNotFoundException(
                        "Não há colaborador com esse id " + id + " em nosso banco de dados."
                ));

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
}
