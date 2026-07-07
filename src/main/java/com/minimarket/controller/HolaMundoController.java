package com.minimarket.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Publico", description = "Endpoint publico de prueba, accesible sin autenticacion.")
public class HolaMundoController {

    @GetMapping("/public/hola")
    @Operation(summary = "Saludo publico", description = "Endpoint publico de verificacion (health-check simple) que no requiere autenticacion.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saludo devuelto correctamente")
    })
    public String holaMundo() {
        return "¡Hola Mundo!";
    }
}
