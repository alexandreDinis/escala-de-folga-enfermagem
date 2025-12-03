package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {

    boolean existsByMesAndAnoAndTurnoAndSetorAndStatus(
            int mes, int ano, TurnoEnum turno, Setor setor, StatusEscalaEnum status);

    @Query("""
    SELECT e FROM Escala e
    WHERE 
        e.setor = :setor AND
        e.turno = :turno AND
        (
            (e.mes = :mes - 1 AND e.ano = :ano AND :mes > 1)
            OR (:mes = 1 AND e.mes = 12 AND e.ano = :ano - 1)
        )
    """)
    Optional<Escala> findEscalaAnterior(
            @Param("mes") int mes,
            @Param("ano") int ano,
            @Param("turno") TurnoEnum turno,
            @Param("setor") Setor setor
    );

    /**
     * Query customizada para busca com filtros dinâmicos
     */
    @Query("""
        SELECT e FROM Escala e
        WHERE 1=1
        AND (:setorId IS NULL OR e.setor.id = :setorId)
        AND (:turno IS NULL OR e.turno = :turno)
        AND (:status IS NULL OR e.status = :status)
        AND (:mes IS NULL OR e.mes = :mes)
        AND (:ano IS NULL OR e.ano = :ano)
        ORDER BY e.ano DESC, e.mes DESC
        """)
    Page<Escala> buscarComFiltros(
            @Param("setorId") Long setorId,
            @Param("turno") TurnoEnum turno,
            @Param("status") StatusEscalaEnum status,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano,
            Pageable pageable
    );
}


