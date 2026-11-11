package com.angel.conversion_unidades;

import com.angel.conversion_unidades.exception.UnidadNoSoportadaException;
import com.angel.conversion_unidades.service.ConversionService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConversionServiceTests {

    private final ConversionService service = new ConversionService();

    @Test
    void unidadNullLanzaUnidadNoSoportada() {
        assertThrows(UnidadNoSoportadaException.class, () -> service.convertirLongitud(1, null, "m"));
        assertThrows(UnidadNoSoportadaException.class, () -> service.convertirPeso(1, "kg", null));
        assertThrows(UnidadNoSoportadaException.class, () -> service.convertirTemperatura(1, null, null));
    }
}
