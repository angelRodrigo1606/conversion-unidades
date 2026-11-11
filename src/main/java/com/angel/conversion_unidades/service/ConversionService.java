package com.angel.conversion_unidades.service;

import com.angel.conversion_unidades.dto.ConversionResponse;
import com.angel.conversion_unidades.exception.UnidadNoSoportadaException;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class ConversionService {

    /** Factores para pasar cada unidad a metros. */
    private static final Map<String, Double> LONGITUD = Map.of(
            "mm", 0.001,
            "cm", 0.01,
            "m", 1.0,
            "km", 1000.0,
            "in", 0.0254,
            "ft", 0.3048,
            "yd", 0.9144,
            "mi", 1609.344);

    /** Factores para pasar cada unidad a gramos. */
    private static final Map<String, Double> PESO = Map.of(
            "mg", 0.001,
            "g", 1.0,
            "kg", 1000.0,
            "t", 1_000_000.0,
            "oz", 28.349523125,
            "lb", 453.59237);

    private static final Set<String> TEMPERATURA = Set.of("c", "f", "k");

    /** Categorías con sus unidades disponibles, ordenadas alfabéticamente. */
    public Map<String, List<String>> unidadesDisponibles() {
        Map<String, List<String>> unidades = new LinkedHashMap<>();
        unidades.put("longitud", LONGITUD.keySet().stream().sorted().toList());
        unidades.put("peso", PESO.keySet().stream().sorted().toList());
        unidades.put("temperatura", TEMPERATURA.stream().sorted()
                .map(u -> u.toUpperCase(Locale.ROOT)).toList());
        return unidades;
    }

    public ConversionResponse convertirLongitud(double valor, String de, String a) {
        return convertirPorFactor(valor, de, a, LONGITUD, "longitud");
    }

    public ConversionResponse convertirPeso(double valor, String de, String a) {
        return convertirPorFactor(valor, de, a, PESO, "peso");
    }

    public ConversionResponse convertirTemperatura(double valor, String de, String a) {
        String origen = normalizar(de);
        String destino = normalizar(a);
        validar(origen, TEMPERATURA, "temperatura");
        validar(destino, TEMPERATURA, "temperatura");

        if (origen.equals(destino)) {
            return new ConversionResponse(valor, origen.toUpperCase(Locale.ROOT), valor,
                    destino.toUpperCase(Locale.ROOT));
        }

        double celsius = switch (origen) {
            case "c" -> valor;
            case "f" -> (valor - 32) * 5 / 9;
            case "k" -> valor - 273.15;
            default -> throw new IllegalStateException();
        };
        double resultado = switch (destino) {
            case "c" -> celsius;
            case "f" -> celsius * 9 / 5 + 32;
            case "k" -> celsius + 273.15;
            default -> throw new IllegalStateException();
        };
        return new ConversionResponse(valor, origen.toUpperCase(Locale.ROOT), resultado,
                destino.toUpperCase(Locale.ROOT));
    }

    private ConversionResponse convertirPorFactor(double valor, String de, String a,
                                                  Map<String, Double> factores, String categoria) {
        String origen = normalizar(de);
        String destino = normalizar(a);
        validar(origen, factores.keySet(), categoria);
        validar(destino, factores.keySet(), categoria);

        double resultado = origen.equals(destino)
                ? valor
                : valor * factores.get(origen) / factores.get(destino);
        return new ConversionResponse(valor, origen, resultado, destino);
    }

    private static void validar(String unidad, Set<String> soportadas, String categoria) {
        if (!soportadas.contains(unidad)) {
            throw new UnidadNoSoportadaException(unidad, categoria, soportadas.stream().sorted().toList());
        }
    }

    /** Devuelve "" si la unidad es null, para que validar() la rechace con un 400 en vez de un NPE. */
    private static String normalizar(String unidad) {
        if (unidad == null) {
            return "";
        }
        return unidad.trim().toLowerCase(Locale.ROOT);
    }
}
