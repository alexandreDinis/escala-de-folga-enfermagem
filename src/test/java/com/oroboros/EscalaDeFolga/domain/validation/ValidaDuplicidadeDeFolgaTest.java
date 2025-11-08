package com.oroboros.EscalaDeFolga.domain.validation;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        folga.setDataSolicitada(LocalDate.of(2025,12, 1));
    }

    @Test
    void devoRetornarTrueQuandoNaoExistirFolgaDubplicada() {
        when(folgaRepository.existsByColaboradorAndDataSolicitada(
                folga.getColaborador(), folga.getDataSolicitada()
        )).thenReturn(false);

        boolean result = validaDuplicidadeDeFolga.validarFolga(folga);

        assertTrue(result, "Deve retornar True quando não existir folga duplicada");
        verify(folgaRepository, times(1))
                .existsByColaboradorAndDataSolicitada(folga.getColaborador(), folga.getDataSolicitada());

    }
    @Test
    void deveretornarFalseQuandoExistirUmaFolgaDuplocada(){
        when(folgaRepository.existsByColaboradorAndDataSolicitada(
                folga.getColaborador(), folga.getDataSolicitada()
        )).thenReturn(true);

        boolean result = validaDuplicidadeDeFolga.validarFolga(folga);

        assertFalse(result, "Deve retornar false quando ouver folga duplicada");

        verify(folgaRepository, times(1))
                .existsByColaboradorAndDataSolicitada(folga.getColaborador(), folga.getDataSolicitada());
    }
}
