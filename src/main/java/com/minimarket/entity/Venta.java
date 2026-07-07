package com.minimarket.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Schema(description = "Venta registrada en MiniMarket Plus, con su detalle de productos vendidos.")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico de la venta.", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @Schema(description = "Usuario (cajero) que registro la venta.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Usuario usuario;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora de la venta.", example = "2026-07-06T15:45:00.000+00:00",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Date fecha;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    @Schema(description = "Detalle de productos, cantidades y precios de la venta.")
    private List<DetalleVenta> detalles;

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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }
}
