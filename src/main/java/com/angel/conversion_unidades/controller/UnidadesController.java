package com.angel.conversion_unidades.controller;

import com.angel.conversion_unidades.service.ConversionService;
import com.angel.conversion_unidades.service.DivisaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UnidadesController {

    private final ConversionService service;
    private final DivisaService divisaService;

    public UnidadesController(ConversionService service, DivisaService divisaService) {
        this.service = service;
        this.divisaService = divisaService;
    }

    @GetMapping("/unidades")
    public Map<String, List<String>> unidades() {
        Map<String, List<String>> unidades = new LinkedHashMap<>(service.unidadesDisponibles());
        unidades.put("divisa", divisaService.divisasDisponibles());
        return unidades;
    }
}
