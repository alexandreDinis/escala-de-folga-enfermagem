package com.oroboros.EscalaDeFolga.app.controller;

import com.oroboros.EscalaDeFolga.app.dto.setor.SetorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.setor.SetorResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.setor.SetorUpdateDTO;
import com.oroboros.EscalaDeFolga.app.mapper.SetorMapper;
import com.oroboros.EscalaDeFolga.domain.service.SetorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/setor")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService setorService;
    private final SetorMapper setorMapper;


    @PostMapping
    public ResponseEntity<SetorResponseDTO>cadastrar(@Valid @RequestBody SetorRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.cadastrar(dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<SetorResponseDTO>buscarPorId(@PathVariable Long id){
        return  ResponseEntity.ok(setorMapper.toResponse(setorService.buscarPorId(id)));
    }


    @GetMapping
    public ResponseEntity<Page<SetorResponseDTO>>listar(Pageable pageable){
        return ResponseEntity.ok(setorService.listar(pageable));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SetorResponseDTO>atualizar(@PathVariable Long id, @RequestBody SetorUpdateDTO dto) {
        return ResponseEntity.ok(setorService.atualizar(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deletar(@PathVariable Long id){
        setorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
