package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;
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
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Gestion del carrito de compras: productos y cantidades por usuario.")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Listar carrito",
            description = "Obtiene todos los items del carrito de compras de MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de items del carrito obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping
    public List<Carrito> listarCarrito() {
        return carritoService.findAll();
    }

    @Operation(summary = "Obtener item del carrito por ID",
            description = "Devuelve la informacion detallada de un item del carrito segun su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item del carrito encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "404", description = "Item del carrito no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerCarritoPorId(
            @Parameter(description = "Identificador del item del carrito", example = "1", required = true)
            @PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        return (carrito != null) ? ResponseEntity.ok(carrito) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Agregar producto al carrito",
            description = "Agrega un producto con su cantidad al carrito de compras de un usuario en MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto agregado al carrito correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping
    public Carrito agregarProductoAlCarrito(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto a agregar al carrito",
                    content = @Content(examples = @ExampleObject(name = "Item de carrito nuevo",
                            value = """
                                    {
                                      "usuario": { "id": 1 },
                                      "producto": { "id": 1 },
                                      "cantidad": 3
                                    }""")))
            @RequestBody Carrito carrito) {
        return carritoService.save(carrito);
    }

    @Operation(summary = "Actualizar item del carrito",
            description = "Actualiza los datos de un item existente del carrito identificado por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item del carrito actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Carrito.class))),
            @ApiResponse(responseCode = "404", description = "Item del carrito no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Carrito> actualizarCarrito(
            @Parameter(description = "Identificador del item del carrito a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Carrito carrito) {
        Carrito existente = carritoService.findById(id);
        if (existente != null) {
            carrito.setId(id);
            return ResponseEntity.ok(carritoService.save(carrito));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar producto del carrito",
            description = "Elimina un item del carrito de compras por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item del carrito eliminado (sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Item del carrito no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @Parameter(description = "Identificador del item del carrito a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito != null) {
            carritoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
