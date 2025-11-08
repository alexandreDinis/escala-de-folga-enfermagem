package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FolgaRepository extends JpaRepository<Folga, Long> {

    boolean existsByColaboradorAndDataSolicitada(Colaborador colaborador, LocalDate dataSolicitada);

    // 
}
