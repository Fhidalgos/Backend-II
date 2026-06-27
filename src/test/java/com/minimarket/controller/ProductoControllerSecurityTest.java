package com.minimarket.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minimarket.entity.Producto;
import com.minimarket.security.config.SecurityConfig;
import com.minimarket.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductoController.class)
@Import(SecurityConfig.class)
class ProductoControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductoService productoService;

    private Producto productoExistente;
    private Producto productoActualizado;

    @BeforeEach
    void setUp() {

        productoExistente = new Producto();
        productoExistente.setId(1L);
        productoExistente.setNombre("Arroz");
        productoExistente.setPrecio(1800.0);
        productoExistente.setStock(20);

        productoActualizado = new Producto();
        productoActualizado.setNombre("Arroz Premium");
        productoActualizado.setPrecio(2200.0);
        productoActualizado.setStock(30);
    }

    @Test
    @WithMockUser(
            username = "administrador",
            authorities = {"ADMINISTRADOR"}
    )
    void administradorPuedeActualizarProducto() throws Exception {

        when(productoService.findById(1L))
                .thenReturn(productoExistente);

        when(productoService.save(any(Producto.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        mockMvc.perform(
                        put("/api/productos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productoActualizado))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Arroz Premium"))
                .andExpect(jsonPath("$.precio").value(2200.0))
                .andExpect(jsonPath("$.stock").value(30));
    }

    @Test
    @WithMockUser(
            username = "cliente",
            authorities = {"CLIENTE"}
    )
    void clienteNoPuedeActualizarProducto() throws Exception {

        mockMvc.perform(
                        put("/api/productos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productoActualizado))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void usuarioNoAutenticadoEsRedirigidoAlLogin() throws Exception {

        mockMvc.perform(
                        put("/api/productos/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(productoActualizado))
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(
            username = "cliente",
            authorities = {"CLIENTE"}
    )
    void clienteAutenticadoPuedeListarProductos() throws Exception {

        when(productoService.findAll())
                .thenReturn(List.of(productoExistente));

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Arroz"))
                .andExpect(jsonPath("$[0].precio").value(1800.0))
                .andExpect(jsonPath("$[0].stock").value(20));
    }
}