package com.minimarket.service;

import com.minimarket.entity.Carrito;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CarritoService {

    List<Carrito> findAll();

    Carrito findById(@NonNull Long id);

    Carrito save(@NonNull Carrito carrito);

    void deleteById(@NonNull Long id);

    List<Carrito> findByUsuarioId(@NonNull Long usuarioId);
}