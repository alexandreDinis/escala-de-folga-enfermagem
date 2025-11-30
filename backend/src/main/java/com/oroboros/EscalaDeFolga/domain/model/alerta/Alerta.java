package com.oroboros.EscalaDeFolga.domain.model.alerta;

import com.oroboros.EscalaDeFolga.domain.model.colaborador.Colaborador;
import com.oroboros.EscalaDeFolga.domain.model.escala.Escala;
import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa alertas gerados durante criação de folgas
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "alertas", indexes = {
        @Index(name = "idx_escala_colaborador", columnList = "escala_id, colaborador_id"),
        @Index(name = "idx_severidade", columnList = "severidade"),
        @Index(name = "idx_data_criacao", columnList = "data_criacao"),
        @Index(name = "idx_resolvido", columnList = "resolvido")
})
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escala_id", nullable = false)
    private Escala escala;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folga_id")
    private Folga folga; // Pode ser null em alertas gerais da escala

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoAlertaEnum tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SeveridadeEnum severidade;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensagem;

    @Column(columnDefinition = "TEXT")
    private String recomendacao;

    @Column(name = "dados_adicionais", columnDefinition = "TEXT")
    private String dadosAdicionais; // JSON com dados contextuais

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @Column(nullable = false)
    private boolean resolvido = false;

    @Column(nullable = false)
    private boolean lido = false;

    /**
     * Factory method para criar alerta
     */
    public static Alerta criar(
            Escala escala,
            Colaborador colaborador,
            Folga folga,
            TipoAlertaEnum tipo,
            String mensagem,
            String recomendacao
    ) {
        Alerta alerta = new Alerta();
        alerta.setEscala(escala);
        alerta.setColaborador(colaborador);
        alerta.setFolga(folga);
        alerta.setTipo(tipo);
        alerta.setSeveridade(tipo.getSeveridadeDefault());
        alerta.setMensagem(mensagem);
        alerta.setRecomendacao(recomendacao);
        alerta.setDataCriacao(LocalDateTime.now());
        return alerta;
    }

    /**
     * Marca alerta como lido
     */
    public void marcarComoLido() {
        this.lido = true;
    }

    /**
     * Marca alerta como resolvido
     */
    public void marcarComoResolvido() {
        this.resolvido = true;
        this.dataResolucao = LocalDateTime.now();
    }
}
