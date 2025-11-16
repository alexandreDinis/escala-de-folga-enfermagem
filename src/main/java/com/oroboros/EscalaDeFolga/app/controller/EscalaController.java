package com.oroboros.EscalaDeFolga.app.controller;


import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.EscalaResponseDTO;
import com.oroboros.EscalaDeFolga.app.mapper.EscalaMapper;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.service.EscalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

        return ResponseEntity.ok(escalaMapper.toResponse(criada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EscalaResponseDTO> buscarPorId(@PathVariable Long id) {

        Escala escala = escalaService.buscarPorId(id);

        return ResponseEntity.ok(escalaMapper.toResponse(escala));
    }
}
