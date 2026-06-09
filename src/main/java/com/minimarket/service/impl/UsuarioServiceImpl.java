package com.minimarket.service.impl;

import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> findById(@NonNull Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByUsername(@NonNull String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Usuario save(@NonNull Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void deleteById(@NonNull Long id) {
        usuarioRepository.deleteById(id);
    }
}