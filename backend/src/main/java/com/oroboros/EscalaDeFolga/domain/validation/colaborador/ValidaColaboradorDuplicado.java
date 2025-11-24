package com.oroboros.EscalaDeFolga.domain.validation.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.util.TextoNormalizerUtil;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validador que impede cadastro de colaboradores duplicados no mesmo setor/turno.
 *
 * <p>Considera duplicados colaboradores com:
 * <ul>
 *   <li>Mesmo nome normalizado (ignora acentos e case)</li>
 *   <li>Mesmo setor</li>
 *   <li>Mesmo turno</li>
 * </ul>
 *
 * <p>Exemplos de duplicatas detectadas:
 * <ul>
 *   <li>"José Silva" e "Jose Silva" no mesmo setor/turno</li>
 *   <li>"MARIA JOÃO" e "Maria João" no mesmo setor/turno</li>
 * </ul>
 *
 * <p>Permite homônimos em setores ou turnos diferentes.
 *
 * @author Alexandre
 */
@Component
@RequiredArgsConstructor
public class ValidaColaboradorDuplicado implements IColaboradorValidator {

    private final ColaboradorRepository colaboradorRepository;

    @Override
    public ResultadoValidacao validar(Colaborador colaborador) {

        // Validações preliminares
        if (colaborador.getNome() == null || colaborador.getNome().isBlank()) {
            return ResultadoValidacao.erro("Nome do colaborador é obrigatório.");
        }

        if (colaborador.getSetor() == null) {
            return ResultadoValidacao.erro("Setor é obrigatório.");
        }

        if (colaborador.getTurno() == null) {
            return ResultadoValidacao.erro("Turno é obrigatório.");
        }

        String nomeNormalizado = TextoNormalizerUtil.normalizar(colaborador.getNome());

        // Busca colaborador com mesmo nome normalizado, setor e turno
        boolean existe = colaboradorRepository.existsByNomeNormalizadoAndSetorAndTurnoAndIdNot(
                nomeNormalizado,
                colaborador.getSetor(),
                colaborador.getTurno(),
                colaborador.getId() != null ? colaborador.getId() : -1L
        );

        if (existe) {
            return ResultadoValidacao.erro(
                    String.format("Já existe um colaborador cadastrado com nome similar a '%s' " +
                                    "no setor %s, turno %s. Verifique se não é uma duplicata.",
                            colaborador.getNome(),
                            colaborador.getSetor().getNome(),
                            colaborador.getTurno().name())
            );
        }

        return ResultadoValidacao.ok();
    }
}
