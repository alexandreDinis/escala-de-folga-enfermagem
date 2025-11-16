package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EscalaService {

    @Autowired
    private  EscalaRepository escalaRepository;


    public Escala criarEscala(Escala escala) {
        return escalaRepository.save(escala);
    }

    public Escala buscarPorId(Long id){
        return escalaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException( "Escala não encontrada."));
    }
}
