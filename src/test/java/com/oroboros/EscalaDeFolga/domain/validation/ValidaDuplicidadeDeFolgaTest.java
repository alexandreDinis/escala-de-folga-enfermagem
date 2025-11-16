package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.validation.folga.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.domain.validation.folga.ValidaDuplicidadeDeFolga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ValidaDuplicidadeDeFolgaTest {

    @Mock
    private FolgaRepository folgaRepository;

    @InjectMocks
    private ValidaDuplicidadeDeFolga validaDuplicidadeDeFolga;

    private Folga folga;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Colaborador colaborador = new Colaborador();
        colaborador.setId(1L);

        folga = new Folga();
        folga.setColaborador(colaborador);
        folga.setDataSolicitada(LocalDate.of(2025, 11, 5));
    }

    @Test
    void deveRetornarErroQuandoJaExistirFolgaNaMesmaData() {
        // given
        when(folgaRepository.existsByColaboradorAndDataSolicitada(folga.getColaborador(), folga.getDataSolicitada()))
                .thenReturn(true);

        // when
        ResultadoValidacao resultado = validaDuplicidadeDeFolga.validar(folga);

        // then
        assertFalse(resultado.isValido());
        assertEquals("Já existe uma folga cadastrada para essa data.", resultado.getMensagem());

        verify(folgaRepository, times(1))
                .existsByColaboradorAndDataSolicitada(folga.getColaborador(), folga.getDataSolicitada());
    }

    @Test
    void deveRetornarOkQuandoNaoExistirFolgaDuplicada() {
        // given
        when(folgaRepository.existsByColaboradorAndDataSolicitada(folga.getColaborador(), folga.getDataSolicitada()))
                .thenReturn(false);

        // when
        ResultadoValidacao resultado = validaDuplicidadeDeFolga.validar(folga);

        // then
        assertTrue(resultado.isValido());
        assertEquals("Folga válida.", resultado.getMensagem());

        verify(folgaRepository, times(1))
                .existsByColaboradorAndDataSolicitada(folga.getColaborador(), folga.getDataSolicitada());
    }
}