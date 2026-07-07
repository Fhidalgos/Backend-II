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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestion de usuarios del sistema y sus roles de acceso.")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Listar usuarios",
            description = "Obtiene la lista completa de usuarios registrados en MiniMarket Plus.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioService.findAll();
    }

    @Operation(summary = "Obtener usuario por ID",
            description = "Devuelve la informacion detallada de un usuario de MiniMarket Plus segun su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuarioPorId(
            @Parameter(description = "Identificador del usuario", example = "1", required = true)
            @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok) // Si el usuario existe, devuelve 200 OK con el usuario
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no, devuelve 404
    }

    @Operation(summary = "Crear usuario",
            description = "Registra un nuevo usuario en MiniMarket Plus con sus roles de acceso.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PostMapping
    public Usuario guardarUsuario(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del usuario a crear",
                    content = @Content(examples = @ExampleObject(name = "Usuario nuevo",
                            value = """
                                    {
                                      "username": "cajero1",
                                      "password": "clave123",
                                      "roles": [ { "id": 1 } ]
                                    }""")))
            @RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @Operation(summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente de MiniMarket Plus identificado por su ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @Parameter(description = "Identificador del usuario a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.findById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioService.save(usuario));
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Eliminar usuario",
            description = "Elimina un usuario de MiniMarket Plus por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado (sin contenido)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "Identificador del usuario a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        if (usuario.isPresent()) { // Verifica si el usuario existe
            usuarioService.deleteById(id); // Elimina al usuario
            return ResponseEntity.noContent().build(); // Respuesta 204 (sin contenido)
        }
        return ResponseEntity.notFound().build(); // Respuesta 404 (no encontrado)
    }
}
