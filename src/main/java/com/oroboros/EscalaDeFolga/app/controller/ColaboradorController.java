package com.oroboros.EscalaDeFolga.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.AuditoriaInfoDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorInputDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorResponseDTO;
import com.oroboros.EscalaDeFolga.app.dto.colaborador.ColaboradorUpdateDTO;
import com.oroboros.EscalaDeFolga.domain.service.ColaboradorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("colaborador")
@SecurityRequirement(name = "bearer-key")
public class ColaboradorController {

    @Autowired
    private ColaboradorService colaboradorService;

    @PostMapping
    public ResponseEntity<ColaboradorResponseDTO> cadastrar (@RequestBody @Valid ColaboradorInputDTO colaborador) throws JsonProcessingException {

        // 🔹 Para testes, dados fictícios
        //todo Mudar após a implementação de segurança
        AuditoriaInfoDTO auditor = new AuditoriaInfoDTO(
                0L,          // usuarioId fictício
                "Sistema",   // usuarioNome fictício
                null,        // ipOrigem
                null         // userAgent
        );
        ColaboradorResponseDTO  colaboradorResponse  = colaboradorService.cadastrar(colaborador, auditor);
        return ResponseEntity.ok().body(colaboradorResponse);
    }

    @GetMapping
    public Page<ColaboradorResponseDTO> listar ( @ParameterObject
                                                     @PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 10) Pageable pageable) {
        return colaboradorService.listar(pageable);
    }

    @GetMapping("{id}")
    public ResponseEntity<ColaboradorResponseDTO> buscarPorId(@PathVariable Long id){
        var colaborador = colaboradorService.buscarPorId(id);
        return ResponseEntity.ok(colaborador);
    }




    @DeleteMapping("{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {

        // 🔹 Para testes, dados fictícios
        //todo Mudar após a implementação de segurança
        AuditoriaInfoDTO auditor = new AuditoriaInfoDTO(
                0L,          // usuarioId fictício
                "Sistema",   // usuarioNome fictício
                null,        // ipOrigem
                null         // userAgent
        );

        colaboradorService.inativar(id,  auditor);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("{id}")
    public ResponseEntity<ColaboradorResponseDTO>atualizar(@PathVariable Long id, @RequestBody ColaboradorUpdateDTO colaboradorUpdateDTO){

        // 🔹 Para testes, dados fictícios
        //todo Mudar após a implementação de segurança
        AuditoriaInfoDTO auditor = new AuditoriaInfoDTO(
                0L,          // usuarioId fictício
                "Sistema",   // usuarioNome fictício
                null,        // ipOrigem
                null         // userAgent
        );

        ColaboradorResponseDTO colaboradorResponseDTO = colaboradorService.atualizar(id, auditor, colaboradorUpdateDTO);
        return ResponseEntity.ok().body(colaboradorResponseDTO);
    }

}













