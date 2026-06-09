package com.minimarket.service.impl;

import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria findById(@NonNull Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public Categoria save(@NonNull Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        categoriaRepository.deleteById(id);
    }
}