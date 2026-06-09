package com.minimarket.service.impl;

import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    public Producto findById(@NonNull Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public Producto save(@NonNull Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        productoRepository.deleteById(id);
    }

    @Override
    public List<Producto> findByCategoriaId(@NonNull Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId);
    }
}