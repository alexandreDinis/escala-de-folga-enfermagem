package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.EscalaColaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ValidaLimiteDeFolgasTest {

    @Mock
    private FolgaRepository folgaRepository;

    @InjectMocks
    private ValidaLimiteDeFolgas validaLimiteDeFolgas;

    private Folga folga;

    private Escala escala;

    private EscalaColaborador escalaColaborador;

    private static final List<StatusFolgaEnum> STATUS_VALIDOS =
            List.of(StatusFolgaEnum.PENDENTE, StatusFolgaEnum.APROVADA);


    @BeforeEach
    void setUp(){
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
    void deveRetornarTrueSePedidoDeFolgaForMenorQueFolgasPermitidas(){
        when(folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                folga.getColaborador(),folga.getEscala(),
                STATUS_VALIDOS))
                .thenReturn(5L);

        boolean result = validaLimiteDeFolgas.validarFolga(folga);

        assertTrue(result, "Deve retornar true se pedido de folga for menor que folgas permitidas");
        verify(folgaRepository, times(1))
                .countByColaboradorAndEscalaAndStatusIn(
                folga.getColaborador(),folga.getEscala(),
                        STATUS_VALIDOS);
    }

    @Test
    void deveRetornarFalseSePedidoDeFolgaForMaiorQueFolgasPermitidas(){
        when(folgaRepository.countByColaboradorAndEscalaAndStatusIn(
                folga.getColaborador(), folga.getEscala(),
                STATUS_VALIDOS))
                .thenReturn(6L);

        boolean result = validaLimiteDeFolgas.validarFolga(folga);

        assertFalse(result, "Deve retornar falso se o pedido de folga for Maior que folgas permitidas");

        verify(folgaRepository, times(1))
                .countByColaboradorAndEscalaAndStatusIn(
                        folga.getColaborador(),folga.getEscala(),
                        STATUS_VALIDOS);

    }
}
