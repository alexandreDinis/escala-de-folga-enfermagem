package com.oroboros.EscalaDeFolga.app.controller;

import com.oroboros.EscalaDeFolga.app.dto.alerta.AvisoHistoricoResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.alerta.ColaboradorSemHistoricoDTO;
import com.oroboros.EscalaDeFolga.app.dto.calendario.CalendarioResponseDTO;
import com.oroboros.EscalaDeFolga.app.mapper.CalendarioMapper;
import com.oroboros.EscalaDeFolga.domain.service.CalendarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controller de Calendário - usa CalendarioMapper
 */

@Slf4j
@RestController
@RequestMapping("api/escala")
@RequiredArgsConstructor
public class CalendarioController {

    private final CalendarioService calendarioService;
    private final CalendarioMapper calendarioMapper;

    /**
     * GET /api/escala/{id}/calendario
     * Retorna calendário visual do mês
     */
    @GetMapping("/{id}/calendario")
    public ResponseEntity<CalendarioResponseDTO> obterCalendario(@PathVariable Long id) {

        // ✅ Service retorna Domain
        CalendarioService.CalendarioDomain calendarioDomain =
                calendarioService.gerarCalendario(id);

        // ✅ Mapper faz a conversão Domain → DTO
        CalendarioResponseDTO response = calendarioMapper.toResponse(calendarioDomain);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/escala/{id}/calendario/avisos
     * Verifica se há colaboradores sem histórico de folga
     */
    @GetMapping("/{id}/calendario/avisos")
    public ResponseEntity<AvisoHistoricoResponseDTO> verificarAvisos(@PathVariable Long id) {
        log.info("🔍 Verificando avisos de histórico para escala ID: {}", id);

        CalendarioService.AvisoHistoricoDomain aviso =
                calendarioService.verificarHistoricoColaboradores(id);

        List<ColaboradorSemHistoricoDTO> colaboradoresDTO = aviso.colaboradores().stream()
                .map(c -> new ColaboradorSemHistoricoDTO(
                        c.getId(),
                        c.getNome(),
                        c.getCargo().name(),
                        c.getTurno().name(),
                        c.getUltimaFolga()
                ))
                .toList();

        AvisoHistoricoResponseDTO response = new AvisoHistoricoResponseDTO(
                aviso.temAvisos(),
                aviso.mensagem(),
                colaboradoresDTO
        );

        if (aviso.temAvisos()) {
            log.warn("⚠️  {} colaboradores sem histórico", colaboradoresDTO.size());
        } else {
            log.info("✅ Todos os colaboradores possuem histórico");
        }

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/escala/{id}/com-colaboradores
     * Retorna escala com lista de colaboradores para o calendário
     * (compatível com o frontend que espera essa rota)
     */
    @GetMapping("/{id}/com-colaboradores")
    public ResponseEntity<CalendarioResponseDTO> buscarComColaboradores(@PathVariable Long id) {
        log.info("📅 Buscando escala com colaboradores para ID: {}", id);

        CalendarioService.CalendarioDomain calendarioDomain =
                calendarioService.gerarCalendario(id);

        CalendarioResponseDTO response = calendarioMapper.toResponse(calendarioDomain);

        return ResponseEntity.ok(response);
    }
}