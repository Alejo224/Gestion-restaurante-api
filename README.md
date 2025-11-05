# 🍽️ Gestión de Restaurante - API

API REST para la gestión de un sistema de restaurante desarrollada con **Spring Boot** y **PostgreSQL**, desplegable mediante **Docker Compose**.

Ahora incluye autenticación segura con Spring Security y tokens JWT, y está documentada con Swagger para facilitar su exploración y pruebas.

## 🔸 Spring Security + JWT

La API cuenta con un sistema de autenticación y autorización basado en JSON Web Tokens (JWT).
Esto permite proteger las rutas y garantizar que solo los usuarios autenticados puedan acceder a los recursos correspondientes.

### Características principales:

- Inicio de sesión mediante credenciales (email y contraseña).

- Generación de token JWT firmado.

- Validación automática del token en cada solicitud.

- Roles y permisos definidos por usuario.

- Filtros personalizados de autenticación y autorización.


## 🔸 Documentación con Swagger

La API está documentada con Swagger (Springdoc OpenAPI) para una navegación clara e interactiva de los endpoints.
Puedes acceder a la interfaz de Swagger una vez que la API esté en ejecución desde:

http://localhost:8080/swagger-ui.html

o en algunos casos:

http://localhost:8080/swagger-ui/index.html

### Desde ahí podrás:

- Probar los endpoints directamente.

- Vializar los modelos de datos y sus parámetros.

- Enviar el token JWT en el encabezado Authorization para probar rutas protegidas.


## 📌 Requisitos previos

Antes de ejecutar este proyecto asegúrate de tener instalado:

- [Docker](https://docs.docker.com/get-docker/)  
- [Docker Compose](https://docs.docker.com/compose/)  
- Una cuenta en [Docker Hub](https://hub.docker.com/) *(solo si vas a subir imágenes)*  

Además, este proyecto necesita la imagen oficial de **PostgreSQL**, pero Docker Compose la descargará automáticamente.

## 🚀 Instalación y uso

### 1. Clonar el repositorio
```bash
git clone https://github.com/Alejo224/Gestion-restaurante-api
cd Gestion-restaurante-api
```

### 2. Descargar la imagen de la API desde Docker Hub

El siguiente enlace habra unas secuencias de pasos para descargar la imagen y ejecutar los contenedores.

- [Descargar aqui](https://hub.docker.com/r/alejo224j/sistemarestaurante-api)

---

### 3. Poder trabajar en el repositorio
- [buenas practicas git](https://github.com/Alejo224/Inmobilaria/tree/buenas-practicas)

