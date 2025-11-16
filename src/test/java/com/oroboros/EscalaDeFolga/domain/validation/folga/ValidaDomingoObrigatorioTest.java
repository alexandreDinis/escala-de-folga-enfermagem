package com.oroboros.EscalaDeFolga.domain.validation.folga;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.infrastructure.repository.FolgaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidaDomingoObrigatorioTest {

    @Mock
    private FolgaRepository folgaRepository;

    @InjectMocks
    private ValidaDomingoObrigatorio validaDomingoObrigatorio;

    private Colaborador colaborador;
    private Escala escala;
    private Folga folga;

    @BeforeEach
    void setUp() {
        colaborador = new Colaborador();
        escala = new Escala();
        escala.setMes(3);
        escala.setAno(2025);
        escala.setFolgasPermitidas(6);
        folga = new Folga();
        folga.setColaborador(colaborador);
        folga.setEscala(escala);
    }

    @Test
    void deveRetornarErroQuandoFaltaUmaFolgaEColaboradorNaoTemDomingo() {
        when(folgaRepository.countByColaboradorAndEscalaAndStatusIn(any(), any(), any()))
                .thenReturn(5L);
        when(folgaRepository.existsFolgaDomingoNoMes(any(), anyInt(), anyInt()))
                .thenReturn(false);

        ResultadoValidacao resultado = validaDomingoObrigatorio.validar(folga);

        assertFalse(resultado.isValido());
        assertEquals("Faltam apenas uma folga e o colaborador ainda não folgou em um domingo.", resultado.getMensagem());
    }

    @Test
    void deveRetornarOkQuandoColaboradorJaFolgoUmDomingo() {
        when(folgaRepository.existsFolgaDomingoNoMes(any(), anyInt(), anyInt()))
                .thenReturn(true);

        ResultadoValidacao resultado = validaDomingoObrigatorio.validar(folga);

        assertTrue(resultado.isValido());
    }
}

