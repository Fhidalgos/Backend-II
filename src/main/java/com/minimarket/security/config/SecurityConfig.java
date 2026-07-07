package com.minimarket.security.config;

import com.minimarket.security.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilita CSRF con la nueva sintaxis
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**").permitAll() // Permitir acceso público
                        // Rutas de documentacion OpenAPI: Swagger UI y el documento JSON
                        // deben ser accesibles sin autenticacion para poder consultar y
                        // validar la documentacion de la API (Semana 7).
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/h2-console/**"
                        ).permitAll()
                        .anyRequest().authenticated() // Requiere autenticación para el resto
                )
                // Permite embeber la consola H2 (usa frames del mismo origen).
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                // Autenticacion HTTP Basic: comoda para probar endpoints desde Swagger UI
                // y Postman con el boton "Authorize".
                .httpBasic(basic -> {})
                .formLogin(form -> form
                        .defaultSuccessUrl("/public/hola", true) // Redirigir después del login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/public/hola")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Configuración de encriptación de contraseñas
    }
}
