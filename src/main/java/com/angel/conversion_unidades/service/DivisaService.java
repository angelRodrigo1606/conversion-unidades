package com.angel.conversion_unidades.service;

import com.angel.conversion_unidades.config.DivisasProperties;
import com.angel.conversion_unidades.dto.DivisaResponse;
import com.angel.conversion_unidades.exception.UnidadNoSoportadaException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class DivisaService {

    private final Map<String, BigDecimal> tasas;

    public DivisaService(DivisasProperties properties) {
        this.tasas = properties.tasas();
    }

    public DivisaResponse convertir(BigDecimal valor, String de, String a) {
        String origen = normalizar(de);
        String destino = normalizar(a);
        validar(origen);
        validar(destino);

        BigDecimal resultado = origen.equals(destino)
                ? valor
                : valor.multiply(tasas.get(destino)).divide(tasas.get(origen), MathContext.DECIMAL64);
        return new DivisaResponse(valor, origen, resultado, destino);
    }

    public List<String> divisasDisponibles() {
        return tasas.keySet().stream().sorted().toList();
    }

    private void validar(String divisa) {
        if (!tasas.containsKey(divisa)) {
            throw new UnidadNoSoportadaException(divisa, "divisa", divisasDisponibles());
        }
    }

    private static String normalizar(String divisa) {
        return divisa == null ? "" : divisa.trim().toUpperCase(Locale.ROOT);
    }
}
