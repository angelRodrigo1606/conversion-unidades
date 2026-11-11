package com.angel.conversion_unidades.controller;

import com.angel.conversion_unidades.dto.ConversionResponse;
import com.angel.conversion_unidades.service.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/convertir")
public class ConversionController {

    private final ConversionService service;

    public ConversionController(ConversionService service) {
        this.service = service;
    }

    @GetMapping("/longitud")
    public ConversionResponse longitud(@RequestParam double valor,
                                       @RequestParam String de,
                                       @RequestParam String a) {
        return service.convertirLongitud(valor, de, a);
    }

    @GetMapping("/peso")
    public ConversionResponse peso(@RequestParam double valor,
                                   @RequestParam String de,
                                   @RequestParam String a) {
        return service.convertirPeso(valor, de, a);
    }

    @GetMapping("/temperatura")
    public ConversionResponse temperatura(@RequestParam double valor,
                                          @RequestParam String de,
                                          @RequestParam String a) {
        return service.convertirTemperatura(valor, de, a);
    }
}
