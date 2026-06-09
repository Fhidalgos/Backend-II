package com.minimarket.service;

import com.minimarket.entity.Producto;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ProductoService {

    List<Producto> findAll();

    Producto findById(@NonNull Long id);

    Producto save(@NonNull Producto producto);

    void deleteById(@NonNull Long id);

    List<Producto> findByCategoriaId(@NonNull Long categoriaId);
}