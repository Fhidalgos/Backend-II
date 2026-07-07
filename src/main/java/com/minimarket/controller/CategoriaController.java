package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.service.CategoriaService;
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
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Gestion de las categorias que agrupan los productos del catalogo.")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Listar categorias",
            description = "Obtiene la lista completa de categorias que agrupan los productos de MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de categorias obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.findAll();
    }

    @Operation(summary = "Obtener categoria por ID",
            description = "Devuelve la informacion detallada de una categoria segun su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtenerCategoriaPorId(
            @Parameter(description = "Identificador de la categoria", example = "1", required = true)
            @PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        return (categoria != null) ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Crear categoria",
            description = "Registra una nueva categoria para agrupar los productos del catalogo de MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping
    public Categoria guardarCategoria(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la categoria a crear",
                    content = @Content(examples = @ExampleObject(name = "Categoria nueva",
                            value = """
                                    {
                                      "nombre": "Abarrotes"
                                    }""")))
            @RequestBody Categoria categoria) {
        return categoriaService.save(categoria);
    }

    @Operation(summary = "Actualizar categoria",
            description = "Actualiza los datos de una categoria existente identificada por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizarCategoria(
            @Parameter(description = "Identificador de la categoria a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Categoria categoria) {
        Categoria categoriaExistente = categoriaService.findById(id);
        if (categoriaExistente != null) {
            categoria.setId(id);
            return ResponseEntity.ok(categoriaService.save(categoria));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar categoria",
            description = "Elimina una categoria del catalogo por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoria eliminada (sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Categoria no encontrada", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "Identificador de la categoria a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        Categoria categoria = categoriaService.findById(id);
        if (categoria != null) {
            categoriaService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
