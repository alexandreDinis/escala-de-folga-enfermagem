package com.oroboros.EscalaDeFolga.app.controller;

import com.oroboros.EscalaDeFolga.app.dto.folga.*;
import com.oroboros.EscalaDeFolga.app.dto.alerta.AlertaDTO;
import com.oroboros.EscalaDeFolga.app.mapper.FolgaMapper;
import com.oroboros.EscalaDeFolga.app.mapper.AlertaMapper;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.service.FolgaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller de Folgas - usa FolgaMapper e AlertaMapper
 */
@RestController
@RequestMapping("api/folga")
@RequiredArgsConstructor
@Slf4j
public class FolgaController {

    private final FolgaService folgaService;
    private final FolgaMapper folgaMapper;
    private final AlertaMapper alertaMapper;

    /**
     * POST /api/folga
     * Cria folga quando admin clica no calendário
     */
    @PostMapping
    public ResponseEntity<FolgaResponseDTO> criar(
            @Valid @RequestBody FolgaRequestDTO request
    ) {
        // ✅ Mapper: DTO → Entidade
        Folga folga = folgaMapper.toEntity(request);

        // ✅ Service: Entidade → Entidade
        Folga criada = folgaService.criarFolga(folga);

        // ✅ Service: gera alertas
        List<Alerta> alertas = folgaService.gerarAlertas(criada);

        // ✅ Service: calcula próximas datas
        FolgaService.ProximasFolgasDomain proximasDomain =
                folgaService.calcularProximasDatas(criada);

        // ✅ Mapper: Entidade → DTO base
        FolgaResponseDTO baseResponse = folgaMapper.toResponse(criada);

        // ✅ Mapper: List<Alerta> → List<AlertaDTO>
        List<AlertaDTO> alertasDTO = alertas.stream()
                .map(alertaMapper::toDTO)
                .collect(Collectors.toList());

        // ✅ Manual: Domain → DTO (ProximasFolgas é simples)
        ProximasFolgasDTO proximasFolgasDTO = new ProximasFolgasDTO(
                proximasDomain.getProximaDataDisponivel(),
                proximasDomain.getProximasCincoDatas(),
                proximasDomain.getDiasAteProximaFolga()
        );

        // ✅ Monta response final com record
        FolgaResponseDTO response = new FolgaResponseDTO(
                baseResponse.id(),
                baseResponse.escalaId(),
                baseResponse.colaboradorId(),
                baseResponse.colaboradorNome(),
                baseResponse.dataSolicitada(),
                baseResponse.diaSemana(),
                baseResponse.isDomingo(),
                baseResponse.status(),
                baseResponse.justificativa(),
                baseResponse.dataCriacao(),
                alertasDTO,
                proximasFolgasDTO
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/folga/{id}
     * Busca folga por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FolgaResponseDTO> buscarPorId(@PathVariable Long id) {
        Folga folga = folgaService.buscarPorId(id);
        FolgaResponseDTO response = folgaMapper.toResponse(folga);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/folga/{id}
     * Atualiza folga pendente
     */
    @PutMapping("/{id}")
    public ResponseEntity<FolgaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FolgaUpdateDTO request
    ) {
        Folga atualizada = folgaService.atualizar(
                id,
                request.dataSolicitada(),
                request.justificativa()
        );

        FolgaResponseDTO response = folgaMapper.toResponse(atualizada);
        return ResponseEntity.ok(response);
    }


    /**
     * POST /api/folga/historico
     * Cadastra histórico de última folga com validação
     */
    @PostMapping("/historico")
    public ResponseEntity<Void> cadastrarHistorico(
            @Valid @RequestBody HistoricoFolgaRequestDTO request
    ) {
        log.info("📝 Cadastrando histórico: colaborador={}, data={}, escala={}",
                request.colaboradorId(), request.dataSolicitada(), request.escalaId());

        try {
            folgaService.cadastrarHistorico(
                    request.colaboradorId(),
                    request.dataSolicitada(),
                    request.escalaId()  // ✅ PASSAR escalaId
            );
            log.info("✅ Histórico cadastrado com sucesso");
            return ResponseEntity.noContent().build();
        } catch (BusinessException e) {
            log.warn("❌ Erro ao cadastrar histórico: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * POST /api/folga/validar-data-ultima-folga
     * Valida se a data da última folga é permitida para a escala
     */
    @PostMapping("/validar-data-ultima-folga")
    public ResponseEntity<ValidacaoUltimaFolgaResponseDTO> validarDataUltimaFolga(
            @Valid @RequestBody ValidarDataUltimaFolgaDTO request
    ) {
        log.info("🔍 Validando data de última folga: colaborador={}, data={}, escala={}",
                request.colaboradorId(), request.dataSolicitada(), request.escalaId());

        ValidacaoUltimaFolgaResponseDTO validacao = folgaService.validarDataUltimaFolga(
                request.escalaId(),
                request.dataSolicitada()
        );

        log.info("✅ Resultado: válido={}, mensagem={}", validacao.valido(), validacao.mensagem());

        return ResponseEntity.ok(validacao);
    }

    /**
     * DELETE /api/folga/{id}
     * Remove folga pendente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        folgaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
