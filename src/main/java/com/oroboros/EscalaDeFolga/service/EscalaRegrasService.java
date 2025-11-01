package com.oroboros.EscalaDeFolga.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class EscalaRegrasService {

    public static final int MAX_DIAS_CONSECUTIVOS_TRABALHO = 6;
    public static final int DIAS_TRABALHO_ANTES_FOLGA = 0;
    public static final int DIAS_FOLGA_APOS_TRABALHO = 1;
    public static final int DOMINGO_FOLGA_OBRIGATORIA = 1;
    public static final int MIN_FOLGAS_POR_SEMANA = 1;


    /**
     * Conta quantos sábados existem no mês da data informada.
     */
    public int contaNumeroDeSabadosNoMes(LocalDate data) {
        return contarDiasDaSemanaNoMes(data, DayOfWeek.SATURDAY);
    }

    /**
     * Conta domingos no mês (folga obrigatória)
     */
    public int contarDomingosNoMes(LocalDate data) {
        return contarDiasDaSemanaNoMes(data, DayOfWeek.SUNDAY);
    }

    /**
     * Método genérico para contar dias da semana
     */
    private int contarDiasDaSemanaNoMes(LocalDate data, DayOfWeek diaSemana) {
        YearMonth yearMonth = YearMonth.of(data.getYear(), data.getMonth());
        int totalDias = yearMonth.lengthOfMonth();
        int count = 0;

        for (int dia = 1; dia <= totalDias; dia++) {
            LocalDate date = yearMonth.atDay(dia);
            if (date.getDayOfWeek() == diaSemana) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calcula número de folgas baseado na quantidade de sábados no mês
     */
    public int calcularFolgasPorFuncionario(int numeroSabados) {
        // Regra: 6 folgas para meses com 5 sábados, 5 folgas para meses com 4 sábados
        return numeroSabados == 5 ? 6 : 5;
    }

}

