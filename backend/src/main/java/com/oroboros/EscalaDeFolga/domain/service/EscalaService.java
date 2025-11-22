package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaUpdateDTO;
import com.oroboros.EscalaDeFolga.domain.exception.EscalaNotFoundException;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.validation.escala.EscalaValidatorComposite;
import com.oroboros.EscalaDeFolga.domain.validation.escala.ValidaEscalaEditavel;
import com.oroboros.EscalaDeFolga.domain.validation.escala.ValidaExclusaoPossivel;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EscalaService {


    private final EscalaRepository escalaRepository;


    private final  ValidaEscalaEditavel validaEscalaEditavel;


    private final ValidaExclusaoPossivel validaExclusaoPossivel;

    private final EscalaValidatorComposite escalaValidatorComposite;


    public Escala criarEscala(Escala escala) {

        var validacao = escalaValidatorComposite.validar(escala);

        if (!validacao.isValido()) {
            throw new IllegalStateException(validacao.getMensagem());
        }

        return escalaRepository.save(escala);
    }

    public Escala buscarPorId(Long id) {
        return escalaRepository.findById(id)
                .orElseThrow(() -> new EscalaNotFoundException(id));
    }

    public Escala atualizarEscala(Long id, EscalaUpdateDTO dto) {

        Escala escala = escalaRepository.findById(id)
                .orElseThrow(() -> new EscalaNotFoundException(id));

        var validacao = validaEscalaEditavel.validar(escala);

        if (!validacao.isValido()) {
            throw new IllegalStateException(validacao.getMensagem());
        }
        if (dto.mes() != null) {
            escala.setMes(dto.mes());
        }
        if (dto.ano() != null) {
            escala.setAno(dto.ano());
        }
        if (dto.folgasPermitidas() != null) {
            escala.setFolgasPermitidas(dto.folgasPermitidas());
        }
        return escalaRepository.save(escala);
    }

    public void deletar(Long id) {

        Escala escala = escalaRepository.findById(id)
                .orElseThrow(() -> new EscalaNotFoundException(id));

        var validacao = validaExclusaoPossivel.validar(escala);

        if (!validacao.isValido()) {
            throw new IllegalStateException(validacao.getMensagem());
        }
        escalaRepository.delete(escala);
    }
}
