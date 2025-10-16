package com.oroboros.EscalaDeFolga.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oroboros.EscalaDeFolga.dto.AuditoriaInfoDTO;
import com.oroboros.EscalaDeFolga.dto.ColaboradorInputDTO;
import com.oroboros.EscalaDeFolga.dto.ColaboradorResponseDTO;
import com.oroboros.EscalaDeFolga.dto.ColaboradorUpdateDTO;
import com.oroboros.EscalaDeFolga.model.AcaoAuditoriaEnum;
import com.oroboros.EscalaDeFolga.model.Colaborador;
import com.oroboros.EscalaDeFolga.repository.AuditoriaColaboradorRepository;
import com.oroboros.EscalaDeFolga.repository.ColaboradorRepository;
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


    public ColaboradorResponseDTO cadastrar(ColaboradorInputDTO colaboradorDTO, AuditoriaInfoDTO auditor) throws JsonProcessingException {

        Colaborador colaborador = new Colaborador(colaboradorDTO);
        colaboradorRepository.save(colaborador);

        //todo:dados ficticios aguardando a implementação da segurança e usuarios.

        auditoriaService.criarAuditoria(
                AcaoAuditoriaEnum.CRIACAO,
                colaborador,
                auditor,
                null,
                JsonUtil.toJson(colaborador)
        );
        return new ColaboradorResponseDTO(colaborador);
    }


    public Page<ColaboradorResponseDTO> listar(Pageable pageable) {
        return colaboradorRepository.findAll(pageable).map(ColaboradorResponseDTO::new);
    }


    public ColaboradorResponseDTO buscarPorId(Long id) {
        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Não há colaborador com esse id " + id + " em nosso banco de dados."));
        return new ColaboradorResponseDTO(colaborador);
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

        return new ColaboradorResponseDTO(colaborador);
    }
}
