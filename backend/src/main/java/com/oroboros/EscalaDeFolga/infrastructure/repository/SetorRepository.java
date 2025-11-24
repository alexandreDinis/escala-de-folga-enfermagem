package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {

    Page<Setor> findByAtivoTrue(Pageable pageable);

    Optional<Setor> findByNomeNormalizadoAndIdNot(String nomeNormalizado, Long id);

    boolean existsByNomeNormalizado(String nomeNormalizado);

    List<Setor> findByNomeNormalizadoContaining(String nomeNormalizado);
}
