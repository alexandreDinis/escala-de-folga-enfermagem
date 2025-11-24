package com.oroboros.EscalaDeFolga.domain.model.escala;

import com.oroboros.EscalaDeFolga.domain.util.TextoNormalizerUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
            this.nomeNormalizado = TextoNormalizerUtil.normalizar(this.nome);
        }
    }

}
