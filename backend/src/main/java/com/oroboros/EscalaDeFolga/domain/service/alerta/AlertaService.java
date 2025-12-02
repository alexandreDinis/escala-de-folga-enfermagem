package com.oroboros.EscalaDeFolga.domain.service.alerta;

import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service responsável por orquestrar geração e persistência de alertas
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlertaService {

    private final List<IAlertaGenerator> geradores;
    private final AlertaRepository alertaRepository;

    /**
     * Gera todos os alertas para uma folga e os persiste
     */
    @Transactional
    public List<Alerta> gerarEPersistirAlertas(Folga folga) {
        log.info("🔔 Gerando alertas para folga: colaborador={}, data={}",
                folga.getColaborador().getNome(),
                folga.getDataSolicitada()
        );

        List<Alerta> todosAlertas = new ArrayList<>();

        // Executa cada gerador
        for (IAlertaGenerator gerador : geradores) {
            try {
                List<Alerta> alertas = gerador.gerarAlertas(folga);
                todosAlertas.addAll(alertas);

                if (!alertas.isEmpty()) {
                    log.debug("✅ {} gerou {} alerta(s)",
                            gerador.getClass().getSimpleName(),
                            alertas.size()
                    );
                }
            } catch (Exception e) {
                log.error("❌ Erro ao executar gerador {}: {}",
                        gerador.getClass().getSimpleName(),
                        e.getMessage()
                );
                // Continua com próximo gerador mesmo se um falhar
            }
        }

        // Persiste todos os alertas
        if (!todosAlertas.isEmpty()) {
            alertaRepository.saveAll(todosAlertas);
            log.info("✅ {} alerta(s) persistido(s) com sucesso", todosAlertas.size());
        } else {
            log.info("ℹ️ Nenhum alerta gerado para esta folga");
        }

        return todosAlertas;
    }

    /**
     * Retorna alertas não resolvidos de uma escala
     */
    public List<Alerta> obterAlertasEscala(Long escalaId) {
        return alertaRepository.findByEscalaIdAndResolvidoFalseOrderBySeveridadeAsc(escalaId);
    }

    /**
     * Retorna alertas de uma folga específica
     */
    public List<Alerta> obterAlertasFolga(Long folgaId) {
        return alertaRepository.findByFolgaIdOrderBySeveridadeAsc(folgaId);
    }

    /**
     * Marca alerta como lido
     */
    @Transactional
    public void marcarComoLido(Long alertaId) {
        alertaRepository.findById(alertaId).ifPresent(alerta -> {
            alerta.marcarComoLido();
            alertaRepository.save(alerta);
            log.info("✅ Alerta {} marcado como lido", alertaId);
        });
    }

    /**
     * Marca alerta como resolvido
     */
    @Transactional
    public void marcarComoResolvido(Long alertaId) {
        alertaRepository.findById(alertaId).ifPresent(alerta -> {
            alerta.marcarComoResolvido();
            alertaRepository.save(alerta);
            log.info("✅ Alerta {} marcado como resolvido", alertaId);
        });
    }

    /**
     * Conta alertas críticos pendentes
     */
    public long contarAlertasCriticos(Long escalaId) {
        return alertaRepository.countAlertasCriticosPendentes(escalaId);
    }
}