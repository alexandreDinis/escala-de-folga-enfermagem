package com.oroboros.EscalaDeFolga.repository;

import com.oroboros.EscalaDeFolga.model.Escala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EscalaRepository extends JpaRepository<Escala, Long> {
    // Consultas customizadas:
    // List<Escala> findByMes(int mes);
}
