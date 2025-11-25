package com.oroboros.EscalaDeFolga.domain.util;

import java.text.Normalizer;

/**
 * Normaliza texto removendo acentos, convertendo para lowercase e removendo espaços extras.
 *
 * @return texto normalizado
 */

public class TextoNormalizerUtil {

    public static String normalizar(String texto) {
        if (texto == null || texto.isBlank()) {
            return "";
        }

        String semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");

        return semAcentos.toLowerCase()
                .trim()
                .replaceAll("\\s+", " ");
    }
}
