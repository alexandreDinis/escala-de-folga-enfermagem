package com.oroboros.EscalaDeFolga.domain.validation.setor;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.util.TextoNormalizerUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para validar a normalização de nomes.
 */
public class NormalizacaoTest {

    @Test
    void deveNormalizarNomeComAcentos() {
        String resultado = TextoNormalizerUtil.normalizar("José da Silva");
        assertEquals("jose da silva", resultado,
                "Deve remover acentos e converter para lowercase");
    }

    @Test
    void deveNormalizarNomeComUppercase() {
        String resultado = TextoNormalizerUtil.normalizar("MARIA JOÃO");
        assertEquals("maria joao", resultado,
                "Deve converter uppercase para lowercase");
    }

    @Test
    void deveNormalizarNomeComEspacosExtras() {
        String resultado = TextoNormalizerUtil.normalizar("  UTI   Neonatal  ");
        assertEquals("uti neonatal", resultado,
                "Deve remover espaços extras e trimmar");
    }

    @Test
    void deveNormalizarNomeComCedilha() {
        String resultado = TextoNormalizerUtil.normalizar("Emergência");
        assertEquals("emergencia", resultado,
                "Deve remover cedilha");
    }

    @Test
    void deveNormalizarNomeComTodosCaracteresEspeciais() {
        String resultado = TextoNormalizerUtil.normalizar("ÓPTICA Ñ  AÇÃO");
        assertEquals("optica n acao", resultado,
                "Deve normalizar todos caracteres especiais");
    }

    @Test
    void deveRetornarVazioParaNull() {
        String resultado = TextoNormalizerUtil.normalizar(null);
        assertEquals("", resultado,
                "Deve retornar string vazia para null");
    }

    @Test
    void deveRetornarVazioParaStringVazia() {
        String resultado = TextoNormalizerUtil.normalizar("");
        assertEquals("", resultado,
                "Deve retornar string vazia");
    }

    @Test
    void deveRetornarVazioParaStringComApenasEspacos() {
        String resultado = TextoNormalizerUtil.normalizar("   ");
        assertEquals("", resultado,
                "Deve retornar string vazia para apenas espaços");
    }

    @Test
    void deveTratarNomesDiferentesComoIguaisAposNormalizacao() {
        String nome1 = TextoNormalizerUtil.normalizar("UTI");
        String nome2 = TextoNormalizerUtil.normalizar("uti");
        String nome3 = TextoNormalizerUtil.normalizar("Uti");
        String nome4 = TextoNormalizerUtil.normalizar("úti");

        assertEquals(nome1, nome2, "UTI e uti devem ser iguais");
        assertEquals(nome1, nome3, "UTI e Uti devem ser iguais");
        assertEquals(nome1, nome4, "UTI e úti devem ser iguais após normalização");
    }

    @Test
    void devePreencherNomeNormalizadoAoSalvarSetor() {
        Setor setor = new Setor();
        setor.setNome("UTI Neonatal");

        // Simula @PrePersist
        setor.setNomeNormalizado(TextoNormalizerUtil.normalizar(setor.getNome()));

        assertEquals("uti neonatal", setor.getNomeNormalizado(),
                "Nome normalizado deve ser preenchido automaticamente");
    }

    @Test
    void devePreencherNomeNormalizadoAoSalvarColaborador() {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome("José da Silva");

        // Simula @PrePersist
        colaborador.setNomeNormalizado(TextoNormalizerUtil.normalizar(colaborador.getNome()));

        assertEquals("jose da silva", colaborador.getNomeNormalizado(),
                "Nome normalizado deve ser preenchido automaticamente");
    }
}
