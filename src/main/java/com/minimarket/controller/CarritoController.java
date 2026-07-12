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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/carrito")
@Tag(
        name = "Carrito",
        description = "Gestión del carrito de compras con enlaces dinámicos HATEOAS."
)
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(
            summary = "Listar carrito",
            description = "Obtiene todos los elementos del carrito e incluye enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de elementos obtenida correctamente",
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
    public CollectionModel<EntityModel<Carrito>> listarCarrito() {

        List<EntityModel<Carrito>> elementos = carritoService.findAll()
                .stream()
                .map(this::crearModeloCarrito)
                .toList();

        return CollectionModel.of(
                elementos,

                linkTo(methodOn(CarritoController.class)
                        .listarCarrito())
                        .withSelfRel(),

                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withRel("productos")
        );
    }

    @Operation(
            summary = "Obtener elemento del carrito por ID",
            description = "Obtiene un elemento del carrito junto con sus enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Elemento del carrito encontrado",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Elemento del carrito no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(
            @Parameter(
                    description = "Identificador del elemento del carrito",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Carrito carrito = carritoService.findById(id);

        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(crearModeloCarrito(carrito));
    }

    @Operation(
            summary = "Agregar producto al carrito",
            description = "Agrega un producto al carrito y devuelve enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Producto agregado correctamente",
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
    public EntityModel<Carrito> agregarProductoAlCarrito(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del producto que se agregará al carrito",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Elemento nuevo",
                                    value = """
                                            {
                                              "usuario": {
                                                "id": 1
                                              },
                                              "producto": {
                                                "id": 1
                                              },
                                              "cantidad": 3
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Carrito carrito) {

        Carrito carritoGuardado = carritoService.save(carrito);

        return crearModeloCarrito(carritoGuardado);
    }

    @Operation(
            summary = "Actualizar elemento del carrito",
            description = "Actualiza la cantidad o información de un elemento del carrito."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Elemento actualizado correctamente",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Elemento del carrito no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Carrito>> actualizarCarrito(
            @Parameter(
                    description = "Identificador del elemento que se actualizará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @RequestBody Carrito carrito) {

        Carrito existente = carritoService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        carrito.setId(id);

        Carrito carritoActualizado = carritoService.save(carrito);

        return ResponseEntity.ok(
                crearModeloCarrito(carritoActualizado)
        );
    }

    @Operation(
            summary = "Eliminar producto del carrito",
            description = "Elimina un elemento del carrito mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Elemento eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Elemento del carrito no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(
            @Parameter(
                    description = "Identificador del elemento que se eliminará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Carrito carrito = carritoService.findById(id);

        if (carrito == null) {
            return ResponseEntity.notFound().build();
        }

        carritoService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private EntityModel<Carrito> crearModeloCarrito(Carrito carrito) {

        return EntityModel.of(
                carrito,

                linkTo(methodOn(CarritoController.class)
                        .obtenerCarritoPorId(carrito.getId()))
                        .withSelfRel(),

                linkTo(methodOn(CarritoController.class)
                        .listarCarrito())
                        .withRel("carrito"),

                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withRel("productos")
        );
    }
}