package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.EscalaColaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidaExclusaoPossivelTest {

    @InjectMocks
    private ValidaExclusaoPossivel validaExclusaoPossivel;

    private Escala escala;
    private Colaborador colaborador;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("Maria Santos");

        escala = new Escala();
        escala.setId(1L);
        escala.setMes(6);
        escala.setAno(2025);
        escala.setFolgasPermitidas(4);
        escala.setFolgas(new ArrayList<>());
        escala.setRegistros(new ArrayList<>());
    }

    /**
     * Caso 1: Escala com status NOVA sem folgas nem registros deve permitir exclusão.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirExclusaoQuandoEscalaEstaNovaSemDependencias() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        var resultado = validaExclusaoPossivel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala NOVA sem folgas ou registros deve poder ser excluída");
    }

    /**
     * Caso 2: Escala com status PARCIAL sem folgas aprovadas nem registros
     * deve permitir exclusão.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirExclusaoQuandoEscalaEstaParcialSemDependencias() {
        escala.setStatus(StatusEscalaEnum.PARCIAL);

        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 5), StatusFolgaEnum.PENDENTE);
        Folga folga2 = criarFolga(LocalDate.of(2025, 6, 12), StatusFolgaEnum.NEGADA);
        escala.setFolgas(Arrays.asList(folga1, folga2));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala PARCIAL com apenas folgas pendentes/rejeitadas deve poder ser excluída");
    }

    /**
     * Caso 3: Escala com status PUBLICADA não deve permitir exclusão.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearExclusaoQuandoEscalaEstaPublicada() {
        escala.setStatus(StatusEscalaEnum.PUBLICADA);

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala PUBLICADA não deve poder ser excluída");
        assertTrue(resultado.getMensagem().contains("publicada"),
                "Mensagem deve mencionar que escala está publicada");
    }

    /**
     * Caso 4: Escala com status FECHADA não deve permitir exclusão.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearExclusaoQuandoEscalaEstaFechada() {
        escala.setStatus(StatusEscalaEnum.FECHADA);

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala FECHADA não deve poder ser excluída");
        assertTrue(resultado.getMensagem().contains("fechada"),
                "Mensagem deve mencionar que escala está fechada");
    }

    /**
     * Caso 5: Escala NOVA com uma folga aprovada não deve permitir exclusão.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearExclusaoQuandoExisteFolgaAprovada() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 5), StatusFolgaEnum.PENDENTE);
        Folga folga2 = criarFolga(LocalDate.of(2025, 6, 12), StatusFolgaEnum.APROVADA);
        escala.setFolgas(Arrays.asList(folga1, folga2));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala com folga aprovada não deve poder ser excluída");
        assertTrue(resultado.getMensagem().contains("folgas aprovadas"),
                "Mensagem deve mencionar folgas aprovadas");
    }

    /**
     * Caso 6: Escala NOVA com múltiplas folgas aprovadas não deve permitir exclusão.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearExclusaoComMultiplasFolgasAprovadas() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 5), StatusFolgaEnum.APROVADA);
        Folga folga2 = criarFolga(LocalDate.of(2025, 6, 12), StatusFolgaEnum.APROVADA);
        Folga folga3 = criarFolga(LocalDate.of(2025, 6, 19), StatusFolgaEnum.APROVADA);
        escala.setFolgas(Arrays.asList(folga1, folga2, folga3));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala com múltiplas folgas aprovadas não deve poder ser excluída");
    }

    /**
     * Caso 7: Escala NOVA com registros de trabalho não deve permitir exclusão.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearExclusaoQuandoExistemRegistrosDeTrabalho() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        EscalaColaborador registro1 = criarRegistro(LocalDate.of(2025, 6, 5));
        escala.setRegistros(Arrays.asList(registro1));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala com registros de trabalho não deve poder ser excluída");
        assertTrue(resultado.getMensagem().contains("registros de trabalho"),
                "Mensagem deve mencionar registros de trabalho");
    }

    /**
     * Caso 8: Escala NOVA com múltiplos registros não deve permitir exclusão.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearExclusaoComMultiplosRegistros() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        EscalaColaborador registro1 = criarRegistro(LocalDate.of(2025, 6, 5));
        EscalaColaborador registro2 = criarRegistro(LocalDate.of(2025, 6, 6));
        EscalaColaborador registro3 = criarRegistro(LocalDate.of(2025, 6, 7));
        escala.setRegistros(Arrays.asList(registro1, registro2, registro3));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala com múltiplos registros não deve poder ser excluída");
    }

    /**
     * Caso 9: Escala PUBLICADA com folgas aprovadas e registros não deve permitir
     * exclusão (múltiplos bloqueios).
     * Esperado: validação BLOQUEADA (primeiro pelo status).
     */
    @Test
    void deveBloquearExclusaoPorStatusAntesDeFolgasERegistros() {
        escala.setStatus(StatusEscalaEnum.PUBLICADA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 5), StatusFolgaEnum.APROVADA);
        escala.setFolgas(Arrays.asList(folga1));

        EscalaColaborador registro1 = criarRegistro(LocalDate.of(2025, 6, 5));
        escala.setRegistros(Arrays.asList(registro1));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala PUBLICADA não deve poder ser excluída");
        assertTrue(resultado.getMensagem().contains("publicada") ||
                        resultado.getMensagem().contains("fechada"),
                "Deve bloquear primeiro por status");
    }

    /**
     * Caso 10: Escala NOVA com folgas aprovadas mas sem registros
     * não deve permitir exclusão.
     * Esperado: validação BLOQUEADA (pelas folgas).
     */
    @Test
    void deveBloquearExclusaoPorFolgasAntesDosRegistros() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 5), StatusFolgaEnum.APROVADA);
        escala.setFolgas(Arrays.asList(folga1));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala com folgas aprovadas não deve poder ser excluída");
        assertTrue(resultado.getMensagem().contains("folgas aprovadas"),
                "Deve bloquear por folgas aprovadas");
    }

    /**
     * Caso 11: Escala NOVA apenas com folgas pendentes e rejeitadas
     * deve permitir exclusão.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirExclusaoComApenasFolgasPendentesERejeitadas() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 5), StatusFolgaEnum.PENDENTE);
        Folga folga2 = criarFolga(LocalDate.of(2025, 6, 12), StatusFolgaEnum.PENDENTE);
        Folga folga3 = criarFolga(LocalDate.of(2025, 6, 19), StatusFolgaEnum.NEGADA);
        escala.setFolgas(Arrays.asList(folga1, folga2, folga3));

        var resultado = validaExclusaoPossivel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala com apenas folgas pendentes/rejeitadas deve poder ser excluída");
    }

    /**
     * Caso 12: Escala PARCIAL com folgas null e registros vazios
     * deve permitir exclusão.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirExclusaoComFolgasNullERegistrosVazios() {
        escala.setStatus(StatusEscalaEnum.PARCIAL);
        escala.setFolgas(null);
        escala.setRegistros(new ArrayList<>());

        var resultado = validaExclusaoPossivel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala PARCIAL sem dependências deve poder ser excluída");
    }

    /**
     * Caso 13: Escala NOVA com registros null mas com folgas pendentes
     * deve permitir exclusão.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirExclusaoComRegistrosNullEFolgasPendentes() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 5), StatusFolgaEnum.PENDENTE);
        escala.setFolgas(List.of(folga1));
        escala.setRegistros(null);

        var resultado = validaExclusaoPossivel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala NOVA com registros null e sem folgas aprovadas deve poder ser excluída");
    }

    /**
     * Caso 14: Escala vazia (sem folgas e sem registros) em estado NOVA
     * deve permitir exclusão.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirExclusaoDeEscalaVazia() {
        escala.setStatus(StatusEscalaEnum.NOVA);
        escala.setFolgas(new ArrayList<>());
        escala.setRegistros(new ArrayList<>());

        var resultado = validaExclusaoPossivel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala vazia deve poder ser excluída");
    }

    // ==================== Métodos Auxiliares ====================

    private Folga criarFolga(LocalDate data, StatusFolgaEnum status) {
        Folga folga = new Folga();
        folga.setId(System.currentTimeMillis() + Math.round(Math.random() * 1000));
        folga.setColaborador(colaborador);
        folga.setEscala(escala);
        folga.setDataSolicitada(data);
        folga.setStatus(status);
        return folga;
    }

    private EscalaColaborador criarRegistro(LocalDate data) {
        EscalaColaborador registro = new EscalaColaborador();
        registro.setId(System.currentTimeMillis() + Math.round(Math.random() * 1000));
        registro.setColaborador(colaborador);
        registro.setEscala(escala);
        registro.setDiaDaEscala(data);
        return registro;
    }
}
