package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.service.DetalleVentaService;
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
@RequestMapping("/api/detalle-ventas")
@Tag(name = "Detalle de ventas", description = "Consulta y registro de las lineas de detalle de cada venta.")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Operation(summary = "Listar detalles de venta",
            description = "Obtiene todas las lineas de detalle registradas en las ventas de MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de detalles de venta obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetalleVenta.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping
    public List<DetalleVenta> listarDetalleVentas() {
        return detalleVentaService.findAll();
    }

    @Operation(summary = "Obtener detalle de venta por ID",
            description = "Devuelve la informacion de una linea de detalle de venta segun su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetalleVenta.class))),
            @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetalleVentaPorId(
            @Parameter(description = "Identificador del detalle de venta", example = "1", required = true)
            @PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);
        return (detalleVenta != null) ? ResponseEntity.ok(detalleVenta) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar detalle de venta",
            description = "Registra una nueva linea de detalle asociada a una venta y un producto existentes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetalleVenta.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping
    public DetalleVenta guardarDetalleVenta(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del detalle de venta a registrar",
                    content = @Content(examples = @ExampleObject(name = "Detalle de venta nuevo",
                            value = """
                                    {
                                      "venta": { "id": 1 },
                                      "producto": { "id": 1 },
                                      "cantidad": 2,
                                      "precio": 890.0
                                    }""")))
            @RequestBody DetalleVenta detalleVenta) {
        return detalleVentaService.save(detalleVenta);
    }

    @Operation(summary = "Actualizar detalle de venta",
            description = "Actualiza los datos de una linea de detalle de venta existente identificada por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalle de venta actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DetalleVenta.class))),
            @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(
            @Parameter(description = "Identificador del detalle de venta a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody DetalleVenta detalleVenta) {
        DetalleVenta existente = detalleVentaService.findById(id);
        if (existente != null) {
            detalleVenta.setId(id);
            return ResponseEntity.ok(detalleVentaService.save(detalleVenta));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar detalle de venta",
            description = "Elimina una linea de detalle de venta por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Detalle de venta eliminado (sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Detalle de venta no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(
            @Parameter(description = "Identificador del detalle de venta a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);
        if (detalleVenta != null) {
            detalleVentaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
