package com.oroboros.EscalaDeFolga.domain.service;

import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaUpdateDTO;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.domain.validation.ResultadoValidacao;
import com.oroboros.EscalaDeFolga.domain.validation.escala.EscalaValidatorComposite;
import com.oroboros.EscalaDeFolga.domain.validation.escala.ValidaEscalaEditavel;
import com.oroboros.EscalaDeFolga.domain.validation.escala.ValidaExclusaoPossivel;
import com.oroboros.EscalaDeFolga.domain.exception.BusinessException;
import com.oroboros.EscalaDeFolga.infrastructure.repository.EscalaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EscalaService {


    private final EscalaRepository escalaRepository;

    private final  ValidaEscalaEditavel validaEscalaEditavel;

    private final ValidaExclusaoPossivel validaExclusaoPossivel;

    private final EscalaValidatorComposite escalaValidatorComposite;

    private final ColaboradorService colaboradorService;

    private final SetorService setorService;


    @Transactional
    public Escala criarEscala(Escala escala) {


        ResultadoValidacao validacao = escalaValidatorComposite.validar(escala);

        if (!validacao.isValido()) {
            throw new BusinessException(validacao.getMensagem());
        }

        Long setorId = escala.getSetor().getId();
        Setor setorGerenciado = setorService.buscarPorId(setorId);

        escala.setSetor(setorGerenciado);

        return escalaRepository.save(escala);
    }


    public Escala buscarPorId(Long id) {
        return escalaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Escala", id));
    }

    public Escala atualizarEscala(Long id, EscalaUpdateDTO dto) {

        Escala escala = escalaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Escala", id));

        var validacao = validaEscalaEditavel.validar(escala);

        if (!validacao.isValido()) {
            throw new BusinessException(validacao.getMensagem());
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


    public Page<Escala> listar(
            Pageable pageable,
            Long setorId,
            TurnoEnum turno,
            StatusEscalaEnum status,
            Integer mes,
            Integer ano
    ) {
        // Se não tem filtros, busca todas
        if (setorId == null && turno == null && status == null && mes == null && ano == null) {
            return escalaRepository.findAll(pageable);
        }

        // Se tem filtros, usa query customizada
        return escalaRepository.buscarComFiltros(setorId, turno, status, mes, ano, pageable);
    }


    /**
     * Verifica histórico de folgas e retorna colaboradores sem histórico
     *
     * @param escalaId ID da escala
     * @return Map com dados brutos (colaboradores e totais)
     */
    public Map<String, Object> verificarHistoricoFolgas(Long escalaId) {
        Escala escala = escalaRepository.findById(escalaId)
                .orElseThrow(() -> new BusinessException("Escala", escalaId));

        // Buscar colaboradores sem histórico
        List<Colaborador> colaboradoresSemHistorico = colaboradorService
                .buscarColaboradoresSemHistorico(escala.getSetor(), escala.getTurno());

        // Contar total de colaboradores
        long totalColaboradores = colaboradorService
                .contarColaboradores(escala.getSetor(), escala.getTurno());

        // Retornar dados brutos (Controller fará a conversão)
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("faltaHistorico", !colaboradoresSemHistorico.isEmpty());
        resultado.put("colaboradoresSemHistorico", colaboradoresSemHistorico);
        resultado.put("totalSemHistorico", colaboradoresSemHistorico.size());
        resultado.put("totalColaboradores", (int) totalColaboradores);

        return resultado;
    }


    public void deletar(Long id) {

        Escala escala = escalaRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Escala", id));

        var validacao = validaExclusaoPossivel.validar(escala);

        if (!validacao.isValido()) {
            throw new BusinessException(validacao.getMensagem());
        }
        escalaRepository.delete(escala);
    }
}
