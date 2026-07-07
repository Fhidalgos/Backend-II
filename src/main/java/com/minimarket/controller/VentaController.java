package com.minimarket.controller;

import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@Tag(name = "Ventas", description = "Registro y consulta de las ventas realizadas en MiniMarket Plus.")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Operation(summary = "Listar ventas",
            description = "Obtiene la lista completa de las ventas registradas en MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Venta.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping
    public List<Venta> listarVentas() {
        return ventaService.findAll();
    }

    @Operation(summary = "Obtener venta por ID",
            description = "Devuelve la informacion detallada de una venta registrada segun su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Venta.class))),
            @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(
            @Parameter(description = "Identificador de la venta", example = "1", required = true)
            @PathVariable Long id) {
        Venta venta = ventaService.findById(id);
        return (venta != null) ? ResponseEntity.ok(venta) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar venta",
            description = "Registra una nueva venta en MiniMarket Plus. Requiere un usuario existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Venta registrada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Venta.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping
    public Venta guardarVenta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la venta a registrar",
                    content = @Content(examples = @ExampleObject(name = "Venta nueva",
                            value = """
                                    {
                                      "usuario": { "id": 1 },
                                      "fecha": "2026-07-06T15:45:00.000+00:00"
                                    }""")))
            @RequestBody Venta venta) {
        return ventaService.save(venta);
    }
}
