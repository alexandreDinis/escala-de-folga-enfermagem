package com.oroboros.EscalaDeFolga.infrastructure.repository;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.AuditoriaColaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaColaboradorRepository extends JpaRepository<AuditoriaColaborador, Long> {

    // Buscar auditorias de um colaborador específico
    List<AuditoriaColaborador> findByColaboradorIdOrderByDataHoraDesc(Long colaboradorId);

}
