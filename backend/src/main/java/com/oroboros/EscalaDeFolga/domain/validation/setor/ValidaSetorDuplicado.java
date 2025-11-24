package com.oroboros.EscalaDeFolga.domain.validation.setor;

import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.util.TextoNormalizerUtil;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.infrastructure.repository.SetorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validador que impede cadastro de setores duplicados.
 *
 * <p>Considera duplicados setores com nomes equivalentes quando normalizados:
 * <ul>
 *   <li>"UTI" = "uti" = "Uti" = "útei"</li>
 *   <li>"Emergência" = "emergencia" = "EMERGENCIA"</li>
 * </ul>
 *
 * <p>Em caso de atualização, permite que o setor mantenha seu próprio nome.
 *
 * @author Alexandre
 */
@Component
@RequiredArgsConstructor
public class ValidaSetorDuplicado implements ISetorValidator {

    private final SetorRepository setorRepository;

    @Override
    public ResultadoValidacao validar(Setor setor) {

        String nomeNormalizado = TextoNormalizerUtil.normalizar(setor.getNome());

        // Busca setor com mesmo nome normalizado
        var existe = setorRepository.findByNomeNormalizadoAndIdNot(
                nomeNormalizado,
                setor.getId() != null ? setor.getId() : -1L
        );

        if (existe.isPresent() && existe.get().isAtivo()) {
            return ResultadoValidacao.erro(
                    String.format("Já existe um setor cadastrado com nome similar a '%s'. ",
                            setor.getNome())
            );
        }

        return ResultadoValidacao.ok();
    }
}

