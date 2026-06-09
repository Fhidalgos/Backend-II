package com.minimarket.service.impl;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.service.DetalleVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    public List<DetalleVenta> findAll() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public DetalleVenta findById(@NonNull Long id) {
        return detalleVentaRepository.findById(id).orElse(null);
    }

    @Override
    public DetalleVenta save(@NonNull DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        detalleVentaRepository.deleteById(id);
    }

    @Override
    public List<DetalleVenta> findByVentaId(@NonNull Long ventaId) {
        return detalleVentaRepository.findByVentaId(ventaId);
    }
}