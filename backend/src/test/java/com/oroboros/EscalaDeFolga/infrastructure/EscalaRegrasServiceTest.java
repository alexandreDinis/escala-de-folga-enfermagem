package com.oroboros.EscalaDeFolga.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.oroboros.EscalaDeFolga.domain.service.EscalaRegrasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EscalaRegrasServiceTest {

    private EscalaRegrasService service;

    @BeforeEach
    void setup() {
        service = new EscalaRegrasService();
    }


    @Test
    void testCalculaMediaDeFolgaPorDia() {
        int totalColaboradores = 37;
        int[] diasDoMes = {31, 28, 30}; // Janeiro, Fevereiro, Março
        int[] qtdDomingosOpcoes = {5, 6}; // número de domingos no mês
        int[] folgasPorColaboradorOpcoes = {5, 6}; // exemplo: 5 folgas/mês por colaborador ou 6

        String[] meses = {"Janeiro", "Fevereiro", "Março"};

        for (int i = 0; i < diasDoMes.length; i++) {
            int totalDiasNoMes = diasDoMes[i];
            String mes = meses[i];

            for (int qtdDomingos : qtdDomingosOpcoes) {
                for (int folgasPorColaborador : folgasPorColaboradorOpcoes) {

                    System.out.println("=== Mês: " + mes +
                            ", Colaboradores: " + totalColaboradores +
                            ", Domingos no mês: " + qtdDomingos +
                            ", folgasPorColaborador: " + folgasPorColaborador + " ===");

                    int resultDomingo = service.calculaMediaDeFolgaPorDia(
                            totalDiasNoMes,
                            totalColaboradores,
                            qtdDomingos,
                            DayOfWeek.SUNDAY,
                            folgasPorColaborador);

                    int resultSemana = service.calculaMediaDeFolgaPorDia(
                            totalDiasNoMes,
                            totalColaboradores,
                            qtdDomingos,
                            DayOfWeek.MONDAY,
                            folgasPorColaborador);

                    // Aqui você pode calcular o "esperado" usando a mesma fórmula ou a regra de negócio que você definiu.
                    // Exemplo de esperado (conforme sua lógica atual):
                    int esperadoDomingo = qtdDomingos > 0 ? (int) Math.ceil((double) totalColaboradores / qtdDomingos) : 0;
                    int totalFolgas = totalColaboradores * folgasPorColaborador;
                    int totalFolgasDomingos = esperadoDomingo * qtdDomingos;
                    int restante = totalFolgas - totalFolgasDomingos;
                    int diasNaoDomingo = totalDiasNoMes - qtdDomingos;
                    int esperadoSemana = diasNaoDomingo > 0 ? (int) Math.ceil((double) Math.max(0, restante) / diasNaoDomingo) : 0;

                    System.out.println("Esperado - Folgas por Domingo: " + esperadoDomingo);
                    System.out.println("Retornado - Folgas por Domingo: " + resultDomingo);
                    System.out.println("Esperado - Folgas por dia restante: " + esperadoSemana);
                    System.out.println("Retornado - Folgas por dia restante: " + resultSemana);
                    System.out.println();

                    // asserts para garantir que método está coerente com a regra calculada
                    assertEquals(esperadoDomingo, resultDomingo, "Domingo mismatch");
                    assertEquals(esperadoSemana, resultSemana, "Semana mismatch");
                }
            }
        }
    }
}
