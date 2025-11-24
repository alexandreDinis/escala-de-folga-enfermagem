package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {


    Page<Colaborador> findByAtivoTrue(Pageable pageable);

    boolean existsBySetorAndTurnoAndUltimaFolgaNull(Setor setor, TurnoEnum turno);

    /**
     * Verifica se existe colaborador com nome normalizado no mesmo setor e turno,
     * excluindo um ID específico. Útil para validação em updates.
     */
    boolean existsByNomeNormalizadoAndSetorAndTurnoAndIdNot(
            String nomeNormalizado,
            Setor setor,
            TurnoEnum turno,
            Long id
    );

    /**
     * Busca colaborador por nome normalizado, setor e turno.
     */
    Optional<Colaborador> findByNomeNormalizadoAndSetorAndTurno(
            String nomeNormalizado,
            Setor setor,
            TurnoEnum turno
    );
}

