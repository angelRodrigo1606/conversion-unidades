package com.angel.conversion_unidades.controller;

import com.angel.conversion_unidades.dto.ConversionResponse;
import com.angel.conversion_unidades.dto.DivisaResponse;
import com.angel.conversion_unidades.service.ConversionService;
import com.angel.conversion_unidades.service.DivisaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/convertir")
public class ConversionController {

    private final ConversionService service;
    private final DivisaService divisaService;

    public ConversionController(ConversionService service, DivisaService divisaService) {
        this.service = service;
        this.divisaService = divisaService;
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

    @GetMapping("/divisa")
    public DivisaResponse divisa(@RequestParam BigDecimal valor,
                                 @RequestParam String de,
                                 @RequestParam String a) {
        return divisaService.convertir(valor, de, a);
    }
}
