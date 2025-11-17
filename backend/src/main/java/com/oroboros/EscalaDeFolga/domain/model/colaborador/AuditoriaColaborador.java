package com.oroboros.EscalaDeFolga.domain.model.colaborador;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditoriaColaborador {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID do colaborador afetado
    @Column(nullable = false)
    private Long colaboradorId;

    // Tipo da ação
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AcaoAuditoriaEnum acao;

    // Quem executou a ação
    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 150)
    private String usuarioNome;

    // Quando ocorreu
    @Column(nullable = false)
    private LocalDateTime dataHora;

    // Snapshots armazenados como texto JSON (compatível com qualquer banco)
    @Lob
    @Column(columnDefinition = "TEXT")
    private String dadosAnteriores;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String dadosNovos;

    // Metadados opcionais
    @Column(length = 50)
    private String ipOrigem;

    @Column(length = 500)
    private String userAgent;


    public static AuditoriaColaborador criar(
            AcaoAuditoriaEnum acao,
            Long colaboradorId,
            Long usuarioId,
            String usuarioNome,
            String dadosAnteriores,
            String dadosNovos,
            String ipOrigem,
            String userAgent
    ){
        return  AuditoriaColaborador.builder()
                .colaboradorId(colaboradorId)
                .acao(acao)
                .usuarioId(usuarioId)
                .usuarioNome(usuarioNome)
                .dadosAnteriores(dadosAnteriores)
                .dadosNovos(dadosNovos)
                .ipOrigem(ipOrigem)
                .userAgent(userAgent)
                .dataHora(LocalDateTime.now())
                .build();
    }

}
