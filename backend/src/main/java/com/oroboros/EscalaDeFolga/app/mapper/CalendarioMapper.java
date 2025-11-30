package com.oroboros.EscalaDeFolga.app.mapper;

import com.oroboros.EscalaDeFolga.app.dto.calendario.*;
import com.oroboros.EscalaDeFolga.domain.service.CalendarioService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.format.TextStyle;
import java.util.Locale;

/**
 * Mapper para conversão de Domain de Calendário para DTOs
 */
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {FolgaMapper.class} // Usa FolgaMapper para mapear folgas
)
public interface CalendarioMapper {

    /**
     * Mapeia CalendarioDomain completo para DTO
     */
    @Mapping(target = "escalaId", source = "escala.id")
    @Mapping(target = "mes", source = "escala.mes")
    @Mapping(target = "ano", source = "escala.ano")
    @Mapping(target = "mesNome", expression = "java(getMesNome(domain.escala().getMes()))")
    @Mapping(target = "turno", source = "escala.turno")
    @Mapping(target = "setorNome", source = "escala.setor.nome")
    @Mapping(target = "dias", source = "dias")
    @Mapping(target = "colaboradores", source = "colaboradores")
    @Mapping(target = "resumo", source = "resumo")
    @Mapping(target = "configuracao", source = "configuracao")
    CalendarioResponseDTO toResponse(CalendarioService.CalendarioDomain domain);

    /**
     * Mapeia DiaDomain para DTO
     */
    @Mapping(target = "dia", expression = "java(dia.data().getDayOfMonth())")
    @Mapping(target = "data", source = "data")
    @Mapping(target = "diaSemana", expression = "java(getDiaSemana(dia.data()))")
    @Mapping(target = "status", source = "status.nome")
    @Mapping(target = "corStatus", source = "status.cor")
    @Mapping(target = "clicavel", source = "status.clicavel")
    @Mapping(target = "motivoNaoClicavel", source = "status.motivo")
    @Mapping(target = "totalFolgasNoDia", source = "totalFolgasNoDia")
    @Mapping(target = "colaboradoresDisponiveis", source = "colaboradoresDisponiveis")
    @Mapping(target = "limiteMaximoFolgas", source = "limiteMaximoFolgas")
    @Mapping(target = "folgasNoDia", source = "folgasNoDia")
    CalendarioDiaDTO toDiaDTO(CalendarioService.DiaDomain dia);

    /**
     * Mapeia Folga para FolgaResumoDiaDTO
     */
    @Mapping(target = "folgaId", source = "id")
    @Mapping(target = "colaboradorId", source = "colaborador.id")
    @Mapping(target = "colaboradorNome", source = "colaborador.nome")
    @Mapping(target = "status", expression = "java(folga.getStatus().name())")
    FolgaResumoDiaDTO toFolgaResumoDTO(com.oroboros.EscalaDeFolga.domain.model.escala.Folga folga);

    /**
     * Mapeia ColaboradorHistoricoDomain para DTO
     */
    @Mapping(target = "id", source = "colaborador.id")
    @Mapping(target = "nome", source = "colaborador.nome")
    @Mapping(target = "turno", expression = "java(colab.colaborador().getTurno().name())")
    @Mapping(target = "ultimaFolga", source = "ultimaFolga")
    @Mapping(target = "diasDesdeUltimaFolga", source = "diasDesdeUltimaFolga")
    @Mapping(target = "proximaDataDisponivel", source = "proximaDataDisponivel")
    @Mapping(target = "totalFolgasNoMes", source = "totalFolgasNoMes")
    @Mapping(target = "folgasRestantes", source = "folgasRestantes")
    @Mapping(target = "datasComFolga", source = "datasComFolga")
    @Mapping(target = "temDomingo", source = "temDomingo")
    @Mapping(target = "emRisco", source = "emRisco")
    @Mapping(target = "alertas", source = "alertas")
    @Mapping(target = "podeFolgarHoje", source = "podeFolgarHoje")
    @Mapping(target = "corStatus", source = "corStatus")
    ColaboradorCalendarioDTO toColaboradorDTO(CalendarioService.ColaboradorHistoricoDomain colab);

    /**
     * Mapeia ResumoCalendarioDomain para DTO (mapeamento direto)
     */
    ResumoCalendarioDTO toResumoDTO(CalendarioService.ResumoCalendarioDomain resumo);

    /**
     * Mapeia ConfiguracaoCalendarioDomain para DTO (mapeamento direto)
     */
    ConfiguracaoCalendarioDTO toConfiguracaoDTO(CalendarioService.ConfiguracaoCalendarioDomain config);

    /**
     * Helpers para formatação
     */
    default String getMesNome(int mes) {
        // 🔍 LOG: Identificar quem está chamando com mês inválido
        if (mes < 1 || mes > 12) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            System.err.println("❌ ERRO: getMesNome chamado com mês inválido: " + mes);
            System.err.println("📍 Stack trace:");
            for (int i = 0; i < Math.min(10, stackTrace.length); i++) {
                System.err.println("  " + stackTrace[i]);
            }
            throw new IllegalArgumentException(
                    String.format("Mês inválido: %d (esperado: 1-12). " +
                            "Verifique logs para identificar origem.", mes)
            );
        }

        return java.time.Month.of(mes)
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
    }

    default String getDiaSemana(java.time.LocalDate data) {
        return data.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
    }
}
