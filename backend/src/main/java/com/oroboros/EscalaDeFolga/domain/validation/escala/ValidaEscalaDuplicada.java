package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Validador responsável por impedir a criação de escalas duplicadas.
 *
 * <p>Uma escala é considerada duplicada quando existe outra escala
 * cadastrada com o mesmo <b>mês</b>, <b>ano</b>, <b>turno</b> e <b>setor</b>,
 * desde que ainda esteja com o status {@code NOVA}.
 *
 * <p>Essa validação é executada durante a criação de uma nova escala.
 * Se uma escala idêntica já estiver aberta, a criação é bloqueada e uma
 * mensagem de erro informativa é retornada.
 *
 * <h3>Regras Validadas</h3>
 * <ul>
 *     <li>Não é permitido criar uma nova escala se outra escala está aberta
 *     para o mesmo setor, turno e período (mes/ano).</li>
 *     <li>A verificação é feita apenas em escalas com {@link StatusEscalaEnum#NOVA}.</li>
 * </ul>
 *
 * <h3>Exemplo de mensagem gerada</h3>
 * <pre>
 * "Já existe uma escala aberta para 5/2025, turno NOITE, setor UTI."
 * </pre>
 *
 * @author Alexandre
 */

@Component
@RequiredArgsConstructor
@Order(3)
public class ValidaEscalaDuplicada implements IEscalaValidator {

    private final EscalaRepository escalaRepository;

    @Override
    public ResultadoValidacao validar(Escala escala) {

        boolean existe = escalaRepository.existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        );

        if (existe) {
            return ResultadoValidacao.erro(
                    String.format("Já existe uma escala aberta para %d/%d, turno %s, setor %s.",
                            escala.getMes(),
                            escala.getAno(),
                            escala.getTurno().name(),
                            escala.getSetor().getNome()
                    )
            );
        }

        return ResultadoValidacao.ok();
    }
}
