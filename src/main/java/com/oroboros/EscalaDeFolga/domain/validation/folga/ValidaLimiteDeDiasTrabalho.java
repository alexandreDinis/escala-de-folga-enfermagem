package com.oroboros.EscalaDeFolga.domain.validation.folga;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.service.EscalaRegrasService;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Validador responsável por garantir que um colaborador não ultrapasse
 * o limite de 6 dias consecutivos de trabalho, considerando inclusive
 * as folgas registradas em escalas anteriores.
 *
 * <p>Durante a criação de uma nova {@link Folga}, o sistema consulta
 * a última folga registrada do colaborador, mesmo que pertença a um mês
 * anterior, e calcula o número de dias entre elas. Caso o intervalo
 * seja superior a 6 dias, a validação falha.</p>
 *
 * <h3>Critérios de validação:</h3>
 * <ul>
 *   <li>Folgas com status <b>PENDENTE</b> ou <b>APROVADA</b> são consideradas válidas.</li>
 *   <li>Se o colaborador não possui folgas anteriores, a validação é aprovada.</li>
 *   <li>Se o intervalo entre a última folga e a nova for superior a 6 dias, a validação falha.</li>
 * </ul>
 *
 * <h3>Exemplo de uso:</h3>
 * <pre>{@code
 * ResultadoValidacao resultado = validaLimiteDeDiasTrabalho.validar(folga);
 * if (!resultado.isValido()) {
 *     return ResponseEntity.badRequest().body(resultado.getMensagem());
 * }
 * }</pre>
 *
 * @author Alexandre Dinis
 * @see FolgaRepository
 * @see ResultadoValidacao
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidaLimiteDeDiasTrabalho implements IFolgaValidator {
    private final FolgaRepository folgaRepository;
    private final EscalaRegrasService escalaRegrasService;

    @Override
    public ResultadoValidacao validar(Folga folga) {
        Colaborador colaborador = folga.getColaborador();
        LocalDate novaData = folga.getDataSolicitada();

        log.debug("🟢 Iniciando validação de limite de dias trabalhados para colaborador: {}", colaborador.getNome());
        log.debug("📅 Nova data solicitada: {}", novaData);

        Optional<LocalDate> ultimaFolga = folgaRepository.findUltimaFolgaAntesDe(colaborador, novaData);

        if (ultimaFolga.isEmpty()) {
            log.info("✅ Nenhuma folga anterior encontrada. Primeira folga do colaborador: {}", colaborador.getNome());
            return ResultadoValidacao.ok();
        }

        long diasEntre = ChronoUnit.DAYS.between(ultimaFolga.get(), novaData);

        long diasTrabalhados = diasEntre - 1;

        int limiteDias = escalaRegrasService.getDiasTrabalhoPermitidos();

        log.debug("📊 Última folga registrada: {}", ultimaFolga.get());
        log.debug("📊 Intervalo entre folgas: {} dias", diasEntre);
        log.debug("📊 Dias trabalhados consecutivos: {} dias", diasTrabalhados);
        log.debug("📊 Limite permitido pela regra: {} dias consecutivos", limiteDias);

        if (diasTrabalhados >= limiteDias) {  // ← IMPORTANTE: >= não só >
            log.warn("⚠️ Colaborador {} ultrapassou o limite permitido. Trabalhou {} dias consecutivos (limite: {}).",
                    colaborador.getNome(), diasTrabalhados, limiteDias);

            return ResultadoValidacao.erro(String.format(
                    "O colaborador %s ultrapassou o limite de %s dias consecutivos de trabalho. " +
                            "Última folga registrada: %s, nova folga solicitada: %s.",
                    colaborador.getNome(),
                    limiteDias,
                    ultimaFolga.get(),
                    novaData
            ));
        }

        log.info("✅ Validação concluída: folga de {} é permitida para colaborador {}.", novaData, colaborador.getNome());

        return ResultadoValidacao.ok();
    }
}

