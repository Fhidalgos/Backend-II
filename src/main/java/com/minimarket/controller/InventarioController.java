package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
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
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Registro y consulta de movimientos de inventario (entradas y salidas de stock).")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(summary = "Listar movimientos de inventario",
            description = "Obtiene la lista completa de movimientos de inventario registrados en MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inventario.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping
    public List<Inventario> listarMovimientosDeInventario() {
        return inventarioService.findAll();
    }

    @Operation(summary = "Obtener movimiento por ID",
            description = "Devuelve la informacion detallada de un movimiento de inventario segun su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inventario.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerMovimientoPorId(
            @Parameter(description = "Identificador del movimiento de inventario", example = "1", required = true)
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        return (inventario != null) ? ResponseEntity.ok(inventario) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Registrar movimiento de inventario",
            description = "Registra un nuevo movimiento de inventario (entrada o salida de stock) para un producto existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento registrado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inventario.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping
    public Inventario registrarMovimiento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del movimiento de inventario a registrar",
                    content = @Content(examples = @ExampleObject(name = "Movimiento nuevo",
                            value = """
                                    {
                                      "producto": { "id": 1 },
                                      "cantidad": 20,
                                      "tipoMovimiento": "Entrada",
                                      "fechaMovimiento": "2026-07-06T15:30:00.000+00:00"
                                    }""")))
            @RequestBody Inventario inventario) {
        return inventarioService.save(inventario);
    }

    @Operation(summary = "Actualizar movimiento de inventario",
            description = "Actualiza los datos de un movimiento de inventario existente identificado por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Movimiento actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Inventario.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarMovimiento(
            @Parameter(description = "Identificador del movimiento a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Inventario inventario) {
        Inventario existente = inventarioService.findById(id);
        if (existente != null) {
            inventario.setId(id);
            return ResponseEntity.ok(inventarioService.save(inventario));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar movimiento de inventario",
            description = "Elimina un movimiento de inventario por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movimiento eliminado (sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(
            @Parameter(description = "Identificador del movimiento a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        Inventario inventario = inventarioService.findById(id);
        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
