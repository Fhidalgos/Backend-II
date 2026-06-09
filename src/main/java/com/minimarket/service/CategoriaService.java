package com.minimarket.service;

import com.minimarket.entity.Categoria;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CategoriaService {

    List<Categoria> findAll();

    Categoria findById(@NonNull Long id);

    Categoria save(@NonNull Categoria categoria);

    void deleteById(@NonNull Long id);
}