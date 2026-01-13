<p align="center">
  <a href="https://endearing-blini-6a6b91.netlify.app/" target="_blank">
    <img src="https://drive.google.com/uc?export=view&id=1TuT30CiBkinh85WuTvjKGKN47hCyCS0Z" width="300" alt="Studios TKOH Logo">
  </a>
</p>

# 🌐 Portfolio Hub API (Spring Boot)

Full-Stack REST API built with **Spring Boot 3**, **Spring Security (JWT)**, and **Spring Data JPA** to manage and expose multiple professional portfolios.

[🇪🇸 Versión en Español ¡AQUI!](README.md)

---

## 🚀 Core Features

### 🔒 Secure API (Backend)
All business logic, security, and data access reside in this project.

### 🔑 JWT Authentication
Registration and login (`/api/auth`) generate a **JSON Web Token** to secure endpoints.

### 🧑‍💼 Portfolio Management
Private endpoints (`/api/me/**`) for authenticated users to **Create**, **Read**, **Update**, and **Delete (CRUD)** all aspects of their portfolio:

- Profile  
- Experience  
- Education  
- Social Links  
- Certificates  
- Projects  
- Skills (by Category)

### 🌍 Public API
Public endpoints (`/api/portfolios/**`) allow any client to view portfolio data:
- List of profiles
- Full profile detail
- Project detail

### ☁️ Google Drive File Management
Full integration to upload files (avatars, resumes, project covers, icons) to **Google Drive** via its API, saving only the **public URL** in the database.

### 📧 Email Notifications
Email sending (e.g., contact form submissions) via **Spring Mail**.

### 🗄️ Database & Migrations
Uses **MySQL** with **Flyway** for versioned schema management.

---

## 🧩 Technology Stack

| Category | Technology |
|-----------|-------------|
| **Backend** | Spring Boot 3 |
| **Security** | Spring Security 6 (JWT) |
| **Data** | Spring Data JPA (Hibernate) |
| **Database** | MySQL |
| **Migrations** | Flyway |
| **DTO Mapping** | MapStruct |
| **Utilities** | Lombok |
| **Validation** | Jakarta Bean Validation |
| **File Uploads** | Google Drive API v3 |
| **Email** | Spring Boot Mail (SMTP) |
| **Documentation** | SpringDoc (Swagger UI) |

---

## 🧱 Database & Migrations

- **Schema:** `studiostkoh.portafolio` (defined in `V1__init.sql`)  
- **Core Tables:**  
  `app_user`, `profile`, `social_link`, `skill_category`, `skill`,  
  `project`, `experience`, `education`, `contact_message`,  
  `certificate`, and the join table `project_skill`.  
- **Migrations:**  
  Handled automatically by **Flyway**. Files are located in:  
  `src/main/resources/db/migration`

---

## ⚙️ Prerequisites

- **Java 21 (JDK)**  
- **Maven 3.9+**  
- **MySQL 8.0+** server with an empty schema (e.g., `studiostkoh.portafolio`)  
- **Google Cloud Platform (OAuth)** credentials for Google Drive API  
- **SMTP server credentials** (e.g., Gmail App Password) for sending emails

---

## 🔧 Configuration & Environment Variables

The application expects the following environment variables  
(or equivalent properties in `application.properties`):

```bash
# MySQL Database
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DATABASE=studiostkoh.portafolio
export MYSQL_USER=your_user
export MYSQL_PASSWORD=your_password

# JWT Security
export JWT_TOKEN=your_long_secret_key_for_jwt
export JWT_EXPIRATION_TIME=60 # (In minutes)

# Google Drive (OAuth 2.0)
export DRIVE_OAUTH_CLIENT_ID=your_client_id
export DRIVE_OAUTH_CLIENT_SECRET=your_client_secret
export DRIVE_OAUTH_REFRESH_TOKEN=your_refresh_token

# Google Drive (Folder IDs)
export DRIVE_FOLDER_USER_AVATARS=id_folder_avatars
export DRIVE_FOLDER_USER_RESUMES=id_folder_resumes
export DRIVE_FOLDER_PROJECTS_COVER=id_folder_covers
export DRIVE_FOLDER_SKILLS_ICON=id_folder_icons
export DRIVE_FOLDER_CERTIFICATES=id_folder_certificates

# Email (SMTP)
export SMTP_HOST=smtp.gmail.com
export SMTP_PORT=587
export GMAIL_APP_EMAIL=your_email@gmail.com
export GMAIL_APP_PASSWORD=your_google_app_password
````

---

## 🧠 Local Execution

1. Clone the repository
2. Set up the environment variables mentioned above
3. Ensure your **MySQL** server is running and the schema exists
4. Run the application:

```bash
./mvnw spring-boot:run
```

* API available at: [http://localhost:8080](http://localhost:8080)
* Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

## 📚 Endpoint Structure (Summary)

### 🔐 Authentication

| Method   | Endpoint             | Description         |
| -------- | -------------------- | ------------------- |
| **POST** | `/api/auth/register` | Register a new user |
| **POST** | `/api/auth/login`    | Log in, returns JWT |

### 👤 Authenticated User (`/api/me/**`)

| Entity                        | Methods                                             |
| ----------------------------- | --------------------------------------------------- |
| **Profile**                   | `GET`, `PUT`                                        |
| **Experience**                | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Education**                 | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Social Links**              | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Projects**                  | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Certificates**              | `GET`, `POST`, `PUT`, `DELETE`                      |
| **Skill Categories / Skills** | `GET`, `POST /batch`, `PUT /batch`, `DELETE /batch` |
| **Uploads**                   | `POST /api/me/upload/...` (Avatar, Resume, etc.)    |

### 🌐 Public API (`/api/portfolios/**`)

| Method   | Endpoint                                               | Description            |
| -------- | ------------------------------------------------------ | ---------------------- |
| **GET**  | `/api/portfolios`                                      | List all profiles      |
| **GET**  | `/api/portfolios/{slug}`                               | Get portfolio details  |
| **GET**  | `/api/portfolios/{profileSlug}/projects/{projectSlug}` | Get project details    |
| **POST** | `/api/portfolios/{slug}/contact`                       | Send a contact message |

### 🛠️ Administration

| Method   | Endpoint                                              | Description                 |
| -------- | ----------------------------------------------------- | --------------------------- |
| **POST** | `/api/admin/profiles/{profileId}/toggle-collaborator` | Example Admin-only endpoint |

---

<p align="center">
  <sub>🛠️ Built with 💙 by <strong>Studios TKOH</strong></sub><br>
</p>
