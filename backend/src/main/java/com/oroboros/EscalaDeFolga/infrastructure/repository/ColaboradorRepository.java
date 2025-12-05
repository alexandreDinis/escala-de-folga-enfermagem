package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.CargoEnum;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    List<Colaborador> findBySetorAndTurno(Setor setor, TurnoEnum turno);

    long countBySetorAndTurno(Setor setor, TurnoEnum turno);

    @Query("""
        SELECT c FROM Colaborador c
        WHERE c.ativo = true
        AND (:search IS NULL OR LOWER(c.nomeNormalizado) LIKE LOWER(CONCAT('%', :search, '%')))
        AND (:setorId IS NULL OR c.setor.id = :setorId)
        AND (:turno IS NULL OR c.turno = :turno)
        AND (:cargo IS NULL OR c.cargo = :cargo)
        """)
    Page<Colaborador> buscarComFiltros(
            @Param("search") String search,
            @Param("setorId") Long setorId,
            @Param("turno") TurnoEnum turno,
            @Param("cargo") CargoEnum cargo,
            Pageable pageable
    );

    /**
     * Busca colaboradores sem histórico de última folga em um setor e turno específicos
     */
    List<Colaborador> findBySetorAndTurnoAndUltimaFolgaNull(Setor setor, TurnoEnum turno);

    /**
     * Conta total de colaboradores ativos em um setor e turno
     */
    long countBySetorAndTurnoAndAtivoTrue(Setor setor, TurnoEnum turno);

    List<Colaborador> findBySetorAndTurnoAndAtivoTrue(Setor setor, TurnoEnum turno);


}

