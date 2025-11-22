package com.oroboros.EscalaDeFolga.domain.model.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
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

    @OneToMany(mappedBy = "colaborador", cascade = CascadeType.ALL)
    private List<Folga> folgas;

    @Column(name = "ultima_folga")
    private LocalDate ultimaFolga;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private Setor setor;


    public void delete(){
        this.ativo = false;
    }
}
