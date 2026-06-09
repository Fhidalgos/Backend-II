package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public List<Inventario> listarMovimientosDeInventario() {
        return inventarioService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventario> obtenerMovimientoPorId(@PathVariable @NonNull Long id) {
        Inventario inventario = inventarioService.findById(id);

        if (inventario != null) {
            return ResponseEntity.ok(inventario);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Inventario registrarMovimiento(@RequestBody @NonNull Inventario inventario) {
        return inventarioService.save(inventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventario> actualizarMovimiento(
            @PathVariable @NonNull Long id,
            @RequestBody @NonNull Inventario inventario) {

        Inventario existente = inventarioService.findById(id);

        if (existente != null) {
            inventario.setId(id);
            Inventario inventarioActualizado = inventarioService.save(inventario);
            return ResponseEntity.ok(inventarioActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable @NonNull Long id) {
        Inventario inventario = inventarioService.findById(id);

        if (inventario != null) {
            inventarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}