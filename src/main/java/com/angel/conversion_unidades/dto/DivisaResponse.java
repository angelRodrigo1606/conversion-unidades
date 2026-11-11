package com.angel.conversion_unidades.dto;

import java.math.BigDecimal;

public record DivisaResponse(
        BigDecimal valorOriginal,
        String unidadOrigen,
        BigDecimal resultado,
        String unidadDestino) {
}
