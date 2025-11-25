package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.app.dto.setor.SetorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.setor.SetorResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.setor.SetorUpdateDTO;
import com.oroboros.EscalaDeFolga.app.mapper.SetorMapper;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.util.TextoNormalizerUtil;
import com.oroboros.EscalaDeFolga.domain.validation.setor.SetorValidadorComposite;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.infrastructure.repository.SetorRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SetorService {


    private final SetorMapper setorMapper;

    private final SetorRepository setorRepository;

    private final SetorValidadorComposite setorValidadorComposite;


    public SetorResponseDTO cadastrar(@Valid SetorRequestDTO dto) {

        Setor newSetor = setorMapper.toEntity(dto);

        String nomeNormalizado = TextoNormalizerUtil.normalizar(dto.nome());

        Optional<Setor> setorInativo = setorRepository.findByNomeNormalizado(nomeNormalizado);

        if (setorInativo.isPresent() && !setorInativo.get().isAtivo()) {
            // Se existe inativo e o cliente não confirmou reativação, lança exceção
            if (dto.confirmarReativacao() == null || !dto.confirmarReativacao()) {
                throw new BusinessException(
                        String.format("Já existe um setor semelhante: '%s'. Deseja reativá-lo?", dto.nome())
                );
            }

        var validacao = setorValidadorComposite.validar(newSetor);
        if (!validacao.isValido()){
            throw new BusinessException(validacao.getMensagem());
        }
            // Cliente confirmou reativação - reativa o setor existente
            Setor setorParaReativar = setorInativo.get();
            setorParaReativar.setAtivo(true);

            return setorMapper.toResponse(setorRepository.save(setorParaReativar));
        }
        // Valida se já existe setor ATIVO com mesmo nome (não pode cadastrar/reativar)
        var validacao = setorValidadorComposite.validar(newSetor);

        if (!validacao.isValido()) {
            throw new BusinessException(validacao.getMensagem());
        }
        // Se passou por todas as validações, cadastra novo setor
        newSetor.setAtivo(true);
        return setorMapper.toResponse(setorRepository.save(newSetor));
    }


    public Setor buscarPorId(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Setor", id));
    }


    public Page<SetorResponseDTO> listar(Pageable pageable) {
        return setorRepository.findByAtivoTrue(pageable).map(setorMapper::toResponse);
    }


    public SetorResponseDTO autalizar(Long id, SetorUpdateDTO dto) {
        Setor setorExistente = setorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Setor", id));

        if (dto.nome() != null) {

            setorExistente.setNome(dto.nome());

            String nomeNormalizado = TextoNormalizerUtil.normalizar(dto.nome());

            Optional<Setor> setorDuplicado = setorRepository.findByNomeNormalizado(nomeNormalizado);

            if (setorDuplicado.isPresent() && !setorDuplicado.get().getId().equals(id)) {

                if (!setorDuplicado.get().isAtivo()) {
                    if (dto.confirmarReativacao() != null && dto.confirmarReativacao()) {
                        setorDuplicado.get().setAtivo(true);
                        return setorMapper.toResponse(setorRepository.save(setorDuplicado.get()));
                    }

                    throw new BusinessException(
                            String.format("Já existe um setor com nome similar a '%s' desativado. Deseja reativá-lo?"
                                    , dto.nome())
                    );
                }

                throw new BusinessException(
                        String.format("Já existe um setor ativo com nome parecido: '%s'"
                                , dto.nome())
                );
            }
        }

        return setorMapper.toResponse(setorRepository.save(setorExistente));
    }


    public void deletar(Long id) {
        Setor setor = setorRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Setor", id));
        setor.deletar();
        setorRepository.save(setor);
    }
}
