package com.angel.conversion_unidades.exception;

public class UnidadNoSoportadaException extends RuntimeException {

    public UnidadNoSoportadaException(String unidad, String categoria, Iterable<String> soportadas) {
        super("Unidad '%s' no soportada para %s. Unidades válidas: %s"
                .formatted(unidad, categoria, String.join(", ", soportadas)));
    }
}
