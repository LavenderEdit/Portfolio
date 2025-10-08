# Portfolio Hub (Spring Boot)
Full-stack web application built with Spring Boot 3 and Thymeleaf to showcase multiple professional portfolios from a single deployment. It ships with dynamic pages, a persisted contact form, and rich sample data that highlights real developer profiles.

### [Version en Español ¡AQUI!](README.md)

# DEMO
[studios-tkoh](https://studios-tkoh.azurewebsites.net)

## Table of contents
- [Highlights](#highlights)
- [Architecture](#architecture)
- [Database & migrations](#database--migrations)
- [Bundled sample data](#bundled-sample-data)
- [Prerequisites](#prerequisites)
- [Configuration](#configuration)
- [Local run](#local-run)
- [Project structure](#project-structure)
- [Testing](#testing)
- [Next steps](#next-steps)

## Highlights
- **Multi-portfolio landing**: the home page lists all available profiles and redirects automatically when there is only one. Each profile lives under `/portfolio/{slug}` and renders dynamic headings, skills, projects, experience, education, and the contact form. [HomeController](src/main/java/com/portfolio/lavender/controller/HomeController.java) orchestrates the data loading.
- **Project deep dives**: rich sections with cover images, repository/demo links, and associated skills per project available at `/portfolio/{profileSlug}/projects/{slug}` via [ProjectController](src/main/java/com/portfolio/lavender/controller/ProjectController.java).
- **Validated contact form**: Jakarta Bean Validation guards the fields, while [ContactController](src/main/java/com/portfolio/lavender/controller/ContactController.java), the [ContactForm DTO](src/main/java/com/portfolio/lavender/dto/simple/ContactForm.java), and [ContactMessageRepo](src/main/java/com/portfolio/lavender/repository/ContactMessageRepo.java) persist the submissions.
- **Responsive dark UI**: [`static/css/site.css`](src/main/resources/static/css/site.css) defines a modern dark theme, and [`static/js/site.js`](src/main/resources/static/js/site.js) powers the accessible navigation toggle, smooth scrolling, and active-section highlighting.
- **Reusable Thymeleaf fragments**: shared layout, navbar, and footer fragments under `templates/fragments` keep `profiles`, `portfolio`, and `project` views consistent and easy to extend.

## Architecture
- **Web layer (MVC)**: Spring MVC controllers render Thymeleaf templates.
  - `HomeController` loads the profile list and individual portfolio page.
  - `ProjectController` presents the project detail view including SEO-friendly metadata.
  - `ContactController` processes form submissions and flashes validation feedback.
- **Domain layer**: JPA entities (`Profile`, `SocialLink`, `SkillCategory`, `Skill`, `Project`, `Experience`, `Education`, `ContactMessage`) model the portfolio content.
- **Persistence**: Spring Data JPA repositories leverage `@EntityGraph` to fetch related skills and social links efficiently.
- **Configuration**: `application.properties` enables Flyway, enforces schema validation, and wires MySQL placeholders. `application-dev` and `application-prod` tweak logging verbosity and the Hikari pool.
- **Key dependencies** (see [`pom.xml`](pom.xml)):
  - Spring Boot starters for Web, Data JPA, Validation, and Thymeleaf.
  - Flyway + flyway-mysql for versioned migrations.
  - Lombok to reduce boilerplate and MapStruct configuration ready for future DTO mappers.

## Database & migrations
- **Initial schema**: `V1__init.sql` creates tables for profiles, social links, skill categories, project catalog plus the `project_skill` join table, experience, education, and contact messages.
- **Multi-profile hardening**: `V2__multi_profile_support.sql` adds foreign keys, indexes, and composite uniqueness so every resource is tied to a specific profile and slugs remain unique per owner.
- **Versioned seeders**: migrations `V4` through `V8` rebuild dependent collections and inject coherent datasets (skills, projects, experience, education) for several personas, providing an attractive default demo on first run.

## Bundled sample data
- **Juan Santos Pimentel Lalangui** (`juan-lavender-1`): full-stack student with projects such as *Chattide Web*, *app-swing*, *Grading API*, and *BusquedaPokemon*, plus professional experience and SENATI studies.
- **Bryan Alexander Vidal Crispin** (`bryan-alexander-vidal-crispin`): web-focused developer showcasing React/Vite SPAs and utilities, with experience at SERVISERC, JHARDSYSTEX, and LUBRICANTES CLAUDIA.
- **Andriy Lionel Pastrana Cajavilca** (`andriy-lionel-pastrana-cajavilca`): frontend & automation enthusiast with numerous GitHub Pages deployments and hands-on work in Python, Java Swing, and React for business systems.

Flyway migrations wipe and re-seed dependent data to keep the dataset consistent, so applying them on clean or existing databases is safe.

## Prerequisites
- Java 21 JDK available on your `PATH`.
- Maven 3.9 or newer.
- MySQL 8.0+ instance (Docker works great) with an empty schema.

## Configuration
The application expects the following environment variables for the JDBC connection:

```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DATABASE=portfolio
export MYSQL_USER=portfolio_user
export MYSQL_PASSWORD=super_secure
```

Quick way to spin up MySQL with Docker:

```bash
docker run --name portfolio-mysql -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=portfolio \
  -p 3306:3306 -d mysql:8.0
```

Create a dedicated user and grant the necessary privileges according to your security rules.

## Local run
1. Clone the repository and enter the `Portfolio` directory.
2. Export the environment variables shown above.
3. Start the application (Flyway runs automatically):
   ```bash
   ./mvnw spring-boot:run
   ```
4. Open `http://localhost:8080` to browse the portfolio catalog.

## Project structure
```
src/main/java/com/portfolio/lavender/
├── controller/      # MVC controllers (home, projects, contact)
├── dto/simple/      # Form DTOs
├── model/           # JPA entities
└── repository/      # Spring Data JPA repositories

src/main/resources/
├── templates/       # Thymeleaf views & fragments
├── static/          # CSS and JavaScript assets
├── db/migration/    # Flyway migrations (schema + seeds)
└── application*.properties
```

## Author(s)
⧉ STUDIOS TKOH! ⧉
