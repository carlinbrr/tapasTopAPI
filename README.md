# TapasTop API
Se trata de una API REST para el proyecto de Ingeniería de Sofware II. La documentación está en el archivo "2223_Enunciado practica.pdf".

## Tecnologías
Las principales herramientas que se han utilizado para el desarrollo del proyecto han sido Java como lenguaje de programación, Spring como framework y MySQL como servidor para la base de datos. Por otro lado, también se han utilizado otras tecnologías como Spring JPA, para el manejo de la base de datos y Spring Security, junto JWT (Json Web Token), para proporcionar seguridad a la API.

## Endpoints

### Registro
* **URI**: host/api/registrar 
* **Cabeceras**: Content Type: multipart/form-data, Accept: application/json
* **Método**: POST
* **Respuesta**: 201 Created, 400 Bad Request

### Login
* **URI**: host/api/login
* **Cabeceras**: Content Type: application/json, Accept: application/json	POST	200 OK
* **Método**: POST
* **Respuesta**: 200 OK, 400 Bad Request

### Confirmar cuenta
* **URI**: host/api/confirmarCuenta
* **Cabeceras**: Content Type: text/plain, Accept: application/json	
* **Método**: POST
* **Respuesta**: 200 OK, 400 Bad Request

### Recuperar contraseña
* **URI**: host/api/recuperarContraseña
* **Cabeceras**: Content Type: text/plain, Accept: application/json
* **Método**: POST
* **Respuesta**: 200 OK, 400 Bad Request

### Verificar token
* **URI**: host/api/verificarToken
* **Cabeceras**: Content Type: text/plain, Accept: application/json
* **Método**: POST
* **Respuesta**: 200 OK, 400 Bad Request

### Cambiar contraseña
* **URI**: host/api/usuarios/me/cambiarContraseña
* **Cabeceras**: Authorization: Bearer token, Content Type: text/plain, Accept: application/json
* **Método**: POST
* **Respuesta**: 200 OK, 401 Unauthorized

### Ver mi usuario
* **URI**: host/api/usuarios/me
* **Cabeceras**: Authorization: Bearer token, Accept: application/json
* **Método**: GET
* **Respuesta**: 200 OK, 404 Not found, 401 Unauthorized

### Crear una degustación
* **URI**: host/api/usuarios/me/degustaciones
* **Cabeceras**: Authorization: Bearer token, Content Type: multipart/form-data, Accept: application/json
* **Método**: POST
* **Respuesta**: 201 Created, 400 Bad Request, 401 Unauthorized

### Crear una valoración
* **URI**: host/api/usuarios/me/valoraciones
* **Cabeceras**: Authorization: Bearer token, Content Type: application/json, Accept: application/json
* **Método**: POST
* **Respuesta**: 201 Created, 400 Bad Request, 401 Unauthorized

### Ver una degustacion
* **URI**: host/api/locales/{localName}/degustaciones/{plateName}
* **Cabeceras**: Authorization: Bearer token, Accept: application/json
* **Método**: GET
* **Respuesta**: 200 OK, 404 Not found, 401 Unauthorized

