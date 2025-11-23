package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ValidaEscalaDuplicadaTest {

    @Mock
    private EscalaRepository escalaRepository;

    @InjectMocks
    private ValidaEscalaDuplicada validador;

    private Escala escala;

    @BeforeEach
    void setUp() {
        Setor setor = new Setor();
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
    }

    /**
     * Caso 1: Não existe escala duplicada.
     * Esperado: validação deve retornar OK.
     */
    @Test
    void deveRetornarOkQuandoNaoExisteEscalaDuplicada() {
        when(escalaRepository.existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        )).thenReturn(false);

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK quando não existe escala duplicada");
        verify(escalaRepository, times(1)).existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        );
    }

    /**
     * Caso 2: Existe uma escala duplicada com status NOVA.
     * Esperado: validação deve retornar erro com mensagem descritiva.
     */
    @Test
    void deveRetornarErroQuandoExisteEscalaDuplicada() {
        when(escalaRepository.existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        )).thenReturn(true);

        var resultado = validador.validar(escala);

        assertFalse(resultado.isValido(),
                "Validação deve retornar erro quando existe escala duplicada");
        assertTrue(resultado.getMensagem().contains("Já existe uma escala aberta para 5/2025"),
                "Mensagem de erro deve informar mês e ano da duplicidade");
        assertTrue(resultado.getMensagem().contains("turno NOITE"),
                "Mensagem de erro deve informar o turno da duplicidade");
        assertTrue(resultado.getMensagem().contains("setor UTI"),
                "Mensagem de erro deve informar o setor da duplicidade");
        verify(escalaRepository, times(1)).existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        );
    }

    /**
     * Caso 3: Existe escala com mesmo mês/ano/turno/setor mas com status diferente de NOVA.
     * Esperado: validação deve retornar OK (não é duplicada).
     */
    @Test
    void deveRetornarOkQuandoExisteEscalaMasComStatusDiferente() {
        when(escalaRepository.existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        )).thenReturn(false);

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK quando escala existente não está com status NOVA");
        verify(escalaRepository, times(1)).existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        );
    }

    /**
     * Caso 4: Verificação com turno diferente.
     * Esperado: validação deve retornar OK (não é duplicada).
     */
    @Test
    void deveRetornarOkQuandoTurnoEhDiferente() {
        escala.setTurno(TurnoEnum.MANHA);

        when(escalaRepository.existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        )).thenReturn(false);

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK quando turno é diferente");
        verify(escalaRepository, times(1)).existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                TurnoEnum.MANHA,
                escala.getSetor(),
                StatusEscalaEnum.NOVA
        );
    }

    /**
     * Caso 5: Verificação com setor diferente.
     * Esperado: validação deve retornar OK (não é duplicada).
     */
    @Test
    void deveRetornarOkQuandoSetorEhDiferente() {
        Setor outroSetor = new Setor();
        outroSetor.setId(2L);
        outroSetor.setNome("Emergência");
        escala.setSetor(outroSetor);

        when(escalaRepository.existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                outroSetor,
                StatusEscalaEnum.NOVA
        )).thenReturn(false);

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK quando setor é diferente");
        verify(escalaRepository, times(1)).existsByMesAndAnoAndTurnoAndSetorAndStatus(
                escala.getMes(),
                escala.getAno(),
                escala.getTurno(),
                outroSetor,
                StatusEscalaEnum.NOVA
        );
    }
}