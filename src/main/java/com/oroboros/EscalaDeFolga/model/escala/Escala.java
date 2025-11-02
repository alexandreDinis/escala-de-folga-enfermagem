package com.oroboros.EscalaDeFolga.model.escala;

import com.oroboros.EscalaDeFolga.model.escala.EscalaColaborador;
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

}
