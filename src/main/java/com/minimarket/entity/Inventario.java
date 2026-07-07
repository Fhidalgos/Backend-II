package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Schema(description = "Movimiento de inventario (entrada o salida de stock) asociado a un producto.")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico del movimiento de inventario.", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @Schema(description = "Producto afectado por el movimiento.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Producto producto;

    @Column(nullable = false)
    @Schema(description = "Cantidad de unidades del movimiento.", example = "20",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer cantidad;

    @Column(nullable = false)
    @Schema(description = "Tipo de movimiento: Entrada (reabastecimiento) o Salida (consumo).",
            example = "Entrada", allowableValues = {"Entrada", "Salida"},
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String tipoMovimiento; // Ejemplo: "Entrada" o "Salida"

    @Column(nullable = false)
    @Schema(description = "Fecha y hora en que se registro el movimiento.",
            example = "2026-07-06T15:30:00.000+00:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Date fechaMovimiento;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public Date getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(Date fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }
}
