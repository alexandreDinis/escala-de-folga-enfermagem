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
          AND FUNCTION('EXTRACT', 'DOW', f.dataSolicitada) = 0
          AND FUNCTION('MONTH', f.dataSolicitada) = :mes
          AND FUNCTION('YEAR', f.dataSolicitada) = :ano
    """)
    boolean existsFolgaDomingoNoMes(
            @Param("colaborador") Colaborador colaborador,
            @Param("mes") int mes,
            @Param("ano") int ano
    );
}
