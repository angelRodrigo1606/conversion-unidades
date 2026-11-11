package com.angel.conversion_unidades.controller;

import com.angel.conversion_unidades.service.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UnidadesController {

    private final ConversionService service;

    public UnidadesController(ConversionService service) {
        this.service = service;
    }

    @GetMapping("/unidades")
    public Map<String, List<String>> unidades() {
        return service.unidadesDisponibles();
    }
}
