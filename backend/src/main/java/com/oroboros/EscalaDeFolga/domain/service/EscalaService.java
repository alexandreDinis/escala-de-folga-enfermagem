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
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EscalaService {


    private final EscalaRepository escalaRepository;

    private final  ValidaEscalaEditavel validaEscalaEditavel;

    private final ValidaExclusaoPossivel validaExclusaoPossivel;

    private final EscalaValidatorComposite escalaValidatorComposite;

    private final ColaboradorService colaboradorService;

    private final SetorService setorService;

    private final FolgaService folgaService;


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
     * Verifica quais colaboradores precisam cadastrar histórico de folga
     */
    public Map<String, Object> verificarHistoricoFolgas(Long escalaId) {
        log.info("🔍 Verificando histórico de folgas para escala {}", escalaId);

        Escala escala = buscarPorId(escalaId);

        // Buscar todos os colaboradores do setor/turno
        List<Colaborador> colaboradores = colaboradorService
                .buscarPorSetorETurno(escala.getSetor(), escala.getTurno());

        log.info("📊 Total de colaboradores: {}", colaboradores.size());

        // ✅ Filtrar colaboradores que NÃO têm histórico válido
        // Passa a escala como parâmetro
        List<Colaborador> colaboradoresSemHistorico = colaboradores.stream()
                .filter(c -> !folgaService.temHistoricoValido(c.getId(), escala))
                .toList();

        boolean faltaHistorico = !colaboradoresSemHistorico.isEmpty();

        log.info("⚠️ Colaboradores sem histórico válido: {}", colaboradoresSemHistorico.size());

        return Map.of(
                "faltaHistorico", faltaHistorico,
                "colaboradoresSemHistorico", colaboradoresSemHistorico,
                "totalSemHistorico", colaboradoresSemHistorico.size(),
                "totalColaboradores", colaboradores.size()
        );
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
