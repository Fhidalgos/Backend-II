package com.minimarket.controller;

import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
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
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestion del catalogo de productos de MiniMarket Plus.")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Listar productos",
            description = "Obtiene la lista completa de productos del catalogo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.findAll();
    }

    @Operation(summary = "Obtener producto por ID",
            description = "Devuelve la informacion detallada de un producto segun su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(
            @Parameter(description = "Identificador del producto", example = "1", required = true)
            @PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return (producto != null) ? ResponseEntity.ok(producto) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear producto",
            description = "Registra un nuevo producto en el catalogo. Requiere una categoria existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping
    public Producto guardarProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto a crear",
                    content = @Content(examples = @ExampleObject(name = "Producto nuevo",
                            value = """
                                    {
                                      "nombre": "Leche 1L",
                                      "precio": 1200.0,
                                      "stock": 30,
                                      "categoria": { "id": 1 }
                                    }""")))
            @RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @Operation(summary = "Actualizar producto",
            description = "Actualiza los datos de un producto existente identificado por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Producto.class))),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "Identificador del producto a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Producto producto) {
        Producto productoExistente = productoService.findById(id);
        if (productoExistente != null) {
            producto.setId(id);
            return ResponseEntity.ok(productoService.save(producto));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar producto",
            description = "Elimina un producto del catalogo por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado (sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(description = "Identificador del producto a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto != null) {
            productoService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
