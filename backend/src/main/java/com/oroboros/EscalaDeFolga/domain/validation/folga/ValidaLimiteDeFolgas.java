package com.oroboros.EscalaDeFolga.domain.validation.folga;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Validador responsável por garantir que um colaborador não exceda o número
 * máximo de folgas permitidas em uma {@link Escala}.
 *
 * <p>Durante a criação de uma nova {@link Folga}, este validador consulta o
 * repositório de folgas e conta quantas solicitações o colaborador já possui
 * na escala atual, considerando apenas aquelas com status
 * {@link StatusFolgaEnum#PENDENTE} e {@link StatusFolgaEnum#APROVADA}.</p>
 *
 * <p>Se o número total de folgas registradas atingir o limite definido
 * em {@link Escala#getFolgasPermitidas()}, o sistema bloqueia a criação
 * da nova folga e retorna uma mensagem explicativa.</p>
 *
 * <h3>Critérios de validação:</h3>
 * <ul>
 *   <li>São consideradas apenas folgas com status <b>PENDENTE</b> ou <b>APROVADA</b>.</li>
 *   <li>O limite de folgas mensais é definido pela escala através de {@link Escala#getFolgasPermitidas()}.</li>
 *   <li>Se o número de folgas já registradas for maior ou igual ao limite, a nova folga será rejeitada.</li>
 * </ul>
 *
 * <h3>Exemplo de uso</h3>
 * <pre>{@code
 * ResultadoValidacao resultado = validaLimiteDeFolgas.validar(folga);
 * if (!resultado.isValido()) {
 *     return ResponseEntity.badRequest().body(resultado.getMensagem());
 * }
 * }</pre>
 *
 * @author
 *  Alexandre Dinis
 * @see IFolgaValidator
 * @see FolgaRepository
 * @see Escala
 */
@Component
@RequiredArgsConstructor
public class ValidaLimiteDeFolgas implements IFolgaValidator {

    private final FolgaRepository folgaRepository;

    @Override
    public ResultadoValidacao validar(Folga folga) {
        Escala escala = folga.getEscala();
        Colaborador colaborador = folga.getColaborador();

        // Conta quantas folgas o colaborador já tem na escala atual
        long totalFolgas = folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                colaborador,
                escala,
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)
        );

        if (totalFolgas >= escala.getFolgasPermitidas()) {
            return ResultadoValidacao.erro(String.format(
                    "O colaborador %s já atingiu o limite de %d folgas permitidas na escala de %02d/%d.",
                    colaborador.getNome(),
                    escala.getFolgasPermitidas(),
                    escala.getMes(),
                    escala.getAno()
            ));
        }

        return ResultadoValidacao.ok();
    }
}

