package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidaEscalaEditavelTest {

    @InjectMocks
    private ValidaEscalaEditavel validaEscalaEditavel;

    private Escala escala;
    private Colaborador colaborador;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("João Silva");

        escala = new Escala();
        escala.setId(1L);
        escala.setMes(5);
        escala.setAno(2025);
        escala.setFolgasPermitidas(4);
        escala.setFolgas(new ArrayList<>());
    }

    /**
     * Caso 1: Escala com status NOVA deve permitir edição.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirEdicaoQuandoEscalaEstaNova() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        var resultado = validaEscalaEditavel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala com status NOVA deve poder ser editada");
    }

    /**
     * Caso 2: Escala com status PARCIAL deve permitir edição.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirEdicaoQuandoEscalaEstaParcial() {
        escala.setStatus(StatusEscalaEnum.PARCIAL);

        var resultado = validaEscalaEditavel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala com status PARCIAL deve poder ser editada");
    }

    /**
     * Caso 3: Escala com status PUBLICADA não deve permitir edição.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearEdicaoQuandoEscalaEstaPublicada() {
        escala.setStatus(StatusEscalaEnum.PUBLICADA);

        var resultado = validaEscalaEditavel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala PUBLICADA não deve poder ser editada");
        assertTrue(resultado.getMensagem().contains("PUBLICADA"),
                "Mensagem deve mencionar o status PUBLICADA");
        assertTrue(resultado.getMensagem().contains("5/2025"),
                "Mensagem deve mencionar mês e ano da escala");
    }

    /**
     * Caso 4: Escala com status FECHADA não deve permitir edição.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearEdicaoQuandoEscalaEstaFechada() {
        escala.setStatus(StatusEscalaEnum.FECHADA);

        var resultado = validaEscalaEditavel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala FECHADA não deve poder ser editada");
        assertTrue(resultado.getMensagem().contains("FECHADA"),
                "Mensagem deve mencionar o status FECHADA");
    }

    /**
     * Caso 5: Escala NOVA com folgas pendentes deve permitir edição.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirEdicaoComFolgasPendentes() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5), StatusFolgaEnum.PENDENTE);
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12), StatusFolgaEnum.PENDENTE);
        escala.setFolgas(Arrays.asList(folga1, folga2));

        var resultado = validaEscalaEditavel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala NOVA com apenas folgas pendentes deve poder ser editada");
    }

    /**
     * Caso 6: Escala NOVA com folgas rejeitadas deve permitir edição.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirEdicaoComFolgasRejeitadas() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5), StatusFolgaEnum.NEGADA);
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12), StatusFolgaEnum.NEGADA);
        escala.setFolgas(Arrays.asList(folga1, folga2));

        var resultado = validaEscalaEditavel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala NOVA com folgas rejeitadas deve poder ser editada");
    }

    /**
     * Caso 7: Escala PARCIAL com uma folga aprovada não deve permitir edição.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearEdicaoComUmaFolgaAprovada() {
        escala.setStatus(StatusEscalaEnum.PARCIAL);

        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5), StatusFolgaEnum.PENDENTE);
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12), StatusFolgaEnum.APROVADA);
        escala.setFolgas(Arrays.asList(folga1, folga2));

        var resultado = validaEscalaEditavel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala com folga aprovada não deve poder ser editada");
        assertTrue(resultado.getMensagem().contains("folgas aprovadas"),
                "Mensagem deve mencionar folgas aprovadas");
    }

    /**
     * Caso 8: Escala PARCIAL com múltiplas folgas aprovadas não deve permitir edição.
     * Esperado: validação BLOQUEADA.
     */
    @Test
    void deveBloquearEdicaoComMultiplasFolgasAprovadas() {
        escala.setStatus(StatusEscalaEnum.PARCIAL);

        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5), StatusFolgaEnum.APROVADA);
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12), StatusFolgaEnum.APROVADA);
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 19), StatusFolgaEnum.PENDENTE);
        escala.setFolgas(Arrays.asList(folga1, folga2, folga3));

        var resultado = validaEscalaEditavel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala com múltiplas folgas aprovadas não deve poder ser editada");
    }

    /**
     * Caso 9: Escala NOVA com mix de status (sem aprovadas) deve permitir edição.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirEdicaoComMixDeStatusSemAprovadas() {
        escala.setStatus(StatusEscalaEnum.NOVA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5), StatusFolgaEnum.PENDENTE);
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12), StatusFolgaEnum.NEGADA);
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 19), StatusFolgaEnum.PENDENTE);
        escala.setFolgas(Arrays.asList(folga1, folga2, folga3));

        var resultado = validaEscalaEditavel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala com mix de pendentes e rejeitadas deve poder ser editada");
    }

    /**
     * Caso 10: Escala NOVA sem folgas deve permitir edição.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirEdicaoQuandoNaoHaFolgas() {
        escala.setStatus(StatusEscalaEnum.NOVA);
        escala.setFolgas(new ArrayList<>());

        var resultado = validaEscalaEditavel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala NOVA sem folgas deve poder ser editada");
    }

    /**
     * Caso 11: Escala PUBLICADA com folgas aprovadas não deve permitir edição
     * (dois bloqueios simultâneos).
     * Esperado: validação BLOQUEADA (primeiro pelo status).
     */
    @Test
    void deveBloquearEdicaoPorStatusAntesDeFolgas() {
        escala.setStatus(StatusEscalaEnum.PUBLICADA);

        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5), StatusFolgaEnum.APROVADA);
        escala.setFolgas(Arrays.asList(folga1));

        var resultado = validaEscalaEditavel.validar(escala);

        assertFalse(resultado.isValido(),
                "Escala PUBLICADA não deve poder ser editada");
        assertTrue(resultado.getMensagem().contains("PUBLICADA"),
                "Deve bloquear primeiro por status, antes de verificar folgas");
    }

    /**
     * Caso 12: Escala PARCIAL com lista de folgas null deve permitir edição.
     * Esperado: validação OK.
     */
    @Test
    void devePermitirEdicaoQuandoListaDeFolgasNull() {
        escala.setStatus(StatusEscalaEnum.PARCIAL);
        escala.setFolgas(null);

        var resultado = validaEscalaEditavel.validar(escala);

        assertTrue(resultado.isValido(),
                "Escala PARCIAL com folgas null deve poder ser editada");
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
}
