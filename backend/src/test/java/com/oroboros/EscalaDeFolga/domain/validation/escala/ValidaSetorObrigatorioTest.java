package com.oroboros.EscalaDeFolga.domain.validation.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.infrastructure.repository.SetorRepository;
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
public class ValidaSetorObrigatorioTest {

    @Mock
    private SetorRepository setorRepository;

    @InjectMocks
    private ValidaSetorObrigatorio validador;

    private Escala escala;
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
    }

    /**
     * Caso 1: Setor válido e existente no banco.
     * Esperado: validação deve retornar OK.
     */
    @Test
    void deveRetornarOkQuandoSetorExiste() {
        when(setorRepository.existsById(setor.getId())).thenReturn(true);

        var resultado = validador.validar(escala);

        assertTrue(resultado.isValido(),
                "Validação deve retornar OK quando setor existe");
        verify(setorRepository, times(1)).existsById(setor.getId());
    }

    /**
     * Caso 2: Setor é nulo.
     * Esperado: validação deve retornar erro.
     */
    @Test
    void deveRetornarErroQuandoSetorEhNulo() {
        escala.setSetor(null);

        var resultado = validador.validar(escala);

        assertFalse(resultado.isValido(),
                "Validação deve retornar erro quando setor é nulo");
        assertTrue(resultado.getMensagem().contains("deve estar vinculada a um setor válido"),
                "Mensagem deve indicar que setor é obrigatório");
        verify(setorRepository, never()).existsById(any());
    }

    /**
     * Caso 3: ID do setor é nulo.
     * Esperado: validação deve retornar erro.
     */
    @Test
    void deveRetornarErroQuandoIdDoSetorEhNulo() {
        setor.setId(null);
        escala.setSetor(setor);

        var resultado = validador.validar(escala);

        assertFalse(resultado.isValido(),
                "Validação deve retornar erro quando ID do setor é nulo");
        assertTrue(resultado.getMensagem().contains("Informe o ID do setor"),
                "Mensagem deve indicar que ID do setor é obrigatório");
        verify(setorRepository, never()).existsById(any());
    }

    /**
     * Caso 4: ID do setor é zero.
     * Esperado: validação deve retornar erro.
     */
    @Test
    void deveRetornarErroQuandoIdDoSetorEhZero() {
        setor.setId(0L);
        escala.setSetor(setor);

        var resultado = validador.validar(escala);

        assertFalse(resultado.isValido(),
                "Validação deve retornar erro quando ID do setor é zero");
        assertTrue(resultado.getMensagem().contains("deve ser um número positivo"),
                "Mensagem deve indicar que ID deve ser positivo");
        verify(setorRepository, never()).existsById(any());
    }

    /**
     * Caso 5: ID do setor é negativo.
     * Esperado: validação deve retornar erro.
     */
    @Test
    void deveRetornarErroQuandoIdDoSetorEhNegativo() {
        setor.setId(-1L);
        escala.setSetor(setor);

        var resultado = validador.validar(escala);

        assertFalse(resultado.isValido(),
                "Validação deve retornar erro quando ID do setor é negativo");
        assertTrue(resultado.getMensagem().contains("deve ser um número positivo"),
                "Mensagem deve indicar que ID deve ser positivo");
        verify(setorRepository, never()).existsById(any());
    }

    /**
     * Caso 6: ID do setor é válido mas não existe no banco.
     * Esperado: validação deve retornar erro informando que setor não existe.
     */
    @Test
    void deveRetornarErroQuandoSetorNaoExisteNoBanco() {
        when(setorRepository.existsById(setor.getId())).thenReturn(false);

        var resultado = validador.validar(escala);

        assertFalse(resultado.isValido(),
                "Validação deve retornar erro quando setor não existe no banco");
        assertTrue(resultado.getMensagem().contains("Setor não encontrado com ID: 1"),
                "Mensagem deve informar o ID do setor não encontrado");
        assertTrue(resultado.getMensagem().contains("Cadastre o setor antes de criar a escala"),
                "Mensagem deve orientar a cadastrar o setor");
        verify(setorRepository, times(1)).existsById(setor.getId());
    }

    /**
     * Caso 7: Múltiplas escalas com setores diferentes, mas apenas um existe.
     * Esperado: validação deve falhar apenas para setor inexistente.
     */
    @Test
    void deveValidarCorretamenteSetoresExistentesEInexistentes() {
        // Primeiro setor existe
        when(setorRepository.existsById(1L)).thenReturn(true);

        Escala escala1 = new Escala();
        Setor setor1 = new Setor();
        setor1.setId(1L);
        escala1.setSetor(setor1);

        var resultado1 = validador.validar(escala1);
        assertTrue(resultado1.isValido(), "Deve validar setor existente");

        // Segundo setor não existe
        when(setorRepository.existsById(999L)).thenReturn(false);

        Escala escala2 = new Escala();
        Setor setor2 = new Setor();
        setor2.setId(999L);
        escala2.setSetor(setor2);

        var resultado2 = validador.validar(escala2);
        assertFalse(resultado2.isValido(), "Deve rejeitar setor inexistente");
        assertTrue(resultado2.getMensagem().contains("999"),
                "Mensagem deve conter o ID do setor inexistente");

        verify(setorRepository, times(1)).existsById(1L);
        verify(setorRepository, times(1)).existsById(999L);
    }
}
