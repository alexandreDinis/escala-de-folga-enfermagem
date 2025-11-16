package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ValidaLimiteDeFolgasTest {

    @Mock
    private FolgaRepository folgaRepository;

    @InjectMocks
    private ValidaLimiteDeFolgas validaLimiteDeFolgas;

    private Folga folga;
    private Escala escala;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Colaborador colaborador = new Colaborador();
        colaborador.setId(1L);

        escala = new Escala();
        escala.setFolgasPermitidas(6);

        folga = new Folga();
        folga.setColaborador(colaborador);
        folga.setEscala(escala);
    }

    @Test
    void deveRetornarErroSeColaboradorUltrapassarLimiteDeFolgas() {
        // given
        when(folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                folga.getColaborador(), folga.getEscala(),
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)))
                .thenReturn(10L);

        // when
        ResultadoValidacao resultado = validaLimiteDeFolgas.validar(folga);

        // then
        assertFalse(resultado.isValido());
        assertEquals("O colaborador já atingiu o limite de folgas para esta escala.", resultado.getMensagem());
    }

    @Test
    void deveRetornarOkSeAindaEstiverDentroDoLimite() {
        // given
        when(folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                folga.getColaborador(), folga.getEscala(),
                List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA)))
                .thenReturn(4L);

        // when
        ResultadoValidacao resultado = validaLimiteDeFolgas.validar(folga);

        // then
        assertTrue(resultado.isValido());
        assertEquals("Folga válida.", resultado.getMensagem());
    }
}
