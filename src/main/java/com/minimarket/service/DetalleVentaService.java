package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import org.springframework.lang.NonNull;

import java.util.List;

public interface DetalleVentaService {

    List<DetalleVenta> findAll();

    DetalleVenta findById(@NonNull Long id);

    DetalleVenta save(@NonNull DetalleVenta detalleVenta);

    void deleteById(@NonNull Long id);

    List<DetalleVenta> findByVentaId(@NonNull Long ventaId);
}