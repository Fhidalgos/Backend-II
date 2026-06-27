package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.service.InventarioService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventarioController.class)
@Import(SecurityConfig.class)
class InventarioControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InventarioService inventarioService;

    private Inventario movimientoEntrada;

    @BeforeEach
    void setUp() {

        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Arroz");
        producto.setPrecio(1800.0);
        producto.setStock(20);

        movimientoEntrada = new Inventario();
        movimientoEntrada.setProducto(producto);
        movimientoEntrada.setCantidad(10);
        movimientoEntrada.setTipoMovimiento("Entrada");
        movimientoEntrada.setFechaMovimiento(new Date());
    }

    @Test
    @WithMockUser(
            username = "cajero",
            authorities = {"CAJERO"}
    )
    void cajeroPuedeRegistrarMovimientoDeInventario() throws Exception {

        when(inventarioService.save(any(Inventario.class)))
                .thenAnswer(invocacion -> {
                    Inventario inventarioGuardado = invocacion.getArgument(0);
                    inventarioGuardado.setId(1L);
                    return inventarioGuardado;
                });

        mockMvc.perform(
                        post("/api/inventario")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(movimientoEntrada))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cantidad").value(10))
                .andExpect(jsonPath("$.tipoMovimiento").value("Entrada"))
                .andExpect(jsonPath("$.producto.id").value(1));
    }

    @Test
    @WithMockUser(
            username = "administrador",
            authorities = {"ADMINISTRADOR"}
    )
    void administradorPuedeRegistrarMovimientoDeInventario() throws Exception {

        when(inventarioService.save(any(Inventario.class)))
                .thenAnswer(invocacion -> {
                    Inventario inventarioGuardado = invocacion.getArgument(0);
                    inventarioGuardado.setId(2L);
                    return inventarioGuardado;
                });

        mockMvc.perform(
                        post("/api/inventario")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(movimientoEntrada))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.cantidad").value(10))
                .andExpect(jsonPath("$.tipoMovimiento").value("Entrada"));
    }

    @Test
    @WithMockUser(
            username = "cliente",
            authorities = {"CLIENTE"}
    )
    void clienteNoPuedeRegistrarMovimientoDeInventario() throws Exception {

        mockMvc.perform(
                        post("/api/inventario")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(movimientoEntrada))
                )
                .andExpect(status().isForbidden());

        verify(inventarioService, never()).save(any(Inventario.class));
    }

    @Test
    void usuarioNoAutenticadoEsRedirigidoAlLogin() throws Exception {

        mockMvc.perform(
                        post("/api/inventario")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(movimientoEntrada))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        verify(inventarioService, never()).save(any(Inventario.class));
    }
}