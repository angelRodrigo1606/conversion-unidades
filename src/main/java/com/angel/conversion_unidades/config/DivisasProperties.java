package com.angel.conversion_unidades.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tasas de cambio leídas de la configuración ({@code divisas.*}).
 * Cada tasa indica cuántas unidades de esa divisa equivalen a 1 unidad de la divisa base.
 */
@ConfigurationProperties(prefix = "divisas")
public record DivisasProperties(String base, Map<String, BigDecimal> tasas) {

    public DivisasProperties {
        if (base == null || base.isBlank()) {
            throw new IllegalArgumentException("divisas.base es obligatorio");
        }
        if (tasas == null || tasas.isEmpty()) {
            throw new IllegalArgumentException("divisas.tasas debe definir al menos una divisa");
        }
        base = base.trim().toUpperCase(Locale.ROOT);
        tasas = tasas.entrySet().stream().collect(Collectors.toUnmodifiableMap(
                e -> e.getKey().trim().toUpperCase(Locale.ROOT),
                Map.Entry::getValue));
        tasas.forEach((codigo, tasa) -> {
            if (tasa == null || tasa.signum() <= 0) {
                throw new IllegalArgumentException("La tasa de " + codigo + " debe ser mayor que 0");
            }
        });
        if (!tasas.containsKey(base) || tasas.get(base).compareTo(BigDecimal.ONE) != 0) {
            throw new IllegalArgumentException("La divisa base " + base + " debe tener tasa 1");
        }
    }
}
