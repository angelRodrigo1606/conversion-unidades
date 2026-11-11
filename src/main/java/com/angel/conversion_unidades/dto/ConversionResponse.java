package com.angel.conversion_unidades.dto;

public record ConversionResponse(
        double valorOriginal,
        String unidadOrigen,
        double resultado,
        String unidadDestino) {
}
