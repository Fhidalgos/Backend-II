package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VentaController.class)
@Import(SecurityConfig.class)
class VentaControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VentaService ventaService;

    private Venta nuevaVenta;

    @BeforeEach
    void setUp() {

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("cliente1");

        nuevaVenta = new Venta();
        nuevaVenta.setUsuario(usuario);
        nuevaVenta.setFecha(new Date());
        nuevaVenta.setDetalles(List.of());
    }

    @Test
    @WithMockUser(
            username = "cajero",
            authorities = {"CAJERO"}
    )
    void cajeroPuedeGenerarVenta() throws Exception {

        when(ventaService.save(any(Venta.class)))
                .thenAnswer(invocacion -> {
                    Venta ventaGuardada = invocacion.getArgument(0);
                    ventaGuardada.setId(1L);
                    return ventaGuardada;
                });

        mockMvc.perform(
                        post("/api/ventas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevaVenta))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.usuario.id").value(1))
                .andExpect(jsonPath("$.usuario.username").value("cliente1"));

        verify(ventaService).save(any(Venta.class));
    }

    @Test
    @WithMockUser(
            username = "cliente",
            authorities = {"CLIENTE"}
    )
    void clienteNoPuedeGenerarVenta() throws Exception {

        mockMvc.perform(
                        post("/api/ventas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevaVenta))
                )
                .andExpect(status().isForbidden());

        verify(ventaService, never()).save(any(Venta.class));
    }

    @Test
    @WithMockUser(
            username = "administrador",
            authorities = {"ADMINISTRADOR"}
    )
    void administradorNoPuedeGenerarVenta() throws Exception {

        mockMvc.perform(
                        post("/api/ventas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevaVenta))
                )
                .andExpect(status().isForbidden());

        verify(ventaService, never()).save(any(Venta.class));
    }

    @Test
    void usuarioNoAutenticadoEsRedirigidoAlLogin() throws Exception {

        mockMvc.perform(
                        post("/api/ventas")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(nuevaVenta))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        verify(ventaService, never()).save(any(Venta.class));
    }
}