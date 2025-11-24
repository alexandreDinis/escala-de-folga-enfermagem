package com.oroboros.EscalaDeFolga.domain.model.escala;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.Normalizer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    /**
     * Versão normalizada do nome para verificação de duplicatas.
     * - Remove acentos
     * - Converte para lowercase
     * - Remove espaços extras
     *
     * Exemplo: "UTI" → "uti", "Emergência" → "emergencia"
     */
    @Column(nullable = false, unique = true)
    private String nomeNormalizado;

    private boolean ativo = true;

    public void deletar(){
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
