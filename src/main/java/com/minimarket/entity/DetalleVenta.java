package com.minimarket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Schema(description = "Linea de detalle de una venta: producto, cantidad y precio unitario aplicado.")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del detalle de venta.", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    // Se ignora al serializar para evitar la recursion Venta -> detalles -> Venta.
    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore
    @Schema(hidden = true)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @Schema(description = "Producto vendido en esta linea.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Producto producto;

    @Column(nullable = false)
    @Schema(description = "Cantidad de unidades vendidas del producto.", example = "2",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(description = "Precio unitario aplicado al momento de la venta.", example = "890.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precio;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
