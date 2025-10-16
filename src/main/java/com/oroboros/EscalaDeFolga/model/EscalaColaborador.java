package com.oroboros.EscalaDeFolga.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class EscalaColaborador {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Escala escala;

    @ManyToOne
    private Colaborador colaborador;

    private LocalDate data; // cada registro é um dia

    @Enumerated(EnumType.STRING)
    private TurnoEnum turno;

}
