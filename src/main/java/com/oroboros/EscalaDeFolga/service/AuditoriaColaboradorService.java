package com.oroboros.EscalaDeFolga.service;

import com.oroboros.EscalaDeFolga.dto.colaborador.AuditoriaInfoDTO;
import com.oroboros.EscalaDeFolga.model.colaborador.AcaoAuditoriaEnum;
import com.oroboros.EscalaDeFolga.model.colaborador.AuditoriaColaborador;
import com.oroboros.EscalaDeFolga.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.repository.AuditoriaColaboradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaColaboradorService {

    @Autowired
    private AuditoriaColaboradorRepository repository;


    public void criarAuditoria(
            AcaoAuditoriaEnum acao,
            Colaborador colaborador,
            AuditoriaInfoDTO auditor,
            String dadosAnteriores,
            String dadosNovos
    ) {
        AuditoriaColaborador auditoria = AuditoriaColaborador.criar(
                acao,
                colaborador.getId(),
                auditor.usuarioId(),
                auditor.usuarioNome(),
                dadosAnteriores,
                dadosNovos,
                auditor.ipOrigem(),
                auditor.userAgent()
        );

        repository.save(auditoria);
    }
}
