/**
 * Author:  Studios TKOH!
 * Created: Oct 7, 2025
 */

-- Perfil principal de Juan Lavender con datos completos
SET @profile_id := (SELECT id FROM profile WHERE slug = 'juan-lavender-1' LIMIT 1);
SET @profile_id := IFNULL(@profile_id, (SELECT id FROM profile WHERE LOWER(full_name) = 'juan lavender' ORDER BY id LIMIT 1));

SET @bio_text := CONCAT(
  'Desarrollador de software en formación con enfoque full stack y bases sólidas en Java, PHP y JavaScript.', CHAR(10), CHAR(10),
  'Disfruto crear soluciones modulares que siguen principios de Clean Code, MVC y APIs RESTful, desde backends con Spring Boot o Laravel hasta frontends dinámicos.', CHAR(10), CHAR(10),
  'Actualmente impulso proyectos personales como Chattide Web, Grading API y BusquedaPokemon para experimentar con persistencia, despliegues en Azure y consumo de APIs públicas.'
);

UPDATE profile
SET full_name = 'Juan Pimentel',
    headline = 'Desarrollador de Software en formación | Java, PHP & Spring Boot',
    bio = @bio_text,
    email = 'gercermagden@gmail.com',
    location = 'Lima, Perú',
    avatar_url = 'https://avatars.githubusercontent.com/u/145859523?v=4',
    resume_url = NULL
WHERE id = @profile_id;

-- Reiniciamos colecciones dependientes para garantizar consistencia del perfil
DELETE FROM project_skill WHERE project_id IN (SELECT id FROM project WHERE profile_id = @profile_id);
DELETE FROM project WHERE profile_id = @profile_id;
DELETE FROM skill WHERE category_id IN (SELECT id FROM skill_category WHERE profile_id = @profile_id);
DELETE FROM skill_category WHERE profile_id = @profile_id;
DELETE FROM social_link WHERE profile_id = @profile_id;
DELETE FROM experience WHERE profile_id = @profile_id;
DELETE FROM education WHERE profile_id = @profile_id;

-- Redes sociales y enlaces destacados
INSERT INTO social_link (profile_id, platform, url, sort_order)
VALUES
  (@profile_id, 'GITHUB', 'https://github.com/LavenderEdit', 10),
  (@profile_id, 'GMAIL', 'mailto:gercermagden@gmail.com', 20),
  (@profile_id, 'LINKED-IN', 'https://linkedin.com/in/juan-santos-pimentel-lalangui-873a0a2a9', 30);

-- Categorías de habilidades
INSERT INTO skill_category (profile_id, name, sort_order)
VALUES
  (@profile_id, 'Lenguajes de Programación', 10),
  (@profile_id, 'Frameworks y Librerías', 20),
  (@profile_id, 'Bases de Datos', 30),
  (@profile_id, 'Herramientas', 40),
  (@profile_id, 'Principios de Desarrollo', 50),
  (@profile_id, 'Otras Habilidades Técnicas', 60);

SET @cat_languages := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Lenguajes de Programación');
SET @cat_frameworks := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Frameworks y Librerías');
SET @cat_databases := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Bases de Datos');
SET @cat_tools := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Herramientas');
SET @cat_principles := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Principios de Desarrollo');
SET @cat_other := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Otras Habilidades Técnicas');

-- Habilidades por categoría
INSERT INTO skill (category_id, name, level, icon, sort_order)
VALUES
  (@cat_languages, 'Java', 5, NULL, 10),
  (@cat_languages, 'PHP', 4, NULL, 20),
  (@cat_languages, 'JavaScript', 4, NULL, 30),
  (@cat_languages, 'Node.js', 3, NULL, 40),
  (@cat_languages, 'SQL', 4, NULL, 50),

  (@cat_frameworks, 'Spring Boot', 4, NULL, 10),
  (@cat_frameworks, 'Laravel', 4, NULL, 20),
  (@cat_frameworks, 'Hibernate/JPA', 4, NULL, 30),

  (@cat_databases, 'MySQL', 4, NULL, 10),
  (@cat_databases, 'PostgreSQL', 3, NULL, 20),

  (@cat_tools, 'Apache NetBeans', 4, NULL, 10),
  (@cat_tools, 'Git', 4, NULL, 20),
  (@cat_tools, 'Azure', 3, NULL, 30),
  (@cat_tools, 'Postman', 4, NULL, 40),

  (@cat_principles, 'Modularización', 4, NULL, 10),
  (@cat_principles, 'RESTful APIs', 4, NULL, 20),
  (@cat_principles, 'MVC', 4, NULL, 30),
  (@cat_principles, 'Clean Code', 3, NULL, 40),

  (@cat_other, 'Web Scraping', 3, NULL, 10),
  (@cat_other, 'Automatización', 3, NULL, 20),
  (@cat_other, 'Documentación Técnica', 4, NULL, 30);

SET @skill_java := (SELECT id FROM skill WHERE category_id = @cat_languages AND name = 'Java');
SET @skill_php := (SELECT id FROM skill WHERE category_id = @cat_languages AND name = 'PHP');
SET @skill_js := (SELECT id FROM skill WHERE category_id = @cat_languages AND name = 'JavaScript');
SET @skill_node := (SELECT id FROM skill WHERE category_id = @cat_languages AND name = 'Node.js');
SET @skill_sql := (SELECT id FROM skill WHERE category_id = @cat_languages AND name = 'SQL');
SET @skill_spring := (SELECT id FROM skill WHERE category_id = @cat_frameworks AND name = 'Spring Boot');
SET @skill_laravel := (SELECT id FROM skill WHERE category_id = @cat_frameworks AND name = 'Laravel');
SET @skill_jpa := (SELECT id FROM skill WHERE category_id = @cat_frameworks AND name = 'Hibernate/JPA');
SET @skill_mysql := (SELECT id FROM skill WHERE category_id = @cat_databases AND name = 'MySQL');
SET @skill_postgres := (SELECT id FROM skill WHERE category_id = @cat_databases AND name = 'PostgreSQL');
SET @skill_git := (SELECT id FROM skill WHERE category_id = @cat_tools AND name = 'Git');
SET @skill_azure := (SELECT id FROM skill WHERE category_id = @cat_tools AND name = 'Azure');
SET @skill_postman := (SELECT id FROM skill WHERE category_id = @cat_tools AND name = 'Postman');
SET @skill_modular := (SELECT id FROM skill WHERE category_id = @cat_principles AND name = 'Modularización');
SET @skill_rest := (SELECT id FROM skill WHERE category_id = @cat_principles AND name = 'RESTful APIs');
SET @skill_mvc := (SELECT id FROM skill WHERE category_id = @cat_principles AND name = 'MVC');
SET @skill_clean := (SELECT id FROM skill WHERE category_id = @cat_principles AND name = 'Clean Code');
SET @skill_webscraping := (SELECT id FROM skill WHERE category_id = @cat_other AND name = 'Web Scraping');
SET @skill_automation := (SELECT id FROM skill WHERE category_id = @cat_other AND name = 'Automatización');
SET @skill_docs := (SELECT id FROM skill WHERE category_id = @cat_other AND name = 'Documentación Técnica');

-- Proyectos destacados
INSERT INTO project (profile_id, title, slug, summary, description, repo_url, live_url, cover_image, start_date, end_date, featured, sort_order)
VALUES
  (@profile_id, 'Chattide Web', 'chattide-web',
   'Aplicación web de red social básica creada con JSP, servlets y JPA (EclipseLink) para practicar autenticación y persistencia.',
   CONCAT(
     'Chattide Web es una plataforma social básica con registro de usuarios, perfiles y publicación de mensajes.', CHAR(10),
     'El proyecto aprovecha JSP, servlets y JPA con EclipseLink para gestionar la persistencia en MySQL.', CHAR(10),
     'Incluye buenas prácticas de modularización y patrones MVC.'
   ),
   'https://github.com/LavenderEdit/chattideweb',
   NULL,
   NULL,
   NULL,
   NULL,
   TRUE,
   10),

  (@profile_id, 'app-swing', 'app-swing',
   'Aplicación de escritorio con Java Swing, JPA e HikariCP para gestionar información educativa conectada a MySQL.',
   CONCAT(
     'Aplicación desktop desarrollada con Java Swing que administra datos académicos.', CHAR(10),
     'Integra JPA con HikariCP para manejar conexiones eficientes a MySQL y mantener la capa de persistencia limpia.', CHAR(10),
     'Sirve como laboratorio para patrones MVC y modularización en interfaces gráficas.'
   ),
   'https://github.com/LavenderEdit/app-swing',
   NULL,
   NULL,
   NULL,
   NULL,
   FALSE,
   20),

  (@profile_id, 'Grading API', 'grading-api',
   'API REST para evaluar estudiantes o colaboradores, construida con Spring Boot y desplegada en Azure.',
   CONCAT(
     'Servicio REST que permite registrar criterios de evaluación, puntajes y observaciones para distintos perfiles.', CHAR(10),
     'Implementada con Spring Boot y mejores prácticas de diseño de APIs RESTful.', CHAR(10),
     'El despliegue se realiza en Azure App Service para exponer la API públicamente.'
   ),
   'https://github.com/LavenderEdit/grading',
   'https://grading-app.azurewebsites.net',
   NULL,
   NULL,
   NULL,
   TRUE,
   30),

  (@profile_id, 'BusquedaPokemon', 'busqueda-pokemon',
   'Frontend en JavaScript que consume la PokeAPI v2 para mostrar estadísticas e imágenes de pokémon.',
   CONCAT(
     'Aplicación web ligera que permite buscar pokémon y visualizar información detallada usando la PokeAPI v2.', CHAR(10),
     'Construida con HTML, CSS y JavaScript enfocándose en consumo de APIs públicas y experiencia de usuario.', CHAR(10),
     'Se encuentra desplegada con GitHub Pages.'
   ),
   'https://github.com/LavenderEdit/BusquedaPokemon',
   'https://lavenderedit.github.io/BusquedaPokemon',
   NULL,
   NULL,
   NULL,
   FALSE,
   40);

SET @proj_chattide := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'chattide-web');
SET @proj_swing := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'app-swing');
SET @proj_grading := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'grading-api');
SET @proj_pokemon := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'busqueda-pokemon');

-- Asociación de habilidades a proyectos
INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_chattide, skill_id FROM (
  SELECT @skill_java AS skill_id UNION ALL
  SELECT @skill_jpa UNION ALL
  SELECT @skill_sql UNION ALL
  SELECT @skill_mvc
) AS chattide_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_swing, skill_id FROM (
  SELECT @skill_java AS skill_id UNION ALL
  SELECT @skill_jpa UNION ALL
  SELECT @skill_mysql UNION ALL
  SELECT @skill_modular
) AS swing_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_grading, skill_id FROM (
  SELECT @skill_java AS skill_id UNION ALL
  SELECT @skill_spring UNION ALL
  SELECT @skill_rest UNION ALL
  SELECT @skill_azure
) AS grading_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_pokemon, skill_id FROM (
  SELECT @skill_js AS skill_id UNION ALL
  SELECT @skill_node UNION ALL
  SELECT @skill_rest UNION ALL
  SELECT @skill_docs
) AS pokemon_skills;

-- Nota: se pueden agregar experiencias y educación en migraciones futuras según se documente la trayectoria profesional.