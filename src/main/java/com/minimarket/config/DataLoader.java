package com.minimarket.config;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.repository.RolRepository;
import com.minimarket.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Carga datos semilla al iniciar la aplicacion (base de datos H2 en memoria).
 *
 * <p>Sin estos datos, la base arranca vacia y no seria posible <b>validar los
 * endpoints documentados</b> en Swagger UI y Postman (criterios 3 y 4 de la
 * pauta). Se crea:</p>
 * <ul>
 *   <li>Un rol <code>ROLE_ADMIN</code> y un usuario <code>admin / admin123</code>
 *       (contrasena cifrada con BCrypt) para autenticarse.</li>
 *   <li>Una categoria y un producto de ejemplo para probar las consultas GET y
 *       satisfacer la relacion Producto -&gt; Categoria (FK obligatoria).</li>
 * </ul>
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UsuarioRepository usuarioRepository,
                      RolRepository rolRepository,
                      CategoriaRepository categoriaRepository,
                      ProductoRepository productoRepository,
                      PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Evita duplicar los datos si la aplicacion se reinicia con datos persistidos.
        if (usuarioRepository.findByUsername("admin").isPresent()) {
            return;
        }

        // Rol y usuario administrador para autenticacion HTTP Basic.
        Rol admin = new Rol();
        admin.setNombre("ROLE_ADMIN");
        rolRepository.save(admin);

        Usuario usuario = new Usuario();
        usuario.setUsername("admin");
        usuario.setPassword(passwordEncoder.encode("admin123"));
        usuario.setRoles(Set.of(admin));
        usuarioRepository.save(usuario);

        // Catalogo minimo de ejemplo para probar los endpoints de consulta.
        Categoria bebidas = new Categoria();
        bebidas.setNombre("Bebidas");
        categoriaRepository.save(bebidas);

        Producto agua = new Producto();
        agua.setNombre("Agua Mineral 1L");
        agua.setPrecio(890.0);
        agua.setStock(50);
        agua.setCategoria(bebidas);
        productoRepository.save(agua);
    }
}
