package com.oroboros.EscalaDeFolga.repository;

import com.oroboros.EscalaDeFolga.model.escala.Folga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolgaRepository extends JpaRepository<Folga, Long> {

    // 
}
