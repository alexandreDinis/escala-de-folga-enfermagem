package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.ColaboradorRepository;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidaHistoricoEscalaAnteriorTest {

    @Mock
    private EscalaRepository escalaRepository;

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @InjectMocks
    private ValidaHistoricoEscalaAnterior validador;

    private Escala escala;
    private Escala escalaAnterior;
    private Setor setor;

    @BeforeEach
    void setUp() {
        setor = new Setor();
        setor.setId(1L);
        setor.setNome("UTI");

        escala = new Escala();
        escala.setId(1L);
        escala.setMes(5);
        escala.setAno(2025);
        escala.setTurno(TurnoEnum.NOITE);
        escala.setSetor(setor);
        escala.setStatus(StatusEscalaEnum.NOVA);
        escala.setFolgas(new ArrayList<>());
        escala.setRegistros(new ArrayList<>());

        escalaAnterior = new Escala();
        escalaAnterior.setId(2L);
        escalaAnterior.setMes(4);
        escalaAnterior.setAno(2025);
        escalaAnterior.setTurno(TurnoEnum.NOITE);
        escalaAnterior.setSetor(setor);
        escalaAnterior.setStatus(StatusEscalaEnum.PUBLICADA);
    }

    /**
     * Caso 1: Existe escala anterior no sistema.
     * Esperado: validação deve retornar OK.
     */
    @Test
    void deveRetornarOkQuandoExisteEscalaAnterior() {
        when(escalaRepository.findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        )).thenReturn(Optional.of(escalaAnterior));

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK quando existe escala anterior");
        verify(escalaRepository, times(1)).findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        );
        verify(colaboradorRepository, never()).existsBySetorAndTurnoAndUltimaFolgaNull(any(), any());
    }

    /**
     * Caso 2: Não existe escala anterior mas todos colaboradores têm última folga registrada.
     * Esperado: validação deve retornar OK.
     */
    @Test
    void deveRetornarOkQuandoNaoExisteEscalaAnteriorMasColaboradoresTemHistorico() {
        when(escalaRepository.findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        )).thenReturn(Optional.empty());

        when(colaboradorRepository.existsBySetorAndTurnoAndUltimaFolgaNull(
                escala.getSetor(),
                escala.getTurno()
        )).thenReturn(false);

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK quando não existe escala anterior mas colaboradores têm histórico");
        verify(escalaRepository, times(1)).findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        );
        verify(colaboradorRepository, times(1)).existsBySetorAndTurnoAndUltimaFolgaNull(
                escala.getSetor(),
                escala.getTurno()
        );
    }

    /**
     * Caso 3: Não existe escala anterior e existem colaboradores sem histórico de última folga.
     * Esperado: validação deve retornar erro com mensagem informativa.
     */
    @Test
    void deveRetornarErroQuandoNaoExisteEscalaAnteriorEColaboradoresSemHistorico() {
        when(escalaRepository.findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        )).thenReturn(Optional.empty());

        when(colaboradorRepository.existsBySetorAndTurnoAndUltimaFolgaNull(
                escala.getSetor(),
                escala.getTurno()
        )).thenReturn(true);

        var resultado = validador.validar(escala);

        assertFalse(resultado.isValido(),
                "Validação deve retornar erro quando não existe histórico válido");
        assertTrue(resultado.getMensagem().contains("Não foi encontrada escala anterior no sistema"),
                "Mensagem de erro deve orientar o usuário a cadastrar histórico");
        assertTrue(resultado.getMensagem().contains("Cadastre o histórico antes de criar a escala"),
                "Mensagem de erro deve orientar o usuário a cadastrar histórico");
        verify(escalaRepository, times(1)).findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        );
        verify(colaboradorRepository, times(1)).existsBySetorAndTurnoAndUltimaFolgaNull(
                escala.getSetor(),
                escala.getTurno()
        );
    }

    /**
     * Caso 4: Primeira escala do ano, mas com histórico manual cadastrado.
     * Esperado: validação deve retornar OK.
     */
    @Test
    void deveRetornarOkQuandoPrimeiraEscalaDoAnoComHistoricoManual() {
        escala.setMes(1);
        escala.setAno(2025);

        when(escalaRepository.findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        )).thenReturn(Optional.empty());

        when(colaboradorRepository.existsBySetorAndTurnoAndUltimaFolgaNull(
                escala.getSetor(),
                escala.getTurno()
        )).thenReturn(false);

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK para primeira escala do ano com histórico manual");
        verify(escalaRepository, times(1)).findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        );
        verify(colaboradorRepository, times(1)).existsBySetorAndTurnoAndUltimaFolgaNull(
                escala.getSetor(),
                escala.getTurno()
        );
    }

    /**
     * Caso 5: Virada de ano com escala de dezembro anterior existente.
     * Esperado: validação deve retornar OK.
     */
    @Test
    void deveRetornarOkQuandoViradaDeAnoComEscalaDezembroAnterior() {
        escala.setMes(1);
        escala.setAno(2025);

        escalaAnterior.setMes(12);
        escalaAnterior.setAno(2024);

        when(escalaRepository.findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        )).thenReturn(Optional.of(escalaAnterior));

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK na virada de ano quando existe escala de dezembro");
        verify(escalaRepository, times(1)).findEscalaAnterior(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor()
        );
        verify(colaboradorRepository, never()).existsBySetorAndTurnoAndUltimaFolgaNull(any(), any());
    }
}