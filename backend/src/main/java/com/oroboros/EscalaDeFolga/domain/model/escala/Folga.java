package com.oroboros.EscalaDeFolga.domain.model.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Folga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Colaborador colaborador;

    private LocalDate dataSolicitada;

    private String justificativa;

    @Enumerated(EnumType.STRING)
    private StatusFolgaEnum status; // PENDENTE, APROVADA, NEGADA

    @ManyToOne
    private Escala escala; // opcional, vincula ao mês correspondente
}
