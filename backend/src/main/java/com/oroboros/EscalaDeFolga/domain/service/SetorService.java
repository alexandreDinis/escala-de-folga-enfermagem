package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.app.dto.escala.SetorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.SetorResposnseDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.SetorUpdateDTO;
import com.oroboros.EscalaDeFolga.app.mapper.SetorMapper;
import com.oroboros.EscalaDeFolga.domain.exception.SetorNotFoundExeption;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.infrastructure.repository.SetorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class SetorService {


    private final SetorMapper setorMapper;

    private final SetorRepository setorRepository;


    public SetorResposnseDTO cadastrar(@Valid SetorRequestDTO dto) {
        Setor newSetor = setorMapper.toEntity(dto);
        return setorMapper.toResponse(setorRepository.save(newSetor));
    }

    public SetorResposnseDTO buscarPorId(Long id) {
        return setorMapper.toResponse(setorRepository.findById(id)
                .orElseThrow(SetorNotFoundExeption::new));
    }

    public Page<SetorResposnseDTO> listar(Pageable pageable) {
        return setorRepository.findByAtivoTrue(pageable).map(setorMapper::toResponse);
    }

    public SetorResposnseDTO autalizar(Long id, SetorUpdateDTO dto) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(SetorNotFoundExeption::new);

        if (dto.nome() != null){
            setor.setNome(dto.nome());
        }
        return setorMapper.toResponse(setorRepository.save((setor)));

    }

    public void deletar(Long id) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(SetorNotFoundExeption::new);
        setor.deletar();
    }
}
