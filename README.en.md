<p align="center">
    <img src="https://drive.google.com/uc?export=view&id=1TuT30CiBkinh85WuTvjKGKN47hCyCS0Z" width="300" alt="Studios TKOH Logo">
</p>

# Portfolio Hub API

**Multi-user REST API** built with **Spring Boot 4**, **Spring Security**, **secure JWT cookies**, **MySQL**, **Flyway**, and the **Google Drive API** to create, manage, and publish professional portfolio hubs.

[Version en Espanol aqui](README.md)

---

## Overview

Portfolio Hub API is the backend for a professional portfolio platform. Each user can manage their profile, work experience, education, projects, certificates, social links, skills, and related files.

The project is prepared to run with **Docker as the main local production mode**, exposing the API on `localhost:8080`. It can sit behind an external HTTPS tunnel or reverse proxy without adding Cloudflare-specific dependencies to the repository.

---

## Core Features

### Authentication and Security

- Email/password registration and login.
- JWT access tokens and refresh tokens.
- Configurable `HttpOnly`, `Secure`, and `SameSite` cookies.
- Temporary compatibility with `Authorization: Bearer`.
- Rotating refresh tokens with revocation support.
- Single-session logout and global logout.
- CSRF protection for cookie-based flows.
- Initial rate limiting for sensitive endpoints.
- Security headers.
- Standard error responses with `requestId`.

### Email Verification

- Email verification with hashed tokens.
- Generic resend responses to avoid email enumeration.
- Private endpoint for checking verification status.
- Prepared to block sensitive actions when `emailVerified=false`.

### Google OAuth2

- Google OAuth2 login.
- Secure external identity linking.
- Verified-email matching when appropriate.
- Same internal session cookie model as email/password login.
- Redirects restricted by an allow-list.

### Portfolio Management

Private endpoints under `/api/me/**` allow authenticated users to manage:

- Professional profile.
- Work experience.
- Education.
- Projects.
- Certificates.
- Social links.
- Skill categories.
- Individual skills.
- Avatar, resume, project covers, skill icons, and certificate files.

### Public API

Public endpoints under `/api/portfolios/**` expose published portfolio data:

- Paginated portfolio listing.
- Portfolio detail by slug.
- Published projects.
- Public contact form.

### Secure Uploads

- Validation by size, extension, real MIME type, and magic bytes.
- Allowed image formats: `png`, `jpg`, `jpeg`, `webp`.
- Allowed document format: `pdf`.
- SVG is blocked.
- Avatars, covers, and icons can be public.
- Resumes and certificates are private by default.
- File metadata is persisted in the database.
- Google Drive uploads can be public or private depending on file type.

### Docker Production Setup

- `docker-compose.yml` for API + MySQL.
- `prod` profile support.
- Internal container port fixed at `8080`.
- `.dockerignore` included to keep local noise out of builds.
- Swagger disabled in production.
- Actuator exposes only `health` and `info`.
- CORS supports multiple comma-separated frontend URLs.

---

## Technology Stack

| Category | Technology |
|---|---|
| Backend | Spring Boot 4 |
| Security | Spring Security 7 |
| Auth | JWT, secure cookies, OAuth2 Client |
| Data | Spring Data JPA, Hibernate |
| Database | MySQL 8 |
| Migrations | Flyway |
| DTO Mapping | MapStruct |
| Utilities | Lombok |
| Validation | Jakarta Bean Validation |
| Uploads | Google Drive API v3 |
| Email | Spring Mail / SMTP |
| Dev Documentation | SpringDoc OpenAPI |
| Observability | Spring Boot Actuator |
| Containers | Docker, Docker Compose |

---

## Database

The main schema is managed with Flyway from:

```text
src/main/resources/db/migration
```

Core tables:

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

The schema supports:

- Internal users and OAuth2 users.
- Session and refresh tokens.
- Email verification tokens.
- Published, private, and draft portfolio states.
- Uploaded files with metadata.
- Project-skill relationships.
- Indexes for public search, ownership checks, and ordering.

---

## Prerequisites

For local execution without Docker:

- Java 21.
- Maven 3.9+.
- MySQL 8.
- SMTP credentials.
- Google Drive OAuth2 credentials if real uploads are enabled.
- Google OAuth2 credentials if Google login is enabled.

For Docker execution:

- Docker.
- Docker Compose.
- Configured `.env-prod` file.

---

## Environment Variables

Use `.env.example` as the base. Do not commit `.env`, `.env-dev`, or `.env-prod`.

Main variables:

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

Note: for local Docker with MySQL inside the same Compose network, `useSSL=false` can be used. If MySQL is moved to an external provider or remote connection, TLS is recommended.

---

## Local Execution

```bash
mvn spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

Swagger UI in development mode:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger is disabled in production.

---

## Docker Execution

Start local production:

```bash
docker compose --env-file .env-prod up --build -d
```

List containers:

```bash
docker compose --env-file .env-prod ps
```

View logs:

```bash
docker logs -f portfolio-api-container
```

Stop:

```bash
docker compose --env-file .env-prod down
```

The API will be available at:

```text
http://localhost:8080
```

Health check:

```text
http://localhost:8080/actuator/health
```

---

## Main Endpoints

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Email/password login |
| POST | `/api/auth/refresh` | Rotate refresh token |
| POST | `/api/auth/logout` | Log out current session |
| POST | `/api/auth/logout-all` | Log out all sessions |
| POST | `/api/auth/verify-email` | Verify email address |
| POST | `/api/auth/resend-verification` | Resend verification email |
| GET | `/oauth2/authorization/google` | Google login |

### Authenticated User

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/me` | Get authenticated profile |
| PUT | `/api/me` | Update profile |
| GET | `/api/me/verification-status` | Check email verification status |
| GET | `/api/me/files/{storedFileId}` | Download private file |
| POST | `/api/me/upload/avatar` | Upload avatar |
| POST | `/api/me/upload/resume` | Upload resume |
| POST | `/api/me/upload/projects/{id}/cover` | Upload project cover |
| POST | `/api/me/upload/skills/{id}/icon` | Upload skill icon |
| POST | `/api/me/upload/certificates/{id}` | Upload certificate file |

### Private Portfolio Management

| Resource | Base path |
|---|---|
| Experience | `/api/me/experiences` |
| Education | `/api/me/educations` |
| Projects | `/api/me/projects` |
| Certificates | `/api/me/certificates` |
| Social links | `/api/me/social-links` |
| Skill categories and skills | `/api/me/skills` |

### Public API

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/portfolios` | Paginated list of published portfolios (supports optional `search` query parameter to filter by full name) |
| GET | `/api/portfolios/{slug}` | Public portfolio detail |
| GET | `/api/portfolios/{profileSlug}/projects` | Published projects |
| GET | `/api/portfolios/{profileSlug}/projects/{projectSlug}` | Project detail |
| POST | `/api/portfolios/{slug}/contact` | Send contact message |

### Actuator

| Method | Endpoint | Description |
|---|---|---|
| GET | `/actuator/health` | Health status |
| GET | `/actuator/info` | Basic application info |

---

## Production Security

In the `prod` profile:

- Swagger/OpenAPI is disabled.
- CORS requires explicit origins.
- Cookies should use `Secure=true` when exposed through HTTPS.
- Internal errors are not returned to clients.
- Actuator exposes only `health` and `info`.
- The backend is prepared to work behind an HTTPS proxy through forwarded headers.

---

## Testing

Run tests:

```bash
mvn test
```

Run full verification:

```bash
mvn verify
```

Test coverage includes:

- Spring context with the test profile.
- CORS.
- Authentication cookies.
- Rate limiting.
- No-op Google Drive service.
- Email verification.
- Secure file validation.

---

## Project Status

Portfolio Hub API is designed as the main backend for a professional portfolio platform. It currently supports modern authentication, private portfolio administration, public publishing, secure uploads, email verification, and local production execution with Docker.

---

<p align="center">
  <sub>Built with passion by <strong>Juan S Pimentel Lalangui</strong></sub><br>
</p>
