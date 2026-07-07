package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Item del carrito de compras: asocia un producto y una cantidad a un usuario.")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del item del carrito.", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario dueno del carrito.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @Schema(description = "Producto agregado al carrito.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Producto producto;

    @Column(nullable = false)
    @Schema(description = "Cantidad del producto solicitada.", example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
