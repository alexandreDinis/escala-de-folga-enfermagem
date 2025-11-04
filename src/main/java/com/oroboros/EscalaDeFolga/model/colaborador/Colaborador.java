package com.oroboros.EscalaDeFolga.model.colaborador;


import com.oroboros.EscalaDeFolga.dto.colaborador.ColaboradorInputDTO;
import com.oroboros.EscalaDeFolga.model.CargoEnum;
import com.oroboros.EscalaDeFolga.model.escala.Folga;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Enumerated(EnumType.STRING)
    private CargoEnum cargo;

    @Enumerated(EnumType.STRING)
    private TurnoEnum turno;

    boolean ativo = true;
    private int domingosFolgaNoMes;
    private LocalDate dataUltimoTrabalho;

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL)
    private List<Folga> folgas;


    // ✅ NOVO: Histórico para regra dos 6 dias
    @Transient // Não persiste, calcula na hora
    public LocalDate getUltimaFolga() {
        // Será calculado via service
        return null;
    }

    public Colaborador(ColaboradorInputDTO colaborador) {
        this.nome = colaborador.nome();
        this.cargo = colaborador.cargo();
        this.turno = colaborador.turno();
    }

    public void delete(){
        this.ativo = false;
    }
}
