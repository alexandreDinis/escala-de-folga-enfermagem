package com.oroboros.EscalaDeFolga.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
public class EscalaRegrasService {

    public static final int MAX_DIAS_PARA_FOLGA = 7;
    public static final int DIAS_TRABALHO_ANTES_FOLGA = 0;

    //TALVEZ NAO USE
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

    // NAO SERA NECESSARIO

    /**
     * Calcula número de folgas baseado na quantidade de sábados no mês
     */
    public int calcularFolgasPorFuncionario(int numeroSabados) {
        // Regra: 6 folgas para meses com 5 sábados, 5 folgas para meses com 4 sábados
        return numeroSabados == 5 ? 6 : 5;
    }

    /**
     * cria metodo que conta quantos colaboradores tem, verifica quantas folgas serao dadas por dia no mês
     * e retorna a media de colaboradores que podem folgar no dia.
     * <p>
     * 1 - verificar quantos domingos
     * 2- verificar quantos colaboradores
     * 3- dividir colaboradores por domingo
     * 4 - resto da divisao e divide pela diferença da quantidade de dias restante para obter quantas pessoas podem folgar no dia.
     */


    public int calculaMediaDeFolgaPorDia(
            int totalDiasNoMes,
            int totalColaboradores,
            int qtdDomingosNoMes,
            DayOfWeek dia,
            int folgasPorColaborador) {

        if (totalDiasNoMes <= 0 || totalColaboradores <= 0 || folgasPorColaborador <= 0) {
            return 0;
        }

        int totalFolgas = totalColaboradores * folgasPorColaborador;

        int folgasPorDomingo = qtdDomingosNoMes > 0
                ? (int) Math.ceil((double) totalColaboradores / qtdDomingosNoMes) // se você quer colaboradores por domingo
                : 0;

        int totalFolgasAlocadasDomingos = folgasPorDomingo * qtdDomingosNoMes;

        int restanteFolgas = totalFolgas - totalFolgasAlocadasDomingos;
        if (restanteFolgas < 0) restanteFolgas = 0;

        int diasNaoDomingo = totalDiasNoMes - qtdDomingosNoMes;
        int folgasPorDiaDaSemana = diasNaoDomingo > 0
                ? (int) Math.ceil((double) restanteFolgas / diasNaoDomingo)
                : 0;

        return dia == DayOfWeek.SUNDAY ? folgasPorDomingo : folgasPorDiaDaSemana;
    }
}


