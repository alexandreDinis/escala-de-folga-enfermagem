package com.oroboros.EscalaDeFolga.domain.validation.folga;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
        * Classe responsável por coordenar e executar todas as validações aplicáveis
 * sobre uma solicitação de {@link Folga}.
        *
        * <p>O {@code FolgaValidatorComposite} atua como um orquestrador que agrega
 * todas as implementações de {@link IFolgaValidator} registradas no contexto
 * do Spring e as executa de forma sequencial.</p>
        *
        * <p>Durante o processo de validação, o método {@link #validar(Folga)} percorre
 * cada validador e interrompe a execução assim que encontra uma falha, retornando
 * um {@link ResultadoValidacao} com a mensagem específica da regra violada.
 * Caso todas as validações sejam aprovadas, o método retorna um
 * {@link ResultadoValidacao#ok()} indicando que a solicitação de folga é válida.</p>
        *
        * <p>Esse padrão facilita a extensão de novas regras de negócio relacionadas a folgas,
        * mantendo o código desacoplado, testável e de fácil manutenção.</p>
        *
        * <h3>Exemplo de uso</h3>
        * <pre>{@code
 * ResultadoValidacao resultado = folgaValidatorComposite.validar(folga);
 * if (!resultado.isValido()) {
 *     return ResponseEntity.badRequest().body(resultado.getMensagem());
 * }
 * }</pre>
        *
        * @author Alexandre Dinis
 * @see IFolgaValidator
 * @see ResultadoValidacao
 * @see ValidaDuplicidadeDeFolga
 * @see ValidaLimiteDeFolgas
 */
@Component
@RequiredArgsConstructor
public class FolgaValidatorComposite {


    private final List<IFolgaValidator> validadores;

    public ResultadoValidacao validar(Folga folga) {
        for (IFolgaValidator validador : validadores) {
            ResultadoValidacao resultado = validador.validar(folga);
            if (!resultado.isValido()) {
                return resultado; // para na primeira falha
            }
        }
        return ResultadoValidacao.ok();
    }
}