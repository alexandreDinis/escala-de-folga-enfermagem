package com.oroboros.EscalaDeFolga.domain.validation.escala;


import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.service.EscalaService;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Regra de negócio responsável por garantir que o sistema possua
 * histórico válido para cálculo de folgas antes de criar uma nova
 * {@link Escala}.
 *
 * <h3>📌 Regras atendidas por este validador:</h3>
 * <ul>
 *     <li>Se existir uma escala imediatamente anterior (mês anterior)
 *         para o mesmo setor e turno, o sistema usará as informações
 *         dessa escala como referência.</li>
 *     <li>Se <b>não for encontrada escalada anterior</b>:
 *         <ul>
 *             <li>O administrador deverá informar manualmente a última folga
 *                 de cada colaborador antes da criação da primeira escala.</li>
 *             <li>O sistema deve impedir a geração da escala atual até que
 *                 essa informação seja cadastrada.</li>
 *         </ul>
 *     </li>
 * </ul>
 *
 * <h3>📌 Justificativa</h3>
 * O cálculo de folgas exige conhecimento da última folga realizada
 * por cada colaborador, especialmente para validação da regra <b>6x1</b>
 * (máximo de 6 dias consecutivos de trabalho) e distribuição mensal.
 * Sem histórico, a escala pode gerar inconsistências trabalhistas.
 *
 * <h3>📌 Exemplos de bloqueio:</h3>
 * <pre>
 * - Não existe escala no mês anterior → bloqueia criação
 * - Não foram informadas as últimas folgas dos colaboradores → bloqueia
 * - Histórico incompleto ou não registrado → bloqueia
 * </pre>
 *
 * <h3>📌 Ação recomendada ao administrador:</h3>
 * <ul>
 *     <li>Cadastrar manualmente a última folga de cada colaborador
 *         antes de gerar a primeira escala no sistema.</li>
 *     <li>Ou, caso já exista uma escala anterior registrada, garantir
 *         que os dados estejam completos.</li>
 * </ul>
 *
 * @see Escala
 * @see EscalaService#criarEscala(Escala)
 * @see Colaborador#getUltimaFolga()
 */

@Component
@Order(2) // Após validação de setor, mas antes de duplicidade
@RequiredArgsConstructor
public class ValidaHistoricoEscalaAnterior implements IEscalaValidator {

    private final EscalaRepository escalaRepository;
    private final ColaboradorRepository colaboradorRepository;

    @Override
    public ResultadoValidacao validar(Escala escala) {

        // Busca escala do mês anterior no mesmo setor e turno
        Optional<Escala> escalaAnterior = escalaRepository.findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        );

        if (escalaAnterior.isEmpty()) {
            // Verifica se colaboradores possuem última folga registrada
            boolean historicoIncompleto = colaboradorRepository
                    .existsBySetorAndTurnoAndUltimaFolgaNull(escala.getSetor(), escala.getTurno());

            if (historicoIncompleto) {
                return ResultadoValidacao.erro(
                        "Não foi encontrada escala anterior no sistema e " +
                                "existem colaboradores sem histórico de última folga. " +
                                "Cadastre o histórico antes de criar a escala."
                );
            }
        }

        return ResultadoValidacao.ok();
    }
}
