package com.oroboros.EscalaDeFolga.repository;

import com.oroboros.EscalaDeFolga.model.Colaborador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {


    Page<Colaborador> findByAtivoTrue(Pageable pageable);
}

