package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.domain.service.EscalaRegrasService;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes para ValidaLimiteDeDiasTrabalho usando Mockito puro (sem Spring Boot).
 * Isso garante que o validador real seja executado.
 */
@ExtendWith(MockitoExtension.class)
class ValidaLimiteDeDiasTrabalhoTest {

    @Mock
    private FolgaRepository folgaRepository;

    @Mock
    private EscalaRegrasService escalaRegrasService;

    @InjectMocks
    private ValidaLimiteDeDiasTrabalho validaLimiteDeDiasTrabalho;

    private Colaborador colaborador;
    private Folga folga;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        colaborador.setId(1L);
        colaborador.setNome("Alexandre Dinis");

        folga = new Folga();
        folga.setColaborador(colaborador);
    }

    /**
     * Método auxiliar para configurar o mock do limite de dias.
     * Chame isso em cada teste que precisar validar a regra.
     */
    private void configurarLimiteDeDias(int dias) {
        lenient().when(escalaRegrasService.getDiasTrabalhoPermitidos()).thenReturn(dias);
    }

    /**
     * Caso 1: Colaborador sem folgas anteriores.
     * Esperado: deve permitir nova folga.
     */
    @Test
    void devePermitirFolgaQuandoNaoExistirFolgaAnterior() {
        LocalDate dataNova = LocalDate.of(2025, 3, 5);
        folga.setDataSolicitada(dataNova);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, dataNova))
                .thenReturn(Optional.empty());

        var resultado = validaLimiteDeDiasTrabalho.validar(folga);

        assertTrue(resultado.isValido());
        assertEquals("Folga válida.", resultado.getMensagem());

        verify(folgaRepository).findUltimaFolgaAntesDe(colaborador, dataNova);
        verifyNoInteractions(escalaRegrasService); // Não deve chamar quando não há folga anterior
    }

    /**
     * Caso 2: Última folga dia 1, nova dia 4 (intervalo = 3 dias, trabalhados = 2).
     * Esperado: deve permitir nova folga.
     */
    @Test
    void devePermitirFolgaQuandoTrabalhouMenosDe6Dias() {
        configurarLimiteDeDias(6);

        LocalDate ultima = LocalDate.of(2025, 3, 1);  // Folga dia 1
        LocalDate nova = LocalDate.of(2025, 3, 4);    // Folga dia 4
        // Trabalhou dias 2, 3 = 2 dias (< 6)

        folga.setDataSolicitada(nova);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, nova))
                .thenReturn(Optional.of(ultima));

        var resultado = validaLimiteDeDiasTrabalho.validar(folga);

        assertTrue(resultado.isValido(), "Com 2 dias trabalhados, a folga deve ser permitida");
        assertEquals("Folga válida.", resultado.getMensagem());
    }

    /**
     * Caso 3: Última folga dia 1, nova dia 7 (intervalo = 6 dias, trabalhados = 5).
     * Esperado: deve PERMITIR (ainda dentro do limite).
     */
    @Test
    void devePermitirFolgaQuandoTrabalhou5Dias() {
        configurarLimiteDeDias(6);

        LocalDate ultima = LocalDate.of(2025, 3, 1);  // Folga dia 1
        LocalDate nova = LocalDate.of(2025, 3, 7);    // Folga dia 7
        // Trabalhou dias 2, 3, 4, 5, 6 = 5 dias (< 6)

        folga.setDataSolicitada(nova);
        folga.setStatus(StatusFolgaEnum.PENDENTE);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, nova))
                .thenReturn(Optional.of(ultima));

        var resultado = validaLimiteDeDiasTrabalho.validar(folga);

        assertTrue(resultado.isValido(),
                "Com 5 dias trabalhados (< 6), a folga deve ser PERMITIDA");
    }

    /**
     * Caso 4: Última folga dia 1, nova dia 8 (intervalo = 7 dias, trabalhados = 6).
     * Esperado: deve REJEITAR - pela lei da escala 6x1, após 6 dias o 7º DEVE ser folga.
     */
    @Test
    void deveRecusarFolgaQuandoTrabalhouExatos6Dias() {
        configurarLimiteDeDias(6);

        LocalDate ultima = LocalDate.of(2025, 3, 1);  // Folga dia 1
        LocalDate nova = LocalDate.of(2025, 3, 8);    // Folga dia 8
        // Trabalhou dias 2, 3, 4, 5, 6, 7 = 6 dias
        // O dia 8 deveria ser folga obrigatória (após 6 dias trabalhados)

        folga.setDataSolicitada(nova);
        folga.setStatus(StatusFolgaEnum.PENDENTE);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, nova))
                .thenReturn(Optional.of(ultima));

        var resultado = validaLimiteDeDiasTrabalho.validar(folga);

        System.out.println("🔍 Caso 6 dias trabalhados:");
        System.out.println("   isValido: " + resultado.isValido());
        System.out.println("   mensagem: " + resultado.getMensagem());

        assertFalse(resultado.isValido(),
                "Pela escala 6x1, após 6 dias trabalhados o colaborador DEVE folgar (lei)");
        assertTrue(
                resultado.getMensagem().toLowerCase().contains("ultrapassou o limite"),
                "Mensagem deveria indicar que ultrapassou o limite. Mensagem atual: " + resultado.getMensagem()
        );
    }

    /**
     * Caso 5: Última folga dia 1, nova dia 9 (intervalo = 8 dias, trabalhados = 7).
     * Esperado: deve REJEITAR (ultrapassou o limite de 6).
     */
    @Test
    void deveRecusarFolgaQuandoTrabalhouMaisDe6Dias() {
        configurarLimiteDeDias(6);

        LocalDate ultima = LocalDate.of(2025, 3, 1);  // Folga dia 1
        LocalDate nova = LocalDate.of(2025, 3, 9);    // Folga dia 9
        // Trabalhou dias 2, 3, 4, 5, 6, 7, 8 = 7 dias (> 6)

        folga.setDataSolicitada(nova);
        folga.setStatus(StatusFolgaEnum.PENDENTE);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, nova))
                .thenReturn(Optional.of(ultima));

        var resultado = validaLimiteDeDiasTrabalho.validar(folga);

        System.out.println("🔍 Caso 7 dias trabalhados:");
        System.out.println("   isValido: " + resultado.isValido());
        System.out.println("   mensagem: " + resultado.getMensagem());

        assertFalse(resultado.isValido(),
                "A folga deveria ser recusada por ultrapassar 6 dias de trabalho consecutivos");
        assertTrue(
                resultado.getMensagem().toLowerCase().contains("ultrapassou o limite"),
                "Mensagem deveria indicar ultrapassagem do limite. Mensagem atual: " + resultado.getMensagem()
        );
    }

    /**
     * Caso 6: Última folga 28/02, nova 05/03 (intervalo = 6 dias, trabalhados = 5).
     * Esperado: folga permitida.
     */
    @Test
    void devePermitirFolgaQuandoUltimaFolgaFoiNaEscalaAnteriorDentroDoLimite() {
        configurarLimiteDeDias(6);

        LocalDate ultima = LocalDate.of(2025, 2, 28);  // Folga 28/02
        LocalDate nova = LocalDate.of(2025, 3, 5);     // Folga 05/03
        // Trabalhou dias 01, 02, 03, 04/03 = 4 dias (< 6)

        folga.setDataSolicitada(nova);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, nova))
                .thenReturn(Optional.of(ultima));

        var resultado = validaLimiteDeDiasTrabalho.validar(folga);

        assertTrue(resultado.isValido());
    }

    /**
     * Caso 7: Última folga 25/02, nova 05/03 (intervalo = 9 dias, trabalhados = 8).
     * Esperado: deve rejeitar folga.
     */
    @Test
    void deveRecusarFolgaQuandoUltimaFolgaNaEscalaAnteriorUltrapassaLimite() {
        configurarLimiteDeDias(6);

        LocalDate ultima = LocalDate.of(2025, 2, 25);  // Folga 25/02
        LocalDate nova = LocalDate.of(2025, 3, 5);     // Folga 05/03
        // Trabalhou 26, 27, 28/02 + 01, 02, 03, 04/03 = 7 dias (> 6)

        folga.setDataSolicitada(nova);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, nova))
                .thenReturn(Optional.of(ultima));

        var resultado = validaLimiteDeDiasTrabalho.validar(folga);

        assertFalse(resultado.isValido());
        assertTrue(resultado.getMensagem().toLowerCase().contains("ultrapassou o limite"));
    }

    /**
     * Teste adicional: Verifica se o serviço de regras é consultado corretamente.
     */
    @Test
    void deveConsultarLimiteDeDiasDoServicoDeRegras() {
        configurarLimiteDeDias(6);

        LocalDate ultima = LocalDate.of(2025, 3, 1);
        LocalDate nova = LocalDate.of(2025, 3, 4);

        folga.setDataSolicitada(nova);

        when(folgaRepository.findUltimaFolgaAntesDe(colaborador, nova))
                .thenReturn(Optional.of(ultima));

        validaLimiteDeDiasTrabalho.validar(folga);

        verify(escalaRegrasService).getDiasTrabalhoPermitidos();
    }
}
