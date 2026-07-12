package com.minimarket.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuracion central de la documentacion OpenAPI (OAS 3) del microservicio
 * MiniMarket Plus.
 *
 * <p>Aqui se definen los elementos clave de la especificacion segun la guia de
 * la Semana 7:</p>
 * <ul>
 *   <li><b>info</b>: titulo, version (SemVer 1.0.0), descripcion, contacto y licencia.</li>
 *   <li><b>servers</b>: URL base del entorno de desarrollo.</li>
 *   <li><b>security</b>: esquema de autenticacion HTTP Basic reutilizable, que
 *       permite probar los endpoints protegidos desde Swagger UI y Postman con
 *       el boton "Authorize".</li>
 * </ul>
 */
@Configuration
public class OpenApiConfig {

    /** Nombre del esquema de seguridad reutilizable, referenciado por Swagger UI. */
    private static final String ESQUEMA_BASIC = "autenticacionBasica";

    @Bean
    public OpenAPI miniMarketOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API MiniMarket Plus")
                        .version("1.0.0")
                        .description("""
                                Microservicio backend de MiniMarket Plus. Expone la gestion de
                                productos, categorias, inventario, carrito de compras, ventas,
                                detalle de ventas y usuarios del sistema.

                                Esta documentacion se genera con OpenAPI Specification (OAS 3)
                                mediante springdoc-openapi, asegurando la consistencia entre el
                                codigo y la documentacion de la API.""")
                        .contact(new Contact()
                                .name("Equipo Backend - MiniMarket Plus")
                                .email("soporte@minimarketplus.cl"))
                        .license(new License()
                                .name("Uso academico Duoc UC")
                                .url("https://www.duoc.cl")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Entorno de desarrollo local")))
                // Aplica el esquema de seguridad a nivel global para que Swagger UI
                // muestre el candado en las operaciones protegidas.
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA_BASIC))
                .components(new Components()
                        .addSecuritySchemes(ESQUEMA_BASIC, new SecurityScheme()
                                .name(ESQUEMA_BASIC)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")
                                .description("Autenticacion HTTP Basic. Use el usuario y la "
                                        + "contrasena semilla (admin / admin123) en el boton "
                                        + "Authorize para consumir los endpoints protegidos.")));
    }
}
