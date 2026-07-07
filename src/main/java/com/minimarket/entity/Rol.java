package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Schema(description = "Rol de autorizacion que agrupa permisos de acceso (RBAC).")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del rol.", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre del rol segun convencion de Spring Security.",
            example = "ROLE_ADMIN", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @ManyToMany(mappedBy = "roles")
    @com.fasterxml.jackson.annotation.JsonIgnore
    @Schema(hidden = true)
    private Set<Usuario> usuarios;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Set<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
