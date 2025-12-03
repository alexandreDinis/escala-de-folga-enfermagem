package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.app.dto.colaborador.AuditoriaInfoDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorUpdateDTO;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.AcaoAuditoriaEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.infrastructure.repository.AuditoriaColaboradorRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import com.oroboros.EscalaDeFolga.app.mapper.ColaboradorMapper;
import com.oroboros.EscalaDeFolga.util.JsonUtil;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service

public class ColaboradorService {

    private final ColaboradorRepository colaboradorRepository;
    private final AuditoriaColaboradorRepository auditoriaRepository;
    private final AuditoriaColaboradorService auditoriaService;
    private final ColaboradorMapper colaboradorMapper;
    private final FolgaService folgaService;

    public ColaboradorService(
            ColaboradorRepository colaboradorRepository,
            AuditoriaColaboradorRepository auditoriaRepository,
            AuditoriaColaboradorService auditoriaService,
            ColaboradorMapper colaboradorMapper,
            @Lazy FolgaService folgaService
    ) {
        this.colaboradorRepository = colaboradorRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.auditoriaService = auditoriaService;
        this.colaboradorMapper = colaboradorMapper;
        this.folgaService = folgaService;
    }


    public ColaboradorResponseDTO cadastrar(ColaboradorRequestDTO colaboradorDTO, AuditoriaInfoDTO auditor) {

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


    public Page<ColaboradorResponseDTO> listar(
            Pageable pageable,
            String search,
            Long setorId,
            TurnoEnum turno,
            CargoEnum cargo
    ) {
        // Se não tem filtros, busca todos ativos
        if ((search == null || search.isBlank()) && setorId == null && turno == null && cargo == null) {
            return colaboradorRepository.findByAtivoTrue(pageable).map(colaboradorMapper::toResponse);
        }

        // Se tem filtros, usa query customizada
        return colaboradorRepository.buscarComFiltros(search, setorId, turno, cargo, pageable)
                .map(colaboradorMapper::toResponse);
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
     * Atualiza a data da última folga de um colaborador
     * Usado para cadastrar histórico de folgas antigas
     *
     * @param colaboradorId ID do colaborador
     * @param dataUltimaFolga Data da última folga
     */
    /**
     * Atualiza a data da última folga de um colaborador
     * e cria registro de folga histórica
     *
     * @param colaboradorId ID do colaborador
     * @param dataUltimaFolga Data da última folga
     */
    @Transactional
    public void atualizarUltimaFolga(Long colaboradorId, LocalDate dataUltimaFolga) {
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                .orElseThrow(() -> new BusinessException("Colaborador", colaboradorId));

        // Atualiza campo ultima_folga
        colaborador.setUltimaFolga(dataUltimaFolga);
        colaboradorRepository.save(colaborador);

        // ✅ Usa FolgaService para criar registro histórico
        folgaService.criarFolgaHistorica(colaborador, dataUltimaFolga);
    }




    /**
     * Busca colaboradores sem histórico de última folga em um setor e turno específicos
     *
     * @param setor Setor a verificar
     * @param turno Turno a verificar
     * @return Lista de colaboradores sem histórico
     */
    public List<Colaborador> buscarColaboradoresSemHistorico(Setor setor, TurnoEnum turno) {
        return colaboradorRepository.findBySetorAndTurnoAndUltimaFolgaNull(setor, turno);
    }

    /**
     * Conta total de colaboradores ativos em um setor e turno
     *
     * @param setor Setor a verificar
     * @param turno Turno a verificar
     * @return Total de colaboradores
     */
    public long contarColaboradores(Setor setor, TurnoEnum turno) {
        return colaboradorRepository.countBySetorAndTurnoAndAtivoTrue(setor, turno);
    }
}
