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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/productos")
@Tag(
        name = "Productos",
        description = "Gestión del catálogo de productos de MiniMarket Plus con enlaces HATEOAS."
)
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @Operation(
            summary = "Listar productos",
            description = "Obtiene todos los productos e incorpora enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de productos obtenida correctamente",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = CollectionModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @GetMapping
    public CollectionModel<EntityModel<Producto>> listarProductos() {

        List<EntityModel<Producto>> productos = productoService.findAll()
                .stream()
                .map(this::crearModeloProducto)
                .toList();

        return CollectionModel.of(
                productos,

                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withSelfRel(),

                linkTo(methodOn(InventarioController.class)
                        .listarMovimientosDeInventario())
                        .withRel("inventario")
        );
    }

    @Operation(
            summary = "Obtener producto por ID",
            description = "Devuelve un producto junto con sus enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto encontrado",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(
            @Parameter(
                    description = "Identificador del producto",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Producto producto = productoService.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                crearModeloProducto(producto)
        );
    }

    @Operation(
            summary = "Crear producto",
            description = "Registra un producto y devuelve enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto creado correctamente",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @PostMapping
    public EntityModel<Producto> guardarProducto(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto que se desea crear",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Producto nuevo",
                                    value = """
                                            {
                                              "nombre": "Leche 1L",
                                              "precio": 1200.0,
                                              "stock": 30,
                                              "categoria": {
                                                "id": 1
                                              }
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Producto producto) {

        Producto productoGuardado =
                productoService.save(producto);

        return crearModeloProducto(productoGuardado);
    }

    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza un producto y devuelve sus enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto actualizado correctamente",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(
            @Parameter(
                    description = "Identificador del producto que se actualizará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody Producto producto) {

        Producto productoExistente =
                productoService.findById(id);

        if (productoExistente == null) {
            return ResponseEntity.notFound().build();
        }

        producto.setId(id);

        Producto productoActualizado =
                productoService.save(producto);

        return ResponseEntity.ok(
                crearModeloProducto(productoActualizado)
        );
    }

    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Producto eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Producto no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(
            @Parameter(
                    description = "Identificador del producto que se eliminará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Producto producto =
                productoService.findById(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        productoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private EntityModel<Producto> crearModeloProducto(
            Producto producto) {

        return EntityModel.of(
                producto,

                linkTo(methodOn(ProductoController.class)
                        .obtenerProductoPorId(producto.getId()))
                        .withSelfRel(),

                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withRel("productos"),

                linkTo(methodOn(InventarioController.class)
                        .listarMovimientosDeInventario())
                        .withRel("inventario")
        );
    }
}