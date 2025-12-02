package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.alerta.Alerta;
import com.oroboros.EscalaDeFolga.domain.model.alerta.SeveridadeEnum;
import com.oroboros.EscalaDeFolga.domain.model.alerta.TipoAlertaEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    /**
     * Busca alertas não resolvidos de uma escala, ordenados por severidade
     */
    @Query("SELECT a FROM Alerta a " +
            "WHERE a.escala.id = :escalaId AND a.resolvido = false " +
            "ORDER BY a.severidade ASC, a.dataCriacao DESC")
    List<Alerta> findByEscalaIdAndResolvidoFalseOrderBySeveridadeAsc(
            @Param("escalaId") Long escalaId
    );

    /**
     * Busca alertas por severidade
     */
    List<Alerta> findByEscalaIdAndSeveridadeAndResolvidoFalse(
            Long escalaId,
            SeveridadeEnum severidade
    );

    /**
     * Busca alertas por tipo
     */
    List<Alerta> findByEscalaIdAndTipoAndResolvidoFalse(
            Long escalaId,
            TipoAlertaEnum tipo
    );

    /**
     * Conta alertas críticos pendentes de uma escala
     */
    @Query("SELECT COUNT(a) FROM Alerta a " +
            "WHERE a.escala.id = :escalaId " +
            "AND a.severidade = 'CRITICA' " +
            "AND a.resolvido = false")
    long countAlertasCriticosPendentes(@Param("escalaId") Long escalaId);

    /**
     * Busca alertas de uma folga específica
     */
    List<Alerta> findByFolgaIdOrderBySeveridadeAsc(Long folgaId);

    /**
     * Busca alertas de um colaborador na escala
     */
    List<Alerta> findByEscalaIdAndColaboradorIdAndResolvidoFalse(
            Long escalaId,
            Long colaboradorId
    );

    /**
     * Verifica se existe alerta em uma data específica
     */
    @Query("SELECT COUNT(a) > 0 FROM Alerta a " +
            "JOIN a.folga f " +
            "WHERE f.dataSolicitada = :data " +
            "AND a.escala = :escala " +
            "AND a.resolvido = false")
    boolean existsByDataAndEscala(
            @Param("data") LocalDate data,
            @Param("escala") Escala escala
    );

    /**
     * Busca alertas por data
     */
    @Query("SELECT a FROM Alerta a " +
            "JOIN a.folga f " +
            "WHERE f.dataSolicitada = :data " +
            "AND a.escala = :escala " +
            "AND a.resolvido = false")
    List<Alerta> findByDataAndEscala(
            @Param("data") LocalDate data,
            @Param("escala") Escala escala
    );
}
