package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusFolgaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FolgaRepository extends JpaRepository<Folga, Long> {

    boolean existsByColaboradorAndDataSolicitada(Colaborador colaborador, LocalDate dataSolicitada);

    long countByColaboradorAndEscalaAndStatusIn(Colaborador colaborador, Escala escala, List<StatusFolgaEnum> status);

    List<Folga> findByEscalaAndStatus(Escala escala, StatusFolgaEnum status);

    @Query("""
        SELECT COUNT(f) > 0
        FROM Folga f
        WHERE f.colaborador = :colaborador
          AND f.status IN ('PENDENTE', 'APROVADA')
          AND FUNCTION('DAYOFWEEK',  f.dataSolicitada) = 1
          AND FUNCTION('MONTH', f.dataSolicitada) = :mes
          AND FUNCTION('YEAR', f.dataSolicitada) = :ano
    """)
    boolean existsFolgaDomingoNoMes(
            @Param("colaborador") Colaborador colaborador,
            @Param("mes") int mes,
            @Param("ano") int ano
    );

    @Query("""
    SELECT MAX(f.dataSolicitada)
    FROM Folga f
    WHERE f.colaborador = :colaborador
      AND f.dataSolicitada < :data
      AND f.status IN ('PENDENTE', 'APROVADA')
""")
    Optional<LocalDate> findUltimaFolgaAntesDe(
            @Param("colaborador") Colaborador colaborador,
            @Param("data") LocalDate data
    );

    List<Folga> findByColaboradorAndEscalaAndStatusIn(
            Colaborador colaborador,
            Escala escala,
            List<StatusFolgaEnum> status
    );

    // Busca folgas de uma data específica
    List<Folga> findByDataSolicitadaAndEscala(LocalDate data, Escala escala);

    // Conta folgas em uma data com status específicos
    @Query("SELECT COUNT(f) FROM Folga f WHERE f.dataSolicitada = :data " +
            "AND f.status IN :status")
    long countByDataSolicitadaAndStatusIn(
            @Param("data") LocalDate data,
            @Param("status") List<StatusFolgaEnum> status
    );

    /**
     * Conta folgas em uma data específica com status filtrados
     */
    @Query("SELECT COUNT(f) FROM Folga f " +
            "WHERE f.escala = :escala " +
            "AND f.dataSolicitada = :data " +
            "AND f.status IN :status")
    long countByEscalaAndDataSolicitadaAndStatusIn(
            @Param("escala") Escala escala,
            @Param("data") LocalDate data,
            @Param("status") List<StatusFolgaEnum> status
    );
}
