# MiniMarket Plus — Backend documentado con OpenAPI (Semana 7)

Microservicio backend de **MiniMarket Plus** (Desarrollo Backend II, PBY2202 — Duoc UC).
Esta entrega corresponde a la **Experiencia 3, Semana 7: Documentando Microservicios con OpenAPI**.

## Objetivo de la semana

Documentar la arquitectura y los endpoints del microservicio utilizando **OpenAPI Specification
(OAS 3)**, asegurando la consistencia y la alineación con los estándares. La documentación se
genera automáticamente desde el código con **springdoc-openapi** y se publica como un portal
interactivo (Swagger UI) y un documento JSON (`/v3/api-docs`).

## Stack

- Java 17 · Spring Boot 3.4.1 · Maven
- Spring Web · Spring Data JPA · H2 (en memoria)
- Spring Security (autenticación HTTP Basic)
- **springdoc-openapi-starter-webmvc-ui 2.7.0** (OpenAPI / Swagger UI)

## Cómo ejecutar

```bash
./mvnw spring-boot:run
```

La aplicación arranca en `http://localhost:8080` y carga datos semilla (un usuario, una categoría
y un producto de ejemplo).

## Documentación de la API

| Recurso | URL |
|---|---|
| **Swagger UI** (portal interactivo) | http://localhost:8080/swagger-ui/index.html |
| **Documento OpenAPI (JSON)** | http://localhost:8080/v3/api-docs |
| Consola H2 | http://localhost:8080/h2-console |

Las rutas de Swagger y del documento OpenAPI son públicas; el resto de los endpoints requieren
autenticación. Para probarlos, pulsa **Authorize** en Swagger UI e ingresa las credenciales
semilla:

- Usuario: `admin`
- Contraseña: `admin123`

## Recursos documentados

Se documentaron **34 operaciones** sobre 8 recursos, con `@Tag`, `@Operation`, `@ApiResponses`,
`@Parameter`, ejemplos de cuerpo de petición (`@ExampleObject`) y modelos con `@Schema`:

`Productos` · `Categorias` · `Inventario` · `Carrito` · `Ventas` · `Detalle de ventas` ·
`Usuarios` · `Publico`.

El archivo [`openapi.json`](./openapi.json) contiene la especificación completa generada,
lista para importar en Postman o para generar clientes.

## Estructura

```
src/main/java/com/minimarket/
├── config/          OpenApiConfig (metadatos OAS) · DataLoader (datos semilla)
├── controller/      8 controladores REST documentados con OpenAPI
├── entity/          8 entidades JPA anotadas con @Schema
├── repository/      Repositorios Spring Data JPA
├── service/         Lógica de negocio
└── security/        Configuración de Spring Security (HTTP Basic)
```
