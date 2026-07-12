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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/inventario")
@Tag(
        name = "Inventario",
        description = "Registro y consulta de movimientos de inventario con enlaces HATEOAS."
)
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @Operation(
            summary = "Listar movimientos de inventario",
            description = "Obtiene todos los movimientos registrados e incorpora enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de movimientos obtenida correctamente",
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
    public CollectionModel<EntityModel<Inventario>> listarMovimientosDeInventario() {

        @SuppressWarnings("unchecked")
        List<Inventario> inventarios =
                (List<Inventario>) inventarioService.findAll();

        List<EntityModel<Inventario>> movimientos = inventarios
                .stream()
                .map(this::crearModeloInventario)
                .toList();

        return CollectionModel.of(
                movimientos,

                linkTo(methodOn(InventarioController.class)
                        .listarMovimientosDeInventario())
                        .withSelfRel(),

                linkTo(methodOn(ProductoController.class)
                        .listarProductos())
                        .withRel("productos")
        );
    }

    @Operation(
            summary = "Obtener movimiento por ID",
            description = "Devuelve un movimiento de inventario junto con enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Movimiento encontrado",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Movimiento no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(
            @Parameter(
                    description = "Identificador del movimiento de inventario",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Inventario inventario = inventarioService.findById(id);

        if (inventario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                crearModeloInventario(inventario)
        );
    }

    @Operation(
            summary = "Registrar movimiento de inventario",
            description = "Registra una entrada o salida de stock y devuelve enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Movimiento registrado correctamente",
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
    public EntityModel<Inventario> registrarMovimiento(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del movimiento de inventario",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Movimiento nuevo",
                                    value = """
                                            {
                                              "producto": {
                                                "id": 1
                                              },
                                              "cantidad": 20,
                                              "tipoMovimiento": "Entrada",
                                              "fechaMovimiento": "2026-07-12T15:30:00.000+00:00"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Inventario inventario) {

        Inventario inventarioGuardado =
                inventarioService.save(inventario);

        return crearModeloInventario(inventarioGuardado);
    }

    @Operation(
            summary = "Actualizar movimiento de inventario",
            description = "Actualiza un movimiento existente y devuelve sus enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Movimiento actualizado correctamente",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Movimiento no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Inventario>> actualizarMovimiento(
            @Parameter(
                    description = "Identificador del movimiento que se actualizará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,

            @RequestBody Inventario inventario) {

        Inventario existente =
                inventarioService.findById(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        inventario.setId(id);

        Inventario inventarioActualizado =
                inventarioService.save(inventario);

        return ResponseEntity.ok(
                crearModeloInventario(inventarioActualizado)
        );
    }

    @Operation(
            summary = "Eliminar movimiento de inventario",
            description = "Elimina un movimiento mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Movimiento eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Movimiento no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(
            @Parameter(
                    description = "Identificador del movimiento que se eliminará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Inventario inventario =
                inventarioService.findById(id);

        if (inventario == null) {
            return ResponseEntity.notFound().build();
        }

        inventarioService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private EntityModel<Inventario> crearModeloInventario(
            Inventario inventario) {

        EntityModel<Inventario> modelo = EntityModel.of(
                inventario,

                linkTo(methodOn(InventarioController.class)
                        .obtenerMovimientoPorId(inventario.getId()))
                        .withSelfRel(),

                linkTo(methodOn(InventarioController.class)
                        .listarMovimientosDeInventario())
                        .withRel("inventario")
        );

        if (inventario.getProducto() != null
                && inventario.getProducto().getId() != null) {

            modelo.add(
                    linkTo(methodOn(ProductoController.class)
                            .obtenerProductoPorId(
                                    inventario.getProducto().getId()
                            ))
                            .withRel("producto")
            );
        }

        return modelo;
    }
}