package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {
}
