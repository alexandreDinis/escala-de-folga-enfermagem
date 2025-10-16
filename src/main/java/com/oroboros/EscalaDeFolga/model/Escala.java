package com.oroboros.EscalaDeFolga.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
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

    // getters e setters
}
