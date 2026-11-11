package com.angel.conversion_unidades;

import com.angel.conversion_unidades.config.DivisasProperties;
import com.angel.conversion_unidades.exception.UnidadNoSoportadaException;
import com.angel.conversion_unidades.service.DivisaService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DivisaServiceTests {

    private final DivisaService service = new DivisaService(new DivisasProperties("USD", Map.of(
            "USD", new BigDecimal("1"),
            "EUR", new BigDecimal("0.92"),
            "MXN", new BigDecimal("17.10"))));

    @Test
    void convierteDesdeLaBase() {
        assertEquals(0, new BigDecimal("1710")
                .compareTo(service.convertir(new BigDecimal("100"), "USD", "MXN").resultado()));
    }

    @Test
    void convierteEntreDivisasNoBase() {
        // 92 EUR = 100 USD = 1710 MXN
        assertEquals(0, new BigDecimal("1710")
                .compareTo(service.convertir(new BigDecimal("92"), "eur", "mxn").resultado()));
    }

    @Test
    void mismaDivisaDevuelveElMismoValor() {
        assertEquals(new BigDecimal("123.456"),
                service.convertir(new BigDecimal("123.456"), "MXN", "MXN").resultado());
    }

    @Test
    void divisaDesconocidaONullLanzaExcepcion() {
        assertThrows(UnidadNoSoportadaException.class, () -> service.convertir(BigDecimal.ONE, "USD", "JPY"));
        assertThrows(UnidadNoSoportadaException.class, () -> service.convertir(BigDecimal.ONE, null, "USD"));
    }

    @Test
    void configuracionInvalidaFallaAlArrancar() {
        assertThrows(IllegalArgumentException.class,
                () -> new DivisasProperties("USD", Map.of("USD", BigDecimal.ONE, "EUR", BigDecimal.ZERO)));
        assertThrows(IllegalArgumentException.class,
                () -> new DivisasProperties("USD", Map.of("EUR", BigDecimal.ONE)));
    }
}
