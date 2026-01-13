<p align="center">
  <a href="https://endearing-blini-6a6b91.netlify.app/" target="_blank">
    <img src="https://drive.google.com/uc?export=view&id=1TuT30CiBkinh85WuTvjKGKN47hCyCS0Z" width="300" alt="Studios TKOH Logo">
  </a>
</p>

# 🎯 Portfolio Hub API

**API REST** construida con **Spring Boot 3**, **Spring Security (JWT)** y **Spring Data JPA** para gestionar y exponer múltiples portafolios profesionales.

[➡️ English version HERE!](README.en.md)

---

## 🚀 Características Principales

### 🔒 API Segura (Backend)
Toda la lógica de negocio, seguridad y acceso a datos está en este proyecto.

### 🔑 Autenticación JWT
Registro e inicio de sesión (`/api/auth`) que genera un **JSON Web Token** para asegurar los endpoints.

### 🧑‍💼 Gestión de Portafolio
Endpoints privados (`/api/me/**`) para que los usuarios autenticados puedan crear, leer, actualizar y eliminar (CRUD) todos los aspectos de su portafolio:

- Perfil (**Profile**)
- Experiencia (**Experience**)
- Educación (**Education**)
- Redes Sociales (**SocialLink**)
- Certificados (**Certificate**)
- Proyectos (**Project**)
- Habilidades (por Categorías) (**SkillCategory**, **Skill**)

### 🌍 API Pública
Endpoints públicos (`/api/portfolios/**`) que permiten a cualquier cliente visualizar los datos de los portafolios:
- Lista de perfiles.
- Detalle de un perfil.
- Detalle de un proyecto.

### ☁️ Gestión de Archivos con Google Drive
Integración completa para subir archivos (avatares, currículums, portadas de proyectos, iconos) a **Google Drive** mediante su API, guardando únicamente la **URL pública** en la base de datos.

### 📧 Notificaciones por Email
Envío de correos (por ejemplo, desde un formulario de contacto) usando **Spring Mail**.

### 🗄️ Base de Datos y Migraciones
Usa **MySQL** con **Flyway** para una gestión de esquemas y migraciones versionadas.

---

## 🧩 Tecnologías Utilizadas

| Categoría | Tecnología |
|------------|-------------|
| **Backend** | Spring Boot 3 |
| **Seguridad** | Spring Security 6 (JWT) |
| **Datos** | Spring Data JPA (Hibernate) |
| **Base de Datos** | MySQL |
| **Migraciones** | Flyway |
| **Mapeo de DTOs** | MapStruct |
| **Utilidades** | Lombok |
| **Validación** | Jakarta Bean Validation |
| **Uploads** | Google Drive API v3 |
| **Email** | Spring Boot Mail (SMTP) |
| **Documentación** | SpringDoc (Swagger UI) |

---

## 🧱 Base de Datos y Migraciones

- **Esquema:** `studiostkoh.portafolio` (definido en `V1__init.sql`).
- **Tablas Principales:**  
  `app_user`, `profile`, `social_link`, `skill_category`, `skill`, `project`,  
  `experience`, `education`, `contact_message`, `certificate`, `project_skill`.
- **Migraciones:**  
  Gestionadas automáticamente por **Flyway**, ubicadas en  
  `src/main/resources/db/migration`.

---

## ⚙️ Requisitos Previos

- **Java 21 (JDK)**  
- **Maven 3.9+**  
- **MySQL 8.0+** (con un esquema vacío, ej. `studiostkoh.portafolio`)  
- **Credenciales de Google Cloud Platform (OAuth)** para la API de Google Drive  
- **Credenciales SMTP** (por ejemplo, Gmail App Password) para envío de correos

---

## 🔧 Configuración y Variables de Entorno

La aplicación espera las siguientes variables de entorno (o propiedades en `application.properties`):

```bash
# Base de Datos MySQL
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DATABASE=studiostkoh.portafolio
export MYSQL_USER=tu_usuario
export MYSQL_PASSWORD=tu_password

# Seguridad JWT
export JWT_TOKEN=tu_clave_secreta_larga_para_jwt
export JWT_EXPIRATION_TIME=60 # (En minutos)

# Google Drive (OAuth 2.0)
export DRIVE_OAUTH_CLIENT_ID=tu_client_id
export DRIVE_OAUTH_CLIENT_SECRET=tu_client_secret
export DRIVE_OAUTH_REFRESH_TOKEN=tu_refresh_token

# Google Drive (IDs de Carpetas)
export DRIVE_FOLDER_USER_AVATARS=id_carpeta_avatares
export DRIVE_FOLDER_USER_RESUMES=id_carpeta_resumes
export DRIVE_FOLDER_PROJECTS_COVER=id_carpeta_covers
export DRIVE_FOLDER_SKILLS_ICON=id_carpeta_iconos
export DRIVE_FOLDER_CERTIFICATES=id_carpeta_certificados

# Email (SMTP)
export SMTP_HOST=smtp.gmail.com
export SMTP_PORT=587
export GMAIL_APP_EMAIL=tu_email@gmail.com
export GMAIL_APP_PASSWORD=tu_google_app_password
````

---

## 🧠 Ejecución Local

1. Clona el repositorio.
2. Configura las variables de entorno mencionadas.
3. Asegúrate de que tu servidor MySQL esté en línea y el esquema exista.
4. Ejecuta la aplicación:

```bash
./mvnw spring-boot:run
```

* La API estará disponible en: [http://localhost:8080](http://localhost:8080)
* Documentación (Swagger UI):
  [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📚 Estructura de Endpoints (Resumen)

### 🔐 Autenticación

| Método | Endpoint             | Descripción                    |
| ------ | -------------------- | ------------------------------ |
| POST   | `/api/auth/register` | Registro de nuevo usuario      |
| POST   | `/api/auth/login`    | Inicio de sesión, devuelve JWT |

### 👤 Usuario Autenticado (`/api/me/**`)

| Entidad                   | Métodos Disponibles                                 |
| ------------------------- | --------------------------------------------------- |
| **Profile**               | `GET`, `PUT`                                        |
| **Experience**            | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Education**             | `GET`, `POST`, `PUT`, `DELETE`                      |
| **SocialLink**            | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Project**               | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Certificate**           | `GET`, `POST`, `PUT`, `DELETE`                      |
| **SkillCategory / Skill** | `GET`, `POST /batch`, `PUT /batch`, `DELETE /batch` |
| **Uploads**               | `POST /api/me/upload/...`                           |

### 🌐 API Pública (`/api/portfolios/**`)

| Método | Endpoint                                               | Descripción                  |
| ------ | ------------------------------------------------------ | ---------------------------- |
| GET    | `/api/portfolios`                                      | Lista todos los perfiles     |
| GET    | `/api/portfolios/{slug}`                               | Detalle de un portafolio     |
| GET    | `/api/portfolios/{profileSlug}/projects/{projectSlug}` | Detalle de un proyecto       |
| POST   | `/api/portfolios/{slug}/contact`                       | Envía un mensaje de contacto |

### 🛠️ Administración

| Método | Endpoint                                              | Descripción                           |
| ------ | ----------------------------------------------------- | ------------------------------------- |
| POST   | `/api/admin/profiles/{profileId}/toggle-collaborator` | Endpoint de ejemplo para rol de Admin |

---

<p align="center">
  <sub>🛠️ Desarrollado con 💙 por <strong>Studios TKOH</strong></sub><br>
</p>
