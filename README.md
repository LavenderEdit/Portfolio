<p align="center">
    <img src="https://drive.google.com/uc?export=view&id=1TuT30CiBkinh85WuTvjKGKN47hCyCS0Z" width="300" alt="Studios TKOH Logo">
</p>

# Portfolio Hub API

**API REST multiusuario** construida con **Spring Boot 4**, **Spring Security**, **JWT en cookies seguras**, **MySQL**, **Flyway** y **Google Drive API** para crear, administrar y publicar portafolios profesionales.

[English version HERE!](README.en.md)

---

## Descripción

Portfolio Hub API es el backend de una plataforma para gestionar portafolios profesionales. Cada usuario puede administrar su perfil, experiencia, educación, proyectos, certificados, redes sociales, habilidades y archivos asociados.

El proyecto está preparado para ejecutarse en **Docker como entorno principal de producción local**, exponiendo la API en `localhost:8080`. Puede colocarse detrás de un túnel externo o proxy HTTPS sin incluir dependencias específicas de Cloudflare dentro del proyecto.

---

## Características principales

### Autenticación y seguridad

- Registro e inicio de sesión por email y password.
- JWT con access token y refresh token.
- Cookies `HttpOnly`, `Secure` y `SameSite` configurables.
- Compatibilidad temporal con `Authorization: Bearer`.
- Refresh tokens rotativos con revocación.
- Logout individual y logout global.
- CSRF habilitado para flujos con cookies.
- Rate limiting inicial para endpoints sensibles.
- Headers de seguridad configurados.
- Manejo de errores estándar con `requestId`.

### Verificación de correo

- Verificación de email con tokens hasheados.
- Reenvío de verificación con respuesta genérica para evitar enumeración.
- Estado de verificación desde endpoint privado.
- Preparado para bloquear acciones sensibles si el email no está verificado.

### OAuth2 con Google

- Login con Google OAuth2.
- Vinculación segura por identidad externa.
- Asociación por email verificado cuando corresponde.
- Emisión de las mismas cookies internas que el login tradicional.
- Redirects controlados por lista blanca.

### Gestión de portafolio

Endpoints privados bajo `/api/me/**` para administrar:

- Perfil profesional.
- Experiencia laboral.
- Educación.
- Proyectos.
- Certificados.
- Redes sociales.
- Categorías de skills.
- Skills individuales.
- Avatar, CV, portadas, iconos y certificados.

### API pública

Endpoints públicos bajo `/api/portfolios/**` para consumir portafolios publicados:

- Listado paginado de portafolios.
- Detalle por slug.
- Proyectos publicados.
- Formulario público de contacto.

### Uploads seguros

- Validación por tamaño, extensión, MIME real y magic bytes.
- Imágenes permitidas: `png`, `jpg`, `jpeg`, `webp`.
- Documentos permitidos: `pdf`.
- SVG bloqueado.
- Avatares, covers e iconos pueden ser públicos.
- CV y certificados se manejan como privados por defecto.
- Metadatos persistidos en base de datos.
- Google Drive puede subir archivos públicos o privados según el tipo.

### Producción con Docker

- `docker-compose.yml` preparado para API + MySQL.
- Perfil `prod` soportado.
- Puerto interno fijo `8080`.
- `.dockerignore` incluido para evitar subir basura al build.
- Swagger deshabilitado en producción.
- Actuator expone solo `health` e `info`.
- CORS soporta múltiples URLs separadas por comas.

---

## Tecnologías utilizadas

| Categoría | Tecnología |
|---|---|
| Backend | Spring Boot 4 |
| Seguridad | Spring Security 7 |
| Auth | JWT, cookies seguras, OAuth2 Client |
| Datos | Spring Data JPA, Hibernate |
| Base de datos | MySQL 8 |
| Migraciones | Flyway |
| DTO Mapping | MapStruct |
| Utilidades | Lombok |
| Validación | Jakarta Bean Validation |
| Uploads | Google Drive API v3 |
| Email | Spring Mail / SMTP |
| Documentación dev | SpringDoc OpenAPI |
| Observabilidad | Spring Boot Actuator |
| Contenedores | Docker, Docker Compose |

---

## Base de datos

El esquema principal se administra con Flyway desde:

```text
src/main/resources/db/migration
```

Tablas principales:

```text
app_user
user_identity
refresh_token
email_verification_token
profile
project
skill_category
skill
global_skill
experience
education
certificate
social_link
contact_message
stored_file
project_skill
```

La base incluye soporte para:

- Usuarios internos y usuarios OAuth2.
- Tokens de sesión/refresh.
- Tokens de verificación de email.
- Portafolios publicados, privados o borrador.
- Archivos subidos con metadatos.
- Relación entre proyectos y skills.
- Índices para búsquedas públicas, ownership y ordenamiento.

---

## Requisitos previos

Para ejecución local sin Docker:

- Java 21.
- Maven 3.9+.
- MySQL 8.
- Credenciales SMTP.
- Credenciales Google Drive OAuth2 si se habilitan uploads reales.
- Credenciales Google OAuth2 si se habilita login con Google.

Para ejecución con Docker:

- Docker.
- Docker Compose.
- Archivo `.env-prod` configurado.

---

## Variables de entorno

Usa `.env.example` como base. No subas `.env`, `.env-dev` ni `.env-prod` al repositorio.

Variables principales:

```bash
# Spring
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

# MySQL
MYSQL_HOST=mysql-db
MYSQL_PORT=3306
MYSQL_DATABASE=studiostkoh.portafolio
MYSQL_USER=your_mysql_user
MYSQL_PASSWORD=your_mysql_password
MYSQL_ROOT_PASSWORD=your_mysql_root_password
MYSQL_CONNECTION_PARAMS=useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC

# JWT
JWT_TOKEN=your_base64_or_strong_jwt_secret
JWT_EXPIRATION_TIME=15
JWT_REFRESH_EXPIRATION_DAYS=30
JWT_ISSUER=portfolio-hub-api
JWT_AUDIENCE=portfolio-hub

# Cookies
COOKIE_SECURE=true
COOKIE_SAME_SITE=None
COOKIE_DOMAIN=

# CORS
CORS_ALLOWED_ORIGINS=https://frontend-one.example.com,https://frontend-two.example.com

# Frontend redirects
FRONTEND_ALLOWED_REDIRECTS=https://frontend-one.example.com,https://frontend-two.example.com
FRONTEND_DEFAULT_REDIRECT=https://frontend-one.example.com

# Email
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
GMAIL_APP_EMAIL=your_email@example.com
GMAIL_APP_PASSWORD=your_app_password

# Email verification
EMAIL_VERIFICATION_EXPIRATION_MINUTES=1440
EMAIL_VERIFICATION_FRONTEND_URL=https://frontend-one.example.com/verify-email

# Google OAuth2 login
GOOGLE_OAUTH_CLIENT_ID=your_google_oauth_client_id
GOOGLE_OAUTH_CLIENT_SECRET=your_google_oauth_client_secret

# Google Drive
GOOGLE_DRIVE_ENABLED=true
DRIVE_OAUTH_CLIENT_ID=your_drive_client_id
DRIVE_OAUTH_CLIENT_SECRET=your_drive_client_secret
DRIVE_OAUTH_REFRESH_TOKEN=your_drive_refresh_token
DRIVE_FOLDER_USER_AVATARS=folder_id
DRIVE_FOLDER_USER_RESUMES=folder_id
DRIVE_FOLDER_PROJECTS_COVER=folder_id
DRIVE_FOLDER_SKILLS_ICON=folder_id
DRIVE_FOLDER_CERTIFICATES=folder_id

# Upload limits
UPLOAD_MAX_IMAGE_SIZE=2MB
UPLOAD_MAX_DOCUMENT_SIZE=5MB

# Actuator
ACTUATOR_EXPOSE_DETAILS=never
```

Nota: para Docker local con MySQL dentro de la misma red de Compose, puede usarse `useSSL=false`. Si MySQL se mueve a un proveedor externo o conexión remota, se recomienda habilitar TLS.

---

## Ejecución local

```bash
mvn spring-boot:run
```

La API quedará disponible en:

```text
http://localhost:8080
```

Swagger UI en modo desarrollo:

```text
http://localhost:8080/swagger-ui/index.html
```

En producción, Swagger queda deshabilitado.

---

## Ejecución con Docker

Levantar producción local:

```bash
docker compose --env-file .env-prod up --build -d
```

Ver contenedores:

```bash
docker compose --env-file .env-prod ps
```

Ver logs:

```bash
docker logs -f portfolio-api-container
```

Apagar:

```bash
docker compose --env-file .env-prod down
```

La API queda disponible en:

```text
http://localhost:8080
```

Health check:

```text
http://localhost:8080/actuator/health
```

---

## Endpoints principales

### Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| POST | `/api/auth/register` | Registrar usuario |
| POST | `/api/auth/login` | Login con email/password |
| POST | `/api/auth/refresh` | Rotar refresh token |
| POST | `/api/auth/logout` | Cerrar sesión actual |
| POST | `/api/auth/logout-all` | Cerrar todas las sesiones |
| POST | `/api/auth/verify-email` | Verificar correo |
| POST | `/api/auth/resend-verification` | Reenviar verificación |
| GET | `/oauth2/authorization/google` | Login con Google |

### Usuario autenticado

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/me` | Obtener perfil autenticado |
| PUT | `/api/me` | Actualizar perfil |
| GET | `/api/me/verification-status` | Estado de verificación de email |
| GET | `/api/me/files/{storedFileId}` | Descargar archivo privado |
| POST | `/api/me/upload/avatar` | Subir avatar |
| POST | `/api/me/upload/resume` | Subir CV |
| POST | `/api/me/upload/projects/{id}/cover` | Subir cover de proyecto |
| POST | `/api/me/upload/skills/{id}/icon` | Subir icono de skill |
| POST | `/api/me/upload/certificates/{id}` | Subir certificado |

### Gestión privada

| Recurso | Base path |
|---|---|
| Experiencia | `/api/me/experiences` |
| Educación | `/api/me/educations` |
| Proyectos | `/api/me/projects` |
| Certificados | `/api/me/certificates` |
| Redes sociales | `/api/me/social-links` |
| Categorías y skills | `/api/me/skills` |

### API pública

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/api/portfolios` | Listado paginado de portafolios publicados |
| GET | `/api/portfolios/{slug}` | Detalle público de portafolio |
| GET | `/api/portfolios/{profileSlug}/projects` | Proyectos publicados |
| GET | `/api/portfolios/{profileSlug}/projects/{projectSlug}` | Detalle de proyecto |
| POST | `/api/portfolios/{slug}/contact` | Enviar mensaje de contacto |

### Actuator

| Método | Endpoint | Descripción |
|---|---|---|
| GET | `/actuator/health` | Estado de salud |
| GET | `/actuator/info` | Información básica |

---

## Seguridad en producción

En perfil `prod`:

- Swagger/OpenAPI queda deshabilitado.
- CORS exige origins explícitos.
- Cookies deben usar `Secure=true` si se exponen por HTTPS.
- Errores internos no se devuelven al cliente.
- Actuator expone solo `health` e `info`.
- El backend está preparado para funcionar detrás de proxy HTTPS mediante forward headers.

---

## Testing

Ejecutar pruebas:

```bash
mvn test
```

Verificación completa:

```bash
mvn verify
```

Las pruebas cubren:

- Contexto Spring con perfil test.
- CORS.
- Cookies de autenticación.
- Rate limiting.
- Servicio noop de Google Drive.
- Verificación de email.
- Validación segura de archivos.

---

## Estado del proyecto

Portfolio Hub API está orientado a ser el backend principal de una plataforma de portafolios profesionales. Actualmente soporta autenticación moderna, administración privada de portafolios, publicación pública, uploads seguros, verificación de email y ejecución productiva local con Docker.

---

<p align="center">
  <sub>Desarrollado con pasión por <strong>Juan S Pimentel Lalangui</strong></sub><br>
</p>
