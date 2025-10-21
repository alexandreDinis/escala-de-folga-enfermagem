package com.oroboros.EscalaDeFolga.repository;

import com.oroboros.EscalaDeFolga.model.colaborador.AuditoriaColaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaColaboradorRepository extends JpaRepository<AuditoriaColaborador, Long> {

    // Buscar auditorias de um colaborador específico
    List<AuditoriaColaborador> findByColaboradorIdOrderByDataHoraDesc(Long colaboradorId);

}
