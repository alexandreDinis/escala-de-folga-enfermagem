package com.oroboros.EscalaDeFolga.app.controller;

import com.oroboros.EscalaDeFolga.app.dto.escala.SetorRequestDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.SetorResposnseDTO;
import com.oroboros.EscalaDeFolga.app.dto.escala.SetorUpdateDTO;
import com.oroboros.EscalaDeFolga.domain.service.SetorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/setor")
@RequiredArgsConstructor
public class SetorController {

    private final SetorService setorService;

    @PostMapping
    public ResponseEntity<SetorResposnseDTO>cadastrar(@Valid @RequestBody SetorRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(setorService.cadastrar(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SetorResposnseDTO>buscarPorId(@PathVariable Long id){
        return  ResponseEntity.ok(setorService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<SetorResposnseDTO>>listar(Pageable pageable){
        return ResponseEntity.ok(setorService.listar(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SetorResposnseDTO>autalizar(@PathVariable Long id, @RequestBody SetorUpdateDTO dto) {
        return ResponseEntity.ok(setorService.autalizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>deletar(@PathVariable Long id){
        setorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
