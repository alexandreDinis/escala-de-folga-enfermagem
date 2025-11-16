package com.oroboros.EscalaDeFolga.domain.model.escala;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.colaborador.TurnoEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;


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

    private LocalDate diaDaEscala;

    @CreatedDate
    private LocalDate criadoEm;

    @Enumerated(EnumType.STRING)
    private TurnoEnum turno;
}
