package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {

    boolean existsByMesAndAnoAndTurnoAndSetorAndStatus(
            int mes, int ano, TurnoEnum turno, Setor setor, StatusEscalaEnum status);

}
