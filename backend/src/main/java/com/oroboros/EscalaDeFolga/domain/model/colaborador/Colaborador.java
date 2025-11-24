package com.oroboros.EscalaDeFolga.domain.model.colaborador;

import com.oroboros.EscalaDeFolga.domain.model.escala.Folga;
import com.oroboros.EscalaDeFolga.domain.model.escala.Setor;
import jakarta.persistence.*;
import lombok.*;

import java.text.Normalizer;
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

    @Column(nullable = false)
    private String nomeNormalizado;

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

    /**
     * Listener JPA que normaliza o nome automaticamente antes de persistir.
     */
    @PrePersist
    @PreUpdate
    private void normalizarNome() {
        if (this.nome != null) {
            this.nomeNormalizado = normalizarTexto(this.nome);
        }
    }

    /**
     * Normaliza texto removendo acentos, convertendo para lowercase e removendo espaços extras.
     *
     * @param texto texto original
     * @return texto normalizado
     */
    public static String normalizarTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return "";
        }
        // Remove acentos (NFD = Decomposição canônica)
        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        // Lowercase e remove espaços extras
        return semAcentos.toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }
}
