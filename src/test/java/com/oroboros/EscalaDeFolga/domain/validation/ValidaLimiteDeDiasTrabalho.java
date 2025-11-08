package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Validador responsável por garantir que o colaborador não ultrapasse
 * o limite máximo de 6 dias consecutivos de trabalho sem uma folga.
 *
 * <p>Durante a criação de uma nova folga, o sistema verifica a data da última folga
 * registrada para o colaborador. Caso o intervalo entre a última folga e a nova
 * data solicitada ultrapasse 6 dias, a solicitação é rejeitada com uma mensagem
 * descritiva.</p>
 *
 * <p>Essa validação garante conformidade com normas trabalhistas e mantém
 * a integridade da escala, evitando sobrecarga de trabalho.</p>
 *
 * @author Alexandre Dinis
 * @since 2025-11
 */
@Component
@RequiredArgsConstructor
public class ValidaLimiteDeDiasTrabalho implements IFolgaValidator {

    private final FolgaRepository folgaRepository;

    /**
     * Executa a validação para impedir que o colaborador ultrapasse 6 dias consecutivos
     * de trabalho sem folgar.
     *
     * @param folga a nova solicitação de folga
     * @return {@link ResultadoValidacao} com status de sucesso ou erro
     */
    @Override
    public ResultadoValidacao validar(Folga folga) {
        Colaborador colaborador = folga.getColaborador();
        LocalDate dataSolicitada = folga.getDataSolicitada();

        // Última folga do colaborador antes da data solicitada
        Optional<LocalDate> ultimaFolga = folgaRepository.findUltimaFolgaAntesDe(
                colaborador, dataSolicitada
        );

        if (ultimaFolga.isPresent()) {
            long diasTrabalhados = ultimaFolga.get().until(dataSolicitada).getDays();

            if (diasTrabalhados > 6) {
                return ResultadoValidacao.erro(
                        "O colaborador já trabalhou " + diasTrabalhados +
                                " dias consecutivos desde a última folga (" + ultimaFolga.get() + ")."
                );
            }
        }

        return ResultadoValidacao.ok();
    }
}
