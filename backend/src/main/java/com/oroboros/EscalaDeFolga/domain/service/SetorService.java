package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.app.dto.setor.SetorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.setor.SetorResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.setor.SetorUpdateDTO;
import com.oroboros.EscalaDeFolga.app.mapper.SetorMapper;
import com.oroboros.EscalaDeFolga.domain.exception.SetorNotFoundExeption;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.util.TextoNormalizerUtil;
import com.oroboros.EscalaDeFolga.domain.validation.setor.SetorValidadorComposite;
import com.oroboros.EscalaDeFolga.infrastructure.exeption.BusinessException;
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

    private final SetorValidadorComposite setorValidadorComposite;


    public SetorResponseDTO cadastrar(@Valid SetorRequestDTO dto) {
        Setor newSetor = setorMapper.toEntity(dto);

        var validacao = setorValidadorComposite.validar(newSetor);
        if (!validacao.isValido()){
            throw new BusinessException(validacao.getMensagem());
        }

        String nomeNormalizado = TextoNormalizerUtil.normalizar(dto.nome());

        boolean exite = setorRepository.existsByNomeNormalizado(nomeNormalizado);

        if (exite){
            throw new BusinessException("Já existe um setor semelhante: " + dto.nome());
        }

        return setorMapper.toResponse(setorRepository.save(newSetor));
    }

    public Setor buscarPorId(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(SetorNotFoundExeption::new);
    }

    public Page<SetorResponseDTO> listar(Pageable pageable) {
        return setorRepository.findByAtivoTrue(pageable).map(setorMapper::toResponse);
    }

    public SetorResponseDTO autalizar(Long id, SetorUpdateDTO dto) {
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
        setorRepository.save(setor);
    }
}
