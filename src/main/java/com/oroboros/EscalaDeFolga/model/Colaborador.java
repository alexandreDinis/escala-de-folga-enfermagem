package com.oroboros.EscalaDeFolga.model;


import com.oroboros.EscalaDeFolga.dto.ColaboradorInputDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
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
    private  CargoEnum cargo;

    @Enumerated(EnumType.STRING)
    private TurnoEnum turno;

    boolean ativo = true;


    public Colaborador(ColaboradorInputDTO colaborador) {
        this.nome = colaborador.nome();
        this.cargo = colaborador.cargo();
        this.turno = colaborador.turno();
    }

    public void delete(){
        this.ativo = false;
    }
}
