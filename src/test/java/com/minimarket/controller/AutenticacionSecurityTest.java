package com.minimarket.controller;

import com.minimarket.security.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request
        .SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response
        .SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response
        .SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
        .redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
        .status;

@WebMvcTest(HolaMundoController.class)
@Import({
        SecurityConfig.class,
        AutenticacionSecurityTest.ConfiguracionUsuariosPrueba.class
})
class AutenticacionSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    /*
     * Configuración utilizada solamente durante las pruebas.
     * Se crea un usuario en memoria para verificar el inicio de sesión.
     */
    @TestConfiguration
    static class ConfiguracionUsuariosPrueba {

        @Bean
        UserDetailsService userDetailsService(
                PasswordEncoder passwordEncoder) {

            UserDetails cajero = User.builder()
                    .username("cajero")
                    .password(passwordEncoder.encode("clave123"))
                    .authorities("CAJERO")
                    .build();

            return new InMemoryUserDetailsManager(cajero);
        }
    }

    /*
     * Comprueba que un usuario registrado, con contraseña correcta,
     * puede iniciar sesión.
     */
    @Test
    void autenticacionValidaPermiteIniciarSesion() throws Exception {

        mockMvc.perform(
                        formLogin()
                                .user("cajero")
                                .password("clave123")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/public/hola"))
                .andExpect(
                        authenticated()
                                .withUsername("cajero")
                                .withAuthorities(
                                        List.of(
                                                new SimpleGrantedAuthority(
                                                        "CAJERO"
                                                )
                                        )
                                )
                );
    }

    /*
     * Comprueba que una contraseña incorrecta impide
     * iniciar sesión.
     */
    @Test
    void contrasenaIncorrectaRechazaAutenticacion() throws Exception {

        mockMvc.perform(
                        formLogin()
                                .user("cajero")
                                .password("claveIncorrecta")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }

    /*
     * Comprueba que un usuario que no existe no puede
     * iniciar sesión.
     */
    @Test
    void usuarioInexistenteRechazaAutenticacion() throws Exception {

        mockMvc.perform(
                        formLogin()
                                .user("usuarioInexistente")
                                .password("clave123")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(unauthenticated());
    }
}

