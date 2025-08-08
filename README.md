# 📌 API de Franquicias

Este proyecto implementa una API para gestionar una red de franquicias, sucursales y productos, siguiendo los principios de **arquitectura hexagonal** y **programación reactiva** con Spring WebFlux.

---

## 📋 Requisitos

Para ejecutar el proyecto de forma local necesitas:

- Java 17 o superior
- Gradle (se incluye el wrapper `gradlew`)
- Docker
- Base de datos MySQL con el nombre `franquicias`
- Variables de entorno configuradas:

```bash
spring.application.name=franquicias_api  
spring.r2dbc.url=r2dbc:mysql://${DB_ENDPOINT}:3306/franquicias  
spring.r2dbc.username=${DB_USERNAME}  
spring.r2dbc.password=${DB_PASSWORD}
```


---

## 🏗 Estructura del Proyecto

El proyecto sigue una estructura de **Arquitectura Hexagonal** con las siguientes capas principales:

- **domain/** → Núcleo de la aplicación. Contiene:
    - Lógica de negocio pura (**Use Cases**)
    - Modelos de dominio (**Records**)
    - Contratos de las interfaces (**Puertos API y SPI**)

- **infrastructure/** → Capa de adaptadores. Contiene:
    - Implementaciones de puertos para tecnología específica
    - **Handlers** de WebFlux
    - **Mappers** de MapStruct
    - **Repositories** de Spring Data R2DBC

- **application/** → Capa de configuración. Une el dominio con la infraestructura mediante:
    - Inyección de dependencias (`@Configuration`, `@Bean`)

---

## 🚀 Cómo ejecutar el proyecto localmente

# Construir la imagen de Docker
```bash
docker build -t franquicias-api .
```

# Ejecutar el contenedor de la aplicación

```bash
docker run --name franquicias-app \
  -p 8080:8080 \
  --network=host \
  franquicias-api
```

La aplicación estará disponible en:
http://localhost:8080

# Probar los endpoints

Cuando la aplicación esté corriendo, accede a la documentación Swagger:

http://localhost:8080/swagger-ui.html

Desde esta interfaz podrás ver y probar todos los endpoints.

#  Endpoints Implementados
```bash
POST /franchises → Crea una nueva franquicia.

POST /franchises/{franchiseId}/branches → Agrega una sucursal a una franquicia.

POST /branches/{branchId}/products → Agrega un producto a una sucursal.

DELETE /branches/{branchId}/products/{productId} → Elimina un producto.

PUT /branches/{branchId}/products/{productId} → Actualiza el stock de un producto.

GET /franchises/{franchiseId}/top-products → Obtiene el producto con mayor stock por cada sucursal.

PUT /franchises/{franchiseId} → Actualiza el nombre de una franquicia.

PUT /branches/{branchId} → Actualiza el nombre de una sucursal.

PUT /products/{productId} → Actualiza el nombre de un producto.
```

## DDL BD

```bash
CREATE DATABASE  IF NOT EXISTS `franquicias` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `franquicias`;

--
-- Table structure for table `franquicias`
--

DROP TABLE IF EXISTS `franquicias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `franquicias` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `stock` int NOT NULL,
  `sucursal_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre_sucursal_id` (`sucursal_id`,`nombre`),
  CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`sucursal_id`) REFERENCES `sucursales` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `sucursales`
--

DROP TABLE IF EXISTS `sucursales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sucursales` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `franquicia_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `franquicia_id` (`franquicia_id`),
  CONSTRAINT `sucursales_ibfk_1` FOREIGN KEY (`franquicia_id`) REFERENCES `franquicias` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
```