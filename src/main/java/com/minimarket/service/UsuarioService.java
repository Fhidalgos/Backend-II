package com.minimarket.service;

import com.minimarket.entity.Usuario;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();

    Optional<Usuario> findById(@NonNull Long id);

    Optional<Usuario> findByUsername(@NonNull String username);

    Usuario save(@NonNull Usuario usuario);

    void deleteById(@NonNull Long id);
}