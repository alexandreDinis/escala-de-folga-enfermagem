package com.oroboros.EscalaDeFolga.app.controller;


import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaUpdateDTO;
import com.oroboros.EscalaDeFolga.app.mapper.EscalaMapper;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.StatusEscalaEnum;
import com.oroboros.EscalaDeFolga.domain.service.EscalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/escala")
@RequiredArgsConstructor
public class EscalaController {

    private final EscalaService escalaService;
    private final EscalaMapper escalaMapper;


    @PostMapping
    public ResponseEntity<EscalaResponseDTO> criarEscala(@Valid @RequestBody EscalaRequestDTO request) {

        Escala escala = escalaMapper.toEntity(request);
        Escala criada = escalaService.criarEscala(escala);

        return ResponseEntity.status(HttpStatus.CREATED).body(escalaMapper.toResponse(criada));
    }


    @GetMapping("/{id}")
    public ResponseEntity<EscalaResponseDTO> buscarPorId(@PathVariable Long id) {

        Escala escala = escalaService.buscarPorId(id);

        return ResponseEntity.ok(escalaMapper.toResponse(escala));
    }


    @PutMapping("{id}")
    public ResponseEntity<EscalaResponseDTO> atualizarEscala(@PathVariable Long id,
                                                             @RequestBody EscalaUpdateDTO dto){

        return ResponseEntity.ok(escalaMapper.toResponse(escalaService.atualizarEscala(id, dto)));

    }


    @GetMapping
    public Page<EscalaResponseDTO> listar(
            @ParameterObject
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable,
            @RequestParam(required = false) Long setorId,
            @RequestParam(required = false) TurnoEnum turno,
            @RequestParam(required = false) StatusEscalaEnum status,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano
    ) {
        return escalaService.listar(pageable, setorId, turno, status, mes, ano)
                .map(escalaMapper::toResponse);
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        escalaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
