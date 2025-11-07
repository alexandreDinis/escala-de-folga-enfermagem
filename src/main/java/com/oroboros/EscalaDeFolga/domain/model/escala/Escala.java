package com.oroboros.EscalaDeFolga.domain.model.escala;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Escala {
    @Id @GeneratedValue
    private Long id;

    private int mes;
    private int ano;

    @OneToMany(mappedBy = "escala", cascade = CascadeType.ALL)
    private List<EscalaColaborador> registros;

    @OneToMany(mappedBy = "escala", cascade = CascadeType.ALL)
    private List<Folga> folgas;

}
