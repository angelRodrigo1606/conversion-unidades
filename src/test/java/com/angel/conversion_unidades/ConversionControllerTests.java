package com.angel.conversion_unidades;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConversionControllerTests {

    @Autowired
    MockMvc mvc;

    @Test
    void longitudKmAMillas() throws Exception {
        mvc.perform(get("/convertir/longitud").param("valor", "10").param("de", "km").param("a", "mi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorOriginal").value(10.0))
                .andExpect(jsonPath("$.unidadOrigen").value("km"))
                .andExpect(jsonPath("$.resultado").value(closeTo(6.2137119, 1e-6)))
                .andExpect(jsonPath("$.unidadDestino").value("mi"));
    }

    @Test
    void pesoKgALibras() throws Exception {
        mvc.perform(get("/convertir/peso").param("valor", "1").param("de", "kg").param("a", "lb"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(closeTo(2.2046226, 1e-6)));
    }

    @Test
    void temperaturaCelsiusAFahrenheit() throws Exception {
        mvc.perform(get("/convertir/temperatura").param("valor", "100").param("de", "C").param("a", "F"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(212.0))
                .andExpect(jsonPath("$.unidadOrigen").value("C"))
                .andExpect(jsonPath("$.unidadDestino").value("F"));
    }

    @Test
    void mismaUnidadDevuelveValorExacto() throws Exception {
        mvc.perform(get("/convertir/longitud").param("valor", "1.23456789").param("de", "m").param("a", "m"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value(1.23456789));
    }

    @Test
    void listaUnidadesPorCategoria() throws Exception {
        mvc.perform(get("/unidades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.longitud", contains("cm", "ft", "in", "km", "m", "mi", "mm", "yd")))
                .andExpect(jsonPath("$.peso", contains("g", "kg", "lb", "mg", "oz", "t")))
                .andExpect(jsonPath("$.temperatura", contains("C", "F", "K")))
                .andExpect(jsonPath("$.divisa", contains("EUR", "MXN", "USD")));
    }

    @Test
    void divisaUsdAMxn() throws Exception {
        mvc.perform(get("/convertir/divisa").param("valor", "100").param("de", "USD").param("a", "MXN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorOriginal").value(100))
                .andExpect(jsonPath("$.unidadOrigen").value("USD"))
                .andExpect(jsonPath("$.resultado").value(1710.0))
                .andExpect(jsonPath("$.unidadDestino").value("MXN"));
    }

    @Test
    void divisaNoConfiguradaDevuelve400() throws Exception {
        mvc.perform(get("/convertir/divisa").param("valor", "1").param("de", "USD").param("a", "JPY"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void unidadInvalidaDevuelve400() throws Exception {
        mvc.perform(get("/convertir/longitud").param("valor", "1").param("de", "km").param("a", "kg"))
                .andExpect(status().isBadRequest());
    }
}
