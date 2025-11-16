package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.service.EscalaRegrasService;
import com.oroboros.EscalaDeFolga.domain.validation.folga.ValidaDistribuicaoSemanalDeFolgas;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidaDistribuicaoSemanalDeFolgasTest {


    @Mock
    private FolgaRepository folgaRepository;

    @Mock
    private EscalaRegrasService escalaRegrasService;

    @InjectMocks
    private ValidaDistribuicaoSemanalDeFolgas validaDistribuicaoSemanalDeFolgas;

    private Colaborador colaborador;
    private Escala escala;
    private Folga folgaNova;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("Alexandre Dinis");

        // Escala de Maio/2025 com 5 FOLGAS PERMITIDAS
        escala = new Escala();
        escala.setId(1L);
        escala.setMes(5);
        escala.setAno(2025);
        escala.setFolgasPermitidas(5); // DIFERENÇA PRINCIPAL

        folgaNova = new Folga();
        folgaNova.setColaborador(colaborador);
        folgaNova.setEscala(escala);
        folgaNova.setStatus(StatusFolgaEnum.PENDENTE);
    }

    /**
     * Caso 1: Com 5 folgas, é possível ter 1 folga por semana e ainda sobra 1.
     * Colaborador tem 4 folgas distribuídas (semanas 1, 2, 3, 4), solicita 5ª na semana 1.
     * Esperado: deve PERMITIR, pois todas as semanas já têm pelo menos 1 folga.
     */
    @Test
    void devePermitirQuintaFolgaQuandoTodasSemanasJaEstaoCobertas() {
        // Folgas distribuídas: uma por semana nas 4 primeiras semanas
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 19)); // Semana 3
        Folga folga4 = criarFolga(LocalDate.of(2025, 5, 26)); // Semana 4

        // 5ª folga na semana 1 (reforço)
        LocalDate dataNova = LocalDate.of(2025, 5, 4); // Semana 1 (domingo)
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertTrue(resultado.isValido(),
                "Com 5 folgas e todas as semanas cobertas, deve permitir folga extra em qualquer semana");
    }

    /**
     * Caso 2: Colaborador concentra 3 folgas na semana 1 e 1 na semana 2.
     * Solicita 5ª folga também na semana 1 (seria a 4ª nesta semana).
     * Semanas 3, 4 e 5 ficariam descobertas.
     * Esperado: deve BLOQUEAR por concentração excessiva.
     */
    @Test
    void deveBloquear4aFolgaNaMesmaSemanaComOutrasDescobertas() {
        // 3 folgas na semana 1, 1 na semana 2
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1 (segunda)
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 6));  // Semana 1 (terça)
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 7));  // Semana 1 (quarta)
        Folga folga4 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2 (segunda)

        // 5ª folga também na semana 1 (última folga)
        LocalDate dataNova = LocalDate.of(2025, 5, 8); // Semana 1 (quinta)
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertFalse(resultado.isValido(),
                "Deve bloquear: semana 1 teria 4 folgas enquanto semanas 3, 4 e 5 ficariam sem nenhuma");
        assertTrue(resultado.getMensagem().contains("atingirá o limite"),
                "Mensagem deve indicar que é a última folga");
        assertTrue(resultado.getMensagem().contains("sem folga"),
                "Mensagem deve listar semanas descobertas");
    }

    /**
     * Caso 3: Distribuição 2-1-1-1 (semanas 1, 2, 3, 4) - 4 folgas usadas.
     * Solicita 5ª na semana 1 (seria a 3ª lá) mas semana 5 fica descoberta.
     * Esperado: depende se considera semana 5 - deve BLOQUEAR se considerar.
     */
    @Test
    void deveBloquearQuandoSemana5FicaSemFolgaCom5FolgasPermitidas() {
        // 2 folgas na semana 1, 1 em cada uma das semanas 2, 3, 4
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 4));  // Semana 1 (domingo)
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1 (segunda)
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2
        Folga folga4 = criarFolga(LocalDate.of(2025, 5, 19)); // Semana 3

        // 5ª e última folga na semana 4
        LocalDate dataNova = LocalDate.of(2025, 5, 26); // Semana 4
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertFalse(resultado.isValido(),
                "Deve bloquear pois semana 5 (30 e 31/05) ficará sem folga");
        assertTrue(resultado.getMensagem().contains("5"),
                "Mensagem deve mencionar semana 5");
    }

    /**
     * Caso 4: Distribuição perfeita incluindo semana 5.
     * Maio/2025 tem 5 semanas (semana 5 = 30 e 31, sexta e sábado).
     * Colaborador distribui 1 folga em cada semana.
     * Esperado: deve PERMITIR.
     */
    @Test
    void devePermitirDistribuicaoPerfeitaNas5Semanas() {
        // 1 folga em cada uma das 4 primeiras semanas
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 4));  // Semana 1 (domingo)
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 11)); // Semana 2 (domingo)
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 18)); // Semana 3 (domingo)
        Folga folga4 = criarFolga(LocalDate.of(2025, 5, 25)); // Semana 4 (domingo)

        // 5ª folga na semana 5 (última do mês)
        LocalDate dataNova = LocalDate.of(2025, 5, 31); // Semana 5 (sábado)
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertTrue(resultado.isValido(),
                "Distribuição perfeita de 1 folga por semana (incluindo semana 5 parcial) deve ser permitida");
    }

    /**
     * Caso 5: Colaborador tem 3 folgas (semanas 1, 2, 3), ainda restam 2.
     * Solicita na semana 1 (já tem 1 lá) - semanas 4 e 5 descobertas mas ainda há 1 folga restante.
     * Esperado: deve PERMITIR, pois ainda há 1 folga para distribuir.
     */
    @Test
    void devePermitirQuandoAindaHaFolgasParaDistribuir() {
        // 3 folgas nas semanas 1, 2 e 3
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 19)); // Semana 3

        // 4ª folga na semana 1 (ainda resta 1 folga para solicitar)
        LocalDate dataNova = LocalDate.of(2025, 5, 4); // Semana 1 (domingo)
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertTrue(resultado.isValido(),
                "Deve permitir pois ainda resta 1 folga que pode ser usada nas semanas 4 ou 5");
    }

    /**
     * Caso 6: Todas as 5 folgas concentradas nas semanas 1 e 2.
     * 3 na semana 1, 1 na semana 2, solicita 5ª também na semana 2.
     * Esperado: deve BLOQUEAR - semanas 3, 4 e 5 descobertas.
     */
    @Test
    void deveBloquearConcentracaoTotalEm2Semanas() {
        // 3 folgas na semana 1, 1 na semana 2
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 4));  // Semana 1 (domingo)
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1 (segunda)
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 6));  // Semana 1 (terça)
        Folga folga4 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2 (segunda)

        // 5ª e última folga também na semana 2
        LocalDate dataNova = LocalDate.of(2025, 5, 11); // Semana 2 (domingo)
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertFalse(resultado.isValido(),
                "Deve bloquear: concentração total em 2 semanas deixa 3 semanas descobertas");
        assertTrue(resultado.getMensagem().contains("3") ||
                        resultado.getMensagem().contains("4") ||
                        resultado.getMensagem().contains("5"),
                "Mensagem deve mencionar as semanas 3, 4 ou 5 descobertas");
    }

    /**
     * Caso 7: Teste com mês de 30 dias (Junho/2025) - 5 semanas mas última muito curta.
     * Verifica se o comportamento é consistente.
     */
    @Test
    void deveValidarCorretamenteEmMesCom30Dias() {
        // Ajusta escala para Junho/2025 (30 dias, 5 semanas)
        escala.setMes(6);

        // 4 folgas nas semanas 1, 2, 3, 4
        Folga folga1 = criarFolga(LocalDate.of(2025, 6, 1));  // Semana 1 (domingo)
        Folga folga2 = criarFolga(LocalDate.of(2025, 6, 8));  // Semana 2 (domingo)
        Folga folga3 = criarFolga(LocalDate.of(2025, 6, 15)); // Semana 3 (domingo)
        Folga folga4 = criarFolga(LocalDate.of(2025, 6, 22)); // Semana 4 (domingo)

        // 5ª folga na semana 5 (última, apenas dia 29 e 30)
        LocalDate dataNova = LocalDate.of(2025, 6, 29); // Semana 5 (domingo)
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertTrue(resultado.isValido(),
                "Deve permitir distribuição perfeita em mês com 30 dias");
    }

    /**
     * Caso 8: Cenário realista - colaborador vai distribuindo aos poucos.
     * Tem 2 folgas (semanas 1 e 2), solicita 3ª na semana 1 (concentração).
     * Ainda restam 2 folgas mas semanas 3, 4 e 5 descobertas.
     * Esperado: pode PERMITIR pois ainda há 2 folgas para distribuir.
     */
    @Test
    void devePermitirConcentracaoQuandoAindaHa2FolgasRestantes() {
        // 2 folgas nas semanas 1 e 2
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2

        // 3ª folga também na semana 1
        LocalDate dataNova = LocalDate.of(2025, 5, 4); // Semana 1 (domingo)
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertTrue(resultado.isValido(),
                "Deve permitir pois ainda restam 2 folgas para cobrir as semanas 3, 4 e 5");
    }

    /**
     * Caso 9: Limite crítico - 4 folgas usadas (semanas 1, 1, 2, 3).
     * Solicita 5ª na semana 1 (seria a 3ª lá).
     * Semanas 4 e 5 descobertas, não há mais folgas.
     * Esperado: deve BLOQUEAR por concentração com >2 folgas e semanas descobertas.
     */
    @Test
    void deveBloquearTerceiraFolgaNaMesmaSemanaComSemanasDescobertas() {
        // 2 na semana 1, 1 na semana 2, 1 na semana 3
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 4));  // Semana 1
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2
        Folga folga4 = criarFolga(LocalDate.of(2025, 5, 19)); // Semana 3

        // 5ª e última folga novamente na semana 1 (seria a 3ª)
        LocalDate dataNova = LocalDate.of(2025, 5, 6); // Semana 1
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertFalse(resultado.isValido(),
                "Deve bloquear: semana 1 terá 3 folgas enquanto semanas 4 e 5 ficarão sem nenhuma");
        assertTrue(resultado.getMensagem().contains("já possui") ||
                        resultado.getMensagem().contains("sem folga"),
                "Mensagem deve indicar problema de distribuição");
    }

    /**
     * Caso 10: Flexibilidade máxima - com 5 folgas é possível ter até 2 por semana
     * em 4 semanas e 1 na 5ª. Colaborador tem distribuição 2-2-1-0-0.
     * Solicita na semana 4, ficando 2-2-1-1-0 (semana 5 descoberta).
     * Esperado: deve BLOQUEAR pois é a última folga e semana 5 fica descoberta.
     */
    @Test
    void deveBloquearQuandoUltimaFolgaDeixaSemana5Descoberta() {
        // 2 na semana 1, 2 na semana 2, 1 na semana 3
        Folga folga1 = criarFolga(LocalDate.of(2025, 5, 4));  // Semana 1
        Folga folga2 = criarFolga(LocalDate.of(2025, 5, 5));  // Semana 1
        Folga folga3 = criarFolga(LocalDate.of(2025, 5, 11)); // Semana 2
        Folga folga4 = criarFolga(LocalDate.of(2025, 5, 12)); // Semana 2

        // 5ª e última folga na semana 4
        LocalDate dataNova = LocalDate.of(2025, 5, 26); // Semana 4
        folgaNova.setDataSolicitada(dataNova);

        when(folgaRepository.findByColaboradorAndEscalaAndStatusIn(
                eq(colaborador),
                eq(escala),
                eq(List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA))
        )).thenReturn(Arrays.asList(folga1, folga2, folga3, folga4));

        var resultado = validaDistribuicaoSemanalDeFolgas.validar(folgaNova);

        assertFalse(resultado.isValido(),
                "Deve bloquear: é a última folga e semanas 3 e 5 ficam descobertas");
    }

    // ==================== Métodos Auxiliares ====================

    private Folga criarFolga(LocalDate data) {
        Folga folga = new Folga();
        folga.setId(System.currentTimeMillis() + Math.round(Math.random() * 1000));
        folga.setColaborador(colaborador);
        folga.setEscala(escala);
        folga.setDataSolicitada(data);
        folga.setStatus(StatusFolgaEnum.APROVADA);
        return folga;
    }
}
