package com.oroboros.EscalaDeFolga.domain.validation.normalizacao;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes para validar a normalização de nomes.
 */
public class NormalizacaoTest {

    @Test
    void deveNormalizarNomeComAcentos() {
        String resultado = Setor.normalizarTexto("José da Silva");
        assertEquals("jose da silva", resultado,
                "Deve remover acentos e converter para lowercase");
    }

    @Test
    void deveNormalizarNomeComUppercase() {
        String resultado = Setor.normalizarTexto("MARIA JOÃO");
        assertEquals("maria joao", resultado,
                "Deve converter uppercase para lowercase");
    }

    @Test
    void deveNormalizarNomeComEspacosExtras() {
        String resultado = Setor.normalizarTexto("  UTI   Neonatal  ");
        assertEquals("uti neonatal", resultado,
                "Deve remover espaços extras e trimmar");
    }

    @Test
    void deveNormalizarNomeComCedilha() {
        String resultado = Setor.normalizarTexto("Emergência");
        assertEquals("emergencia", resultado,
                "Deve remover cedilha");
    }

    @Test
    void deveNormalizarNomeComTodosCaracteresEspeciais() {
        String resultado = Setor.normalizarTexto("ÓPTICA Ñ  AÇÃO");
        assertEquals("optica n acao", resultado,
                "Deve normalizar todos caracteres especiais");
    }

    @Test
    void deveRetornarVazioParaNull() {
        String resultado = Setor.normalizarTexto(null);
        assertEquals("", resultado,
                "Deve retornar string vazia para null");
    }

    @Test
    void deveRetornarVazioParaStringVazia() {
        String resultado = Setor.normalizarTexto("");
        assertEquals("", resultado,
                "Deve retornar string vazia");
    }

    @Test
    void deveRetornarVazioParaStringComApenasEspacos() {
        String resultado = Setor.normalizarTexto("   ");
        assertEquals("", resultado,
                "Deve retornar string vazia para apenas espaços");
    }

    @Test
    void deveTratarNomesDiferentesComoIguaisAposNormalizacao() {
        String nome1 = Setor.normalizarTexto("UTI");
        String nome2 = Setor.normalizarTexto("uti");
        String nome3 = Setor.normalizarTexto("Uti");
        String nome4 = Setor.normalizarTexto("úti");

        assertEquals(nome1, nome2, "UTI e uti devem ser iguais");
        assertEquals(nome1, nome3, "UTI e Uti devem ser iguais");
        assertEquals(nome1, nome4, "UTI e úti devem ser iguais após normalização");
    }

    @Test
    void devePreencherNomeNormalizadoAoSalvarSetor() {
        Setor setor = new Setor();
        setor.setNome("UTI Neonatal");

        // Simula @PrePersist
        setor.setNomeNormalizado(Setor.normalizarTexto(setor.getNome()));

        assertEquals("uti neonatal", setor.getNomeNormalizado(),
                "Nome normalizado deve ser preenchido automaticamente");
    }

    @Test
    void devePreencherNomeNormalizadoAoSalvarColaborador() {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome("José da Silva");

        // Simula @PrePersist
        colaborador.setNomeNormalizado(Colaborador.normalizarTexto(colaborador.getNome()));

        assertEquals("jose da silva", colaborador.getNomeNormalizado(),
                "Nome normalizado deve ser preenchido automaticamente");
    }
}
