package com.oroboros.EscalaDeFolga.domain.validation.folga;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Validador responsável por garantir que cada colaborador tenha pelo menos
 * uma folga em um domingo dentro da escala mensal.
 *
 * <p>Essa regra verifica se o colaborador já teve uma folga de domingo no mês
 * atual. Caso o colaborador ainda não tenha folgado em um domingo e possua
 * apenas uma folga restante para atingir o limite permitido na {@link Escala},
 * o sistema retorna um erro de validação, impedindo a criação de novas folgas
 * até que essa condição seja atendida.</p>
 *
 * <p>A verificação é feita de forma dinâmica, consultando o {@link FolgaRepository}
 * para identificar se há registros de folga em domingos e quantas folgas já foram
 * concedidas (pendentes ou aprovadas). Essa abordagem evita duplicação de dados
 * e garante consistência independente do banco de dados utilizado.</p>
 *
 * <p>Essa validação é executada automaticamente como parte do fluxo de
 * {@link FolgaValidatorComposite}, que agrega todas as regras de negócio
 * relacionadas às folgas.</p>
 *
 * <h3>Exemplo de cenário</h3>
 * <pre>{@code
 * ResultadoValidacao resultado = validaDomingoObrigatorio.validar(folga);
 * if (!resultado.isValido()) {
 *     throw new RegraNegocioException(resultado.getMensagem());
 * }
 * }</pre>
 *
 * <h3>Regra aplicada:</h3>
 * <ul>
 *   <li>Se o colaborador ainda <b>não folgou em um domingo</b> e</li>
 *   <li>Falta apenas <b>uma folga</b> para atingir o limite mensal,</li>
 *   <li>Então a solicitação é rejeitada com uma mensagem informativa.</li>
 * </ul>
 *
 * <h3>Banco de dados suportados:</h3>
 * <p>O método utiliza expressões portáveis via {@code FUNCTION()} do JPA,
 * o que o torna compatível com os dialetos do MySQL, PostgreSQL, H2 e Oracle.</p>
 *
 * @author Alexandre Dinis
 * @since 2025-11
 * @see IFolgaValidator
 * @see FolgaValidatorComposite
 * @see ValidaDuplicidadeDeFolga
 * @see ValidaLimiteDeFolgas
 */
@Component
@RequiredArgsConstructor
public class ValidaDomingoObrigatorio implements IFolgaValidator {

    private final FolgaRepository folgaRepository;

    /**
     * Executa a validação de obrigatoriedade de folga aos domingos.
     *
     * <p>Verifica o total de folgas já concedidas para o colaborador e
     * identifica se existe alguma folga registrada em um domingo
     * dentro da {@link Escala} atual. Caso falte apenas uma folga e
     * o colaborador ainda não tenha folgado em um domingo, o método
     * retorna uma instância de {@link ResultadoValidacao} com status
     * inválido e mensagem explicativa.</p>
     *
     * @param folga objeto {@link Folga} contendo os dados da solicitação
     * @return {@link ResultadoValidacao} representando o sucesso ou falha da validação
     */
    @Override
    public ResultadoValidacao validar(Folga folga) {

        Escala escala = folga.getEscala();
        Colaborador colaborador = folga.getColaborador();

        long totalFolgas = folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                colaborador,
                escala,
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)
        );

        boolean temDomingo = folgaRepository.existsFolgaDomingoNoMes(
                colaborador, escala.getMes(), escala.getAno()
        );

        if (!temDomingo && totalFolgas + 1 >= escala.getFolgasPermitidas()) {
            return ResultadoValidacao.erro(
                    "Faltam apenas uma folga e o colaborador ainda não folgou em um domingo."
            );
        }

        return ResultadoValidacao.ok();
    }
}
