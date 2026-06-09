package com.minimarket.service;

import com.minimarket.entity.Venta;
import org.springframework.lang.NonNull;

import java.util.List;

public interface VentaService {

    List<Venta> findAll();

    Venta findById(@NonNull Long id);

    Venta save(@NonNull Venta venta);

    List<Venta> findByUsuarioId(@NonNull Long usuarioId);
}