package com.minimarket.service.impl;

import com.minimarket.entity.Carrito;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Override
    public List<Carrito> findAll() {
        return carritoRepository.findAll();
    }

    @Override
    public Carrito findById(@NonNull Long id) {
        return carritoRepository.findById(id).orElse(null);
    }

    @Override
    public Carrito save(@NonNull Carrito carrito) {
        return carritoRepository.save(carrito);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        carritoRepository.deleteById(id);
    }

    @Override
    public List<Carrito> findByUsuarioId(@NonNull Long usuarioId) {
        return carritoRepository.findByUsuarioId(usuarioId);
    }
}