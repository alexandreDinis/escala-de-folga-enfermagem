package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EscalaValidatorCompositeTest {

    @Mock
    private IEscalaValidator validador1;

    @Mock
    private IEscalaValidator validador2;

    @Mock
    private IEscalaValidator validador3;

    private EscalaValidatorComposite composite;
    private Escala escala;

    @BeforeEach
    void setUp() {
        escala = new Escala();
        escala.setId(1L);
        escala.setMes(5);
        escala.setAno(2025);
        escala.setStatus(StatusEscalaEnum.NOVA);
        escala.setFolgas(new ArrayList<>());
        escala.setRegistros(new ArrayList<>());
    }

    /**
     * Caso 1: Todos os validadores retornam OK.
     * Esperado: composite deve retornar OK.
     */
    @Test
    void deveRetornarOkQuandoTodosValidadoresPassam() {
        List<IEscalaValidator> validators = Arrays.asList(validador1, validador2, validador3);
        composite = new EscalaValidatorComposite(validators);

        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador2.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador3.validar(escala)).thenReturn(ResultadoValidacao.ok());

        var resultado = composite.validar(escala);

        assertTrue(resultado.isValido(),
                "Composite deve retornar OK quando todos os validadores passam");
        verify(validador1, times(1)).validar(escala);
        verify(validador2, times(1)).validar(escala);
        verify(validador3, times(1)).validar(escala);
    }

    /**
     * Caso 2: Primeiro validador falha.
     * Esperado: composite deve retornar erro e não executar os demais.
     */
    @Test
    void deveRetornarErroDoPrimeiroValidadorQuandoFalha() {
        List<IEscalaValidator> validators = Arrays.asList(validador1, validador2, validador3);
        composite = new EscalaValidatorComposite(validators);

        String mensagemErro = "Erro no validador 1";
        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.erro(mensagemErro));

        var resultado = composite.validar(escala);

        assertFalse(resultado.isValido(),
                "Composite deve retornar erro quando primeiro validador falha");
        assertEquals(mensagemErro, resultado.getMensagem(),
                "Mensagem deve ser do primeiro validador que falhou");
        verify(validador1, times(1)).validar(escala);
        verify(validador2, never()).validar(escala);
        verify(validador3, never()).validar(escala);
    }

    /**
     * Caso 3: Segundo validador falha.
     * Esperado: composite deve executar primeiro, parar no segundo, não executar terceiro.
     */
    @Test
    void deveRetornarErroDoSegundoValidadorQuandoFalha() {
        List<IEscalaValidator> validators = Arrays.asList(validador1, validador2, validador3);
        composite = new EscalaValidatorComposite(validators);

        String mensagemErro = "Erro no validador 2";
        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador2.validar(escala)).thenReturn(ResultadoValidacao.erro(mensagemErro));

        var resultado = composite.validar(escala);

        assertFalse(resultado.isValido(),
                "Composite deve retornar erro quando segundo validador falha");
        assertEquals(mensagemErro, resultado.getMensagem(),
                "Mensagem deve ser do segundo validador que falhou");
        verify(validador1, times(1)).validar(escala);
        verify(validador2, times(1)).validar(escala);
        verify(validador3, never()).validar(escala);
    }

    /**
     * Caso 4: Último validador falha.
     * Esperado: todos são executados até o último que retorna erro.
     */
    @Test
    void deveRetornarErroDoUltimoValidadorQuandoFalha() {
        List<IEscalaValidator> validators = Arrays.asList(validador1, validador2, validador3);
        composite = new EscalaValidatorComposite(validators);

        String mensagemErro = "Erro no validador 3";
        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador2.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador3.validar(escala)).thenReturn(ResultadoValidacao.erro(mensagemErro));

        var resultado = composite.validar(escala);

        assertFalse(resultado.isValido(),
                "Composite deve retornar erro quando último validador falha");
        assertEquals(mensagemErro, resultado.getMensagem(),
                "Mensagem deve ser do último validador que falhou");
        verify(validador1, times(1)).validar(escala);
        verify(validador2, times(1)).validar(escala);
        verify(validador3, times(1)).validar(escala);
    }

    /**
     * Caso 5: Lista de validadores vazia.
     * Esperado: deve retornar OK (nenhuma regra para validar).
     */
    @Test
    void deveRetornarOkQuandoListaDeValidadoresVazia() {
        List<IEscalaValidator> validators = new ArrayList<>();
        composite = new EscalaValidatorComposite(validators);

        var resultado = composite.validar(escala);

        assertTrue(resultado.isValido(),
                "Composite deve retornar OK quando não há validadores");
    }

    /**
     * Caso 6: Apenas um validador que passa.
     * Esperado: deve retornar OK.
     */
    @Test
    void deveRetornarOkComApenasUmValidadorQuePassa() {
        List<IEscalaValidator> validators = Arrays.asList(validador1);
        composite = new EscalaValidatorComposite(validators);

        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.ok());

        var resultado = composite.validar(escala);

        assertTrue(resultado.isValido(),
                "Composite deve retornar OK com um único validador que passa");
        verify(validador1, times(1)).validar(escala);
    }

    /**
     * Caso 7: Apenas um validador que falha.
     * Esperado: deve retornar erro.
     */
    @Test
    void deveRetornarErroComApenasUmValidadorQueFalha() {
        List<IEscalaValidator> validators = Arrays.asList(validador1);
        composite = new EscalaValidatorComposite(validators);

        String mensagemErro = "Validação falhou";
        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.erro(mensagemErro));

        var resultado = composite.validar(escala);

        assertFalse(resultado.isValido(),
                "Composite deve retornar erro com um único validador que falha");
        assertEquals(mensagemErro, resultado.getMensagem());
        verify(validador1, times(1)).validar(escala);
    }

    /**
     * Caso 8: Múltiplos validadores, mas apenas os dois primeiros passam.
     * Terceiro falha.
     * Esperado: executa 1 e 2, para no 3 com erro.
     */
    @Test
    void deveExecutarNaOrdemAteEncontrarErro() {
        List<IEscalaValidator> validators = Arrays.asList(validador1, validador2, validador3);
        composite = new EscalaValidatorComposite(validators);

        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador2.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador3.validar(escala)).thenReturn(
                ResultadoValidacao.erro("Terceiro validador falhou")
        );

        var resultado = composite.validar(escala);

        assertFalse(resultado.isValido());
        assertTrue(resultado.getMensagem().contains("Terceiro validador falhou"));
        verify(validador1, times(1)).validar(escala);
        verify(validador2, times(1)).validar(escala);
        verify(validador3, times(1)).validar(escala);
    }

    /**
     * Caso 9: Validadores são chamados com a mesma instância de escala.
     * Esperado: todos devem receber o mesmo objeto.
     */
    @Test
    void devePassarMesmaInstanciaDeEscalaParaTodos() {
        List<IEscalaValidator> validators = Arrays.asList(validador1, validador2);
        composite = new EscalaValidatorComposite(validators);

        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.ok());
        when(validador2.validar(escala)).thenReturn(ResultadoValidacao.ok());

        composite.validar(escala);

        verify(validador1).validar(same(escala));
        verify(validador2).validar(same(escala));
    }

    /**
     * Caso 10: Teste de integração com validadores reais.
     * Simula comportamento real de ValidaEscalaEditavel e ValidaExclusaoPossivel.
     */
    @Test
    void deveValidarComValidadoresReais() {
        ValidaEscalaEditavel validaEditavel = new ValidaEscalaEditavel();
        ValidaExclusaoPossivel validaExclusao = new ValidaExclusaoPossivel();

        List<IEscalaValidator> validators = Arrays.asList(validaEditavel, validaExclusao);
        composite = new EscalaValidatorComposite(validators);

        // Escala NOVA sem dependências - deve passar em ambos
        var resultado = composite.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala NOVA sem dependências deve passar em todos os validadores");
    }

    /**
     * Caso 11: Teste de integração - escala PUBLICADA.
     * Ambos validadores devem bloquear.
     * Esperado: primeiro erro é retornado (de ValidaEscalaEditavel).
     */
    @Test
    void deveRetornarPrimeiroErroEmCadeiaDeValidadoresReais() {
        ValidaEscalaEditavel validaEditavel = new ValidaEscalaEditavel();
        ValidaExclusaoPossivel validaExclusao = new ValidaExclusaoPossivel();

        List<IEscalaValidator> validators = Arrays.asList(validaEditavel, validaExclusao);
        composite = new EscalaValidatorComposite(validators);

        escala.setStatus(StatusEscalaEnum.PUBLICADA);

        var resultado = composite.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala PUBLICADA deve falhar no primeiro validador");
        assertTrue(resultado.getMensagem().contains("PUBLICADA") ||
                        resultado.getMensagem().contains("modificada"),
                "Mensagem deve vir do ValidaEscalaEditavel");
    }

    /**
     * Caso 12: Validadores em ordem diferente produzem resultados diferentes.
     * Demonstra que a ordem importa quando múltiplos podem falhar.
     */
    @Test
    void deveRespeitarOrdemDosValidadores() {
        List<IEscalaValidator> validators1 = Arrays.asList(validador1, validador2);
        EscalaValidatorComposite composite1 = new EscalaValidatorComposite(validators1);

        List<IEscalaValidator> validators2 = Arrays.asList(validador2, validador1);
        EscalaValidatorComposite composite2 = new EscalaValidatorComposite(validators2);

        when(validador1.validar(escala)).thenReturn(ResultadoValidacao.erro("Erro 1"));
        when(validador2.validar(escala)).thenReturn(ResultadoValidacao.erro("Erro 2"));

        var resultado1 = composite1.validar(escala);
        var resultado2 = composite2.validar(escala);

        assertEquals("Erro 1", resultado1.getMensagem(),
                "Primeiro composite deve retornar erro do validador1");
        assertEquals("Erro 2", resultado2.getMensagem(),
                "Segundo composite deve retornar erro do validador2");
    }
}
