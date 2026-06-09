package com.minimarket.config;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // 1. Crear roles si no existen
        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
            .orElseGet(() -> {
                Rol r = new Rol();
                r.setNombre("CLIENTE");
                return rolRepository.save(r);
            });
        
        Rol rolEmpleado = rolRepository.findByNombre("EMPLEADO")
            .orElseGet(() -> {
                Rol r = new Rol();
                r.setNombre("EMPLEADO");
                return rolRepository.save(r);
            });
        
        Rol rolGerente = rolRepository.findByNombre("GERENTE")
            .orElseGet(() -> {
                Rol r = new Rol();
                r.setNombre("GERENTE");
                return rolRepository.save(r);
            });

        // 2. Crear usuario cliente1
        if (usuarioRepository.findByUsername("cliente1").isEmpty()) {
            Usuario cliente = new Usuario();
            cliente.setUsername("cliente1");
            cliente.setPassword(passwordEncoder.encode("123456"));
            cliente.setRoles(Set.of(rolCliente));
            usuarioRepository.save(cliente);
            System.out.println("Usuario cliente1 creado con contraseña 123456");
        }

        // 3. Crear usario empleado
        if (usuarioRepository.findByUsername("empleado1").isEmpty()) {
           Usuario empleado = new Usuario();
           empleado.setUsername("empleado1");
           empleado.setPassword(passwordEncoder.encode("123456"));
           empleado.setRoles(Set.of(rolEmpleado));
           usuarioRepository.save(empleado);
           System.out.println("Usuario empleado1 creado con contraseña 123456");
}

        // 4. Crear usuario admin
        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(rolGerente));
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado con contraseña admin123");
        }

        System.out.println(" Usuarios disponibles: cliente1/123456 (CLIENTE), admin/admin123 (GERENTE)");
    }
}