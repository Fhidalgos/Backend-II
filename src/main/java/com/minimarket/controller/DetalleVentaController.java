package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-ventas")
public class DetalleVentaController {

    @Autowired
    private DetalleVentaService detalleVentaService;

    @GetMapping
    public List<DetalleVenta> listarDetalleVentas() {
        return detalleVentaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetalleVenta> obtenerDetalleVentaPorId(@PathVariable @NonNull Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);

        if (detalleVenta != null) {
            return ResponseEntity.ok(detalleVenta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public DetalleVenta guardarDetalleVenta(@RequestBody @NonNull DetalleVenta detalleVenta) {
        return detalleVentaService.save(detalleVenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetalleVenta> actualizarDetalleVenta(
            @PathVariable @NonNull Long id,
            @RequestBody @NonNull DetalleVenta detalleVenta) {

        DetalleVenta existente = detalleVentaService.findById(id);

        if (existente != null) {
            detalleVenta.setId(id);
            DetalleVenta detalleVentaActualizado = detalleVentaService.save(detalleVenta);
            return ResponseEntity.ok(detalleVentaActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleVenta(@PathVariable @NonNull Long id) {
        DetalleVenta detalleVenta = detalleVentaService.findById(id);

        if (detalleVenta != null) {
            detalleVentaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}