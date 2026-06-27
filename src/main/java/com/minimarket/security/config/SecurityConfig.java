package com.minimarket.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Se deshabilita CSRF para facilitar las pruebas de la API REST.
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Rutas públicas
                .requestMatchers(
                    "/",
                    "/public/**",
                    "/login",
                    "/logout",
                    "/error"
                ).permitAll()

                /*
                 * PRODUCTOS Y CATEGORÍAS
                 * Todos los usuarios autenticados pueden consultar.
                 * Solo ADMINISTRADOR puede crear, modificar o eliminar.
                 */
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/productos/**",
                    "/api/categorias/**"
                ).authenticated()

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/productos/**",
                    "/api/categorias/**"
                ).hasAuthority("ADMINISTRADOR")

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/productos/**",
                    "/api/categorias/**"
                ).hasAuthority("ADMINISTRADOR")

                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/productos/**",
                    "/api/categorias/**"
                ).hasAuthority("ADMINISTRADOR")

                /*
                 * INVENTARIO
                 * CAJERO y ADMINISTRADOR pueden consultar y registrar movimientos.
                 */
                .requestMatchers(
                    "/api/inventario/**"
                ).hasAnyAuthority("CAJERO", "ADMINISTRADOR")

                /*
                 * VENTAS
                 * Solo CAJERO puede generar una venta.
                 */
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/ventas/**"
                ).hasAuthority("CAJERO")

                /*
                 * CAJERO y ADMINISTRADOR pueden consultar ventas
                 * y sus respectivos detalles.
                 */
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/ventas/**",
                    "/api/detalle-ventas/**"
                ).hasAnyAuthority("CAJERO", "ADMINISTRADOR")

                .requestMatchers(
                    "/api/detalle-ventas/**"
                ).hasAnyAuthority("CAJERO", "ADMINISTRADOR")

                /*
                 * CARRITO
                 * CLIENTE administra su carrito.
                 */
                .requestMatchers(
                    "/api/carrito/**"
                ).hasAuthority("CLIENTE")

                /*
                 * USUARIOS
                 * Solo ADMINISTRADOR puede acceder a la gestión de usuarios.
                 */
                .requestMatchers(
                    "/api/usuarios/**"
                ).hasAuthority("ADMINISTRADOR")

                // Cualquier otra ruta requiere autenticación.
                .anyRequest().authenticated()
            )

            // Login proporcionado por Spring Security.
            .formLogin(form -> form
                .defaultSuccessUrl("/public/hola", true)
                .permitAll()
            )

            // Cierre de sesión.
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/public/hola")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration)
            throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}