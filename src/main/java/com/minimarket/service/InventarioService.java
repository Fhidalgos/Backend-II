package com.minimarket.service;

import com.minimarket.entity.Inventario;
import org.springframework.lang.NonNull;

import java.util.List;

public interface InventarioService {

    List<Inventario> findAll();

    Inventario findById(@NonNull Long id);

    Inventario save(@NonNull Inventario inventario);

    void deleteById(@NonNull Long id);

    List<Inventario> findByProductoId(@NonNull Long productoId);
}