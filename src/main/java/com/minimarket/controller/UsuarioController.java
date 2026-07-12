package com.minimarket.controller;

import com.minimarket.entity.Usuario;
import com.minimarket.service.UsuarioService;
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
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@Tag(
        name = "Usuarios",
        description = "Administración de usuarios y roles con enlaces dinámicos HATEOAS."
)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(
            summary = "Listar usuarios",
            description = "Obtiene todos los usuarios registrados e incorpora enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de usuarios obtenida correctamente",
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
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {

        List<EntityModel<Usuario>> usuarios = usuarioService.findAll()
                .stream()
                .map(this::crearModeloUsuario)
                .toList();

        return CollectionModel.of(
                usuarios,
                linkTo(methodOn(UsuarioController.class)
                        .listarUsuarios())
                        .withSelfRel(),

                linkTo(methodOn(CarritoController.class)
                        .listarCarrito())
                        .withRel("carrito")
        );
    }

    @Operation(
            summary = "Obtener usuario por ID",
            description = "Devuelve un usuario junto con sus enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorId(
            @Parameter(
                    description = "Identificador del usuario",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Optional<Usuario> usuario = usuarioService.findById(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                crearModeloUsuario(usuario.get())
        );
    }

    @Operation(
            summary = "Crear usuario",
            description = "Registra un usuario con sus roles y devuelve enlaces HATEOAS."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario creado correctamente",
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
    public EntityModel<Usuario> guardarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario que se desea crear",
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Usuario nuevo",
                                    value = """
                                            {
                                              "username": "cajero1",
                                              "password": "clave123",
                                              "roles": [
                                                {
                                                  "id": 1
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
            @RequestBody Usuario usuario) {

        Usuario usuarioGuardado = usuarioService.save(usuario);

        return crearModeloUsuario(usuarioGuardado);
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos y roles de un usuario existente."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado correctamente",
                    content = @Content(
                            mediaType = "application/hal+json",
                            schema = @Schema(implementation = EntityModel.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> actualizarUsuario(
            @Parameter(
                    description = "Identificador del usuario que se actualizará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Optional<Usuario> usuarioExistente =
                usuarioService.findById(id);

        if (usuarioExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuario.setId(id);

        Usuario usuarioActualizado =
                usuarioService.save(usuario);

        return ResponseEntity.ok(
                crearModeloUsuario(usuarioActualizado)
        );
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario mediante su identificador."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado correctamente",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(
                    description = "Identificador del usuario que se eliminará",
                    example = "1",
                    required = true
            )
            @PathVariable Long id) {

        Optional<Usuario> usuario =
                usuarioService.findById(id);

        if (usuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    private EntityModel<Usuario> crearModeloUsuario(Usuario usuario) {

        return EntityModel.of(
                usuario,

                linkTo(methodOn(UsuarioController.class)
                        .obtenerUsuarioPorId(usuario.getId()))
                        .withSelfRel(),

                linkTo(methodOn(UsuarioController.class)
                        .listarUsuarios())
                        .withRel("usuarios"),

                linkTo(methodOn(CarritoController.class)
                        .listarCarrito())
                        .withRel("carrito")
        );
    }
}