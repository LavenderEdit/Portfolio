# Studios TKOH!
Aplicación web full-stack construida con Spring Boot 3 y Thymeleaf para presentar múltiples portafolios profesionales desde una sola instancia. El proyecto incluye páginas dinámicas, formulario de contacto persistente y datos de ejemplo ricos que demuestran perfiles de desarrolladores reales.

### [English version HERE!](README.en.md)

# Demo
### [studios-tkoh](https://studios-tkoh.azurewebsites.net)

## Tabla de contenidos
- [Características destacadas](#características-destacadas)
- [Arquitectura y componentes](#arquitectura-y-componentes)
- [Base de datos y migraciones](#base-de-datos-y-migraciones)
- [Datos de ejemplo incluidos](#datos-de-ejemplo-incluidos)
- [Requisitos previos](#requisitos-previos)
- [Configuración y variables de entorno](#configuración-y-variables-de-entorno)
- [Ejecución local](#ejecución-local)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Pruebas](#pruebas)
- [Siguientes pasos sugeridos](#siguientes-pasos-sugeridos)

## Características destacadas
- **Portafolios múltiples con rutas amigables**: la página principal lista todos los perfiles disponibles y redirige automáticamente si solo existe uno. Cada perfil se expone en `/portfolio/{slug}` con encabezados dinámicos, secciones de habilidades, proyectos, experiencia, educación y formulario de contacto. [HomeController](src/main/java/com/portfolio/lavender/controller/HomeController.java) orquesta la carga de datos.
- **Detalle de proyectos**: secciones enriquecidas con imágenes, enlaces a repositorios/demos y listado de habilidades asociadas por proyecto, accesibles en `/portfolio/{profileSlug}/projects/{slug}` mediante [ProjectController](src/main/java/com/portfolio/lavender/controller/ProjectController.java).
- **Formulario de contacto validado**: validación del lado del servidor con Jakarta Bean Validation y persistencia de mensajes a través de [ContactController](src/main/java/com/portfolio/lavender/controller/ContactController.java), [ContactForm DTO](src/main/java/com/portfolio/lavender/dto/simple/ContactForm.java) y el repositorio JPA [ContactMessageRepo](src/main/java/com/portfolio/lavender/repository/ContactMessageRepo.java).
- **Frontend responsivo con dark theme**: diseño moderno definido en [`static/css/site.css`](src/main/resources/static/css/site.css) y comportamiento accesible del menú, navegación suave y resaltado de sección implementado en [`static/js/site.js`](src/main/resources/static/js/site.js).
- **Plantillas Thymeleaf reutilizables**: fragmentos de layout, barra de navegación y pie de página bajo `templates/fragments` permiten mantener consistencia visual entre las vistas principales (`profiles`, `portfolio`, `project`).

## Arquitectura y componentes
- **Capa web (MVC)**: controladores Spring MVC entregan modelos a plantillas Thymeleaf.
  - `HomeController` carga la lista de perfiles y la vista principal del portafolio individual.
  - `ProjectController` muestra el detalle de un proyecto, incluyendo metadatos para SEO.
  - `ContactController` procesa el formulario de contacto con mensajes flash.
- **Capa de dominio**: entidades JPA (`Profile`, `SocialLink`, `SkillCategory`, `Skill`, `Project`, `Experience`, `Education`, `ContactMessage`) modelan la información que se muestra en el portafolio.
- **Persistencia**: repositorios Spring Data JPA con `@EntityGraph` optimizan la carga de relaciones (por ejemplo, habilidades asociadas a categorías y proyectos).
- **Configuración de aplicación**: `application.properties` habilita Flyway, valida el esquema y define los placeholders para la conexión MySQL. Perfiles `dev` y `prod` ajustan la verbosidad de logs y parámetros del pool Hikari.
- **Dependencias clave** (ver [`pom.xml`](pom.xml)):
  - Spring Boot starters para web, datos JPA, validación y Thymeleaf.
  - Flyway con extensión MySQL para migraciones versionadas.
  - Lombok para reducir boilerplate y configuración de MapStruct lista para futuros DTO mappers.

## Base de datos y migraciones
- **Esquema inicial**: `V1__init.sql` crea tablas para perfiles, redes sociales, categorías de habilidades, proyectos con tabla puente `project_skill`, experiencia, educación y mensajes de contacto.
- **Soporte multi-perfil**: `V2__multi_profile_support.sql` refuerza claves foráneas e índices para asociar cada recurso a un perfil específico, asegurando que las rutas por *slug* sean únicas por portafolio.
- **Seeders versionados**: migraciones `V4` a `V8` reinicializan colecciones dependientes y agregan datos coherentes (habilidades, proyectos, experiencias y educación) para distintos perfiles. Esto permite tener contenido atractivo en el primer arranque sin tareas manuales.

## Datos de ejemplo incluidos
- **Juan Santos Pimentel Lalangui** (`juan-lavender-1`): perfil full-stack con proyectos como *Chattide Web*, *app-swing*, *Grading API* y *BusquedaPokemon*, además de experiencia profesional y formación en SENATI.
- **Bryan Alexander Vidal Crispin** (`bryan-alexander-vidal-crispin`): enfoque web fullstack con proyectos SPA y utilidades en React/Vite, experiencias en SERVISERC, JHARDSYSTEX y LUBRICANTES CLAUDIA.
- **Andriy Lionel Pastrana Cajavilca** (`andriy-lionel-pastrana-cajavilca`): especialización frontend y automatización con proyectos personales desplegados en GitHub Pages y experiencia en Python, Java Swing y React para sistemas empresariales.

Cada migración elimina y vuelve a poblar datos relacionados para mantener coherencia, por lo que es seguro ejecutar Flyway en entornos limpios o existentes.

## Requisitos previos
- Java 21 (JDK) configurado en el `PATH`.
- Maven 3.9+.
- Servidor MySQL 8.0+ accesible (puedes usar Docker) con un esquema vacío.

## Configuración y variables de entorno
La aplicación espera variables de entorno estándar para la conexión MySQL:

```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_DATABASE=portfolio
export MYSQL_USER=portfolio_user
export MYSQL_PASSWORD=super_seguro
```

Ejemplo rápido para levantar MySQL con Docker:

```bash
docker run --name portfolio-mysql -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_DATABASE=portfolio \
  -p 3306:3306 -d mysql:8.0
```

Crea un usuario y otorga permisos según tus políticas de seguridad.

## Ejecución local
1. Clona el repositorio y entra en la carpeta `Portfolio`.
2. Configura las variables de entorno mencionadas.
3. Ejecuta las migraciones y levanta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Visita `http://localhost:8080` para navegar entre los portafolios. Flyway aplicará automáticamente las migraciones al arrancar.

## Estructura del proyecto
```
src/main/java/com/portfolio/lavender/
├── controller/      # Controladores MVC (home, proyectos, contacto)
├── dto/simple/      # DTOs para formularios
├── model/           # Entidades JPA del dominio
└── repository/      # Repositorios Spring Data JPA

src/main/resources/
├── templates/       # Vistas Thymeleaf y fragmentos compartidos
├── static/          # CSS y JavaScript del frontend
├── db/migration/    # Migraciones Flyway (esquema + semillas)
└── application*.properties
```

## Autor(es)
⧉ STUDIOS TKOH! ⧉
