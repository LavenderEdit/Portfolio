/**
 * Author:  Studios TKOH!
 * Created: Oct 7, 2025
 */

SET @profile_slug := 'bryan-alexander-vidal-crispin';
SET @profile_email := 'vidalbryanalexander@gmail.com';

SET @bio_text := CONCAT(
  'Estudiante del 6to ciclo de Desarrollo de Software en SENATI con foco en aplicaciones web.', CHAR(10), CHAR(10),
  'Experiencia construyendo sitios responsivos, integrando bases de datos SQL/MySQL y consumiendo procedimientos almacenados.', CHAR(10), CHAR(10),
  'Destaca por su creatividad, responsabilidad y capacidad para resolver problemas en equipos multidisciplinarios.'
);

INSERT INTO profile (full_name, headline, bio, email, location, avatar_url, resume_url, slug)
SELECT
  'Bryan Alexander Vidal Crispin',
  'Estudiante de Desarrollo de Software | Desarrollo Web Frontend & Backend',
  @bio_text,
  @profile_email,
  'Lima, Perú',
  'https://avatars.githubusercontent.com/u/144495404?v=4',
  NULL,
  @profile_slug
WHERE NOT EXISTS (SELECT 1 FROM profile WHERE slug = @profile_slug);

SET @profile_id := (SELECT id FROM profile WHERE slug = @profile_slug LIMIT 1);
SET @profile_id := IFNULL(@profile_id, (SELECT id FROM profile WHERE LOWER(email) = LOWER(@profile_email) ORDER BY id LIMIT 1));

UPDATE profile
SET full_name = 'Bryan Alexander Vidal Crispin',
    headline = 'Estudiante de Desarrollo de Software | Desarrollo Web Frontend & Backend',
    bio = @bio_text,
    email = @profile_email,
    location = 'Lima, Perú',
    avatar_url = 'https://avatars.githubusercontent.com/u/144495404?v=4',
    resume_url = NULL
WHERE id = @profile_id;

DELETE FROM project_skill WHERE project_id IN (SELECT id FROM project WHERE profile_id = @profile_id);
DELETE FROM project WHERE profile_id = @profile_id;
DELETE FROM skill WHERE category_id IN (SELECT id FROM skill_category WHERE profile_id = @profile_id);
DELETE FROM skill_category WHERE profile_id = @profile_id;
DELETE FROM social_link WHERE profile_id = @profile_id;
DELETE FROM experience WHERE profile_id = @profile_id;
DELETE FROM education WHERE profile_id = @profile_id;

INSERT INTO social_link (profile_id, platform, url, sort_order)
VALUES
  (@profile_id, 'LINKEDIN', 'https://www.linkedin.com/in/bryan-alexander-vidal-crispin-110410301/', 10),
  (@profile_id, 'GITHUB', 'https://github.com/VaCris', 20),
  (@profile_id, 'GMAIL', 'mailto:vidalbryanalexander@gmail.com', 30);

INSERT INTO skill_category (profile_id, name, sort_order)
VALUES
  (@profile_id, 'Frontend', 10),
  (@profile_id, 'Backend', 20),
  (@profile_id, 'Bases de Datos', 30),
  (@profile_id, 'Control de Versiones', 40),
  (@profile_id, 'Habilidades Blandas', 50),
  (@profile_id, 'Idiomas', 60);

SET @cat_frontend := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Frontend');
SET @cat_backend := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Backend');
SET @cat_databases := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Bases de Datos');
SET @cat_versioning := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Control de Versiones');
SET @cat_softskills := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Habilidades Blandas');
SET @cat_languages := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Idiomas');

INSERT INTO skill (category_id, name, level, icon, sort_order)
VALUES
  (@cat_frontend, 'React', 4, NULL, 10),
  (@cat_frontend, 'Vite', 4, NULL, 20),
  (@cat_frontend, 'HTML5', 5, NULL, 30),
  (@cat_frontend, 'CSS3', 4, NULL, 40),
  (@cat_frontend, 'JavaScript', 4, NULL, 50),
  (@cat_frontend, 'Tailwind CSS', 4, NULL, 60),

  (@cat_backend, 'PHP', 4, NULL, 10),
  (@cat_backend, 'Java', 3, NULL, 20),
  (@cat_backend, 'C#', 3, NULL, 30),

  (@cat_databases, 'SQL', 4, NULL, 10),
  (@cat_databases, 'MySQL', 4, NULL, 20),
  (@cat_databases, 'SQLite', 3, NULL, 30),
  (@cat_databases, 'MariaDB', 3, NULL, 40),

  (@cat_versioning, 'Git', 4, NULL, 10),

  (@cat_softskills, 'Resolución de problemas', 4, NULL, 10),
  (@cat_softskills, 'Trabajo en equipo', 4, NULL, 20),
  (@cat_softskills, 'Organización y puntualidad', 4, NULL, 30),
  (@cat_softskills, 'Pensamiento crítico', 4, NULL, 40),
  (@cat_softskills, 'Adaptabilidad y creatividad', 4, NULL, 50),

  (@cat_languages, 'Español (Nativo)', 5, NULL, 10),
  (@cat_languages, 'Inglés (Técnico)', 3, NULL, 20);

SET @skill_react := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'React');
SET @skill_vite := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'Vite');
SET @skill_html := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'HTML5');
SET @skill_css := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'CSS3');
SET @skill_js := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'JavaScript');
SET @skill_tailwind := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'Tailwind CSS');
SET @skill_php := (SELECT id FROM skill WHERE category_id = @cat_backend AND name = 'PHP');
SET @skill_sql := (SELECT id FROM skill WHERE category_id = @cat_databases AND name = 'SQL');

INSERT INTO project (profile_id, title, slug, summary, description, repo_url, live_url, cover_image, start_date, end_date, featured, sort_order)
VALUES
  (@profile_id, 'Sistema de Gestión de Ventas', 'sistema-gestion-ventas',
   'Plataforma web para gestionar ventas, comprobantes y reportes con enfoque responsive.',
   CONCAT(
     'Aplicación SPA desarrollada con React y Tailwind CSS para administrar ventas, facturas, boletas y guías.', CHAR(10),
     'Incluye gestión de clientes, productos y reportes, priorizando la experiencia de usuario y el rendimiento.', CHAR(10),
     'Despliegue disponible en Cloudflare Pages para demostraciones rápidas.'
   ),
   'https://github.com/LionelPastranaMK500/Sistema-de-Gestion',
   'https://sistema-de-gestion.pages.dev/',
   NULL,
   NULL,
   NULL,
   TRUE,
   10),
  (@profile_id, 'Control de Gastos Mensuales', 'control-gastos-mensuales',
   'CRUD web para registrar y visualizar gastos mensuales desde cualquier dispositivo.',
   CONCAT(
     'Proyecto práctico construido con HTML, CSS y JavaScript para gestionar presupuestos personales.', CHAR(10),
     'Permite crear, editar y eliminar gastos, además de visualizar totales por mes.', CHAR(10),
     'El código está disponible en GitHub y desplegado con GitHub Pages.'
   ),
   'https://github.com/VaCris/Control-de-gastos-mensuales',
   'https://vacris.github.io/Control-de-gastos-mensuales/',
   NULL,
   NULL,
   NULL,
   FALSE,
   20),
  (@profile_id, 'Generador de Contraseñas Seguras', 'generador-contrasenas-seguras',
   'Utilidad web que crea contraseñas robustas según la longitud solicitada por el usuario.',
   CONCAT(
     'Herramienta ligera en HTML, CSS y JavaScript que arma contraseñas aleatorias con diferentes combinaciones.', CHAR(10),
     'Permite ajustar la longitud deseada y copiar fácilmente el resultado.', CHAR(10),
     'Ideal para practicar lógica de programación en el navegador y reforzar seguridad.'
   ),
   'https://github.com/VaCris/generador-contrasena',
   'https://vacris.github.io/generador-contrasena/',
   NULL,
   NULL,
   NULL,
   FALSE,
   30);

SET @proj_gestion := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'sistema-gestion-ventas');
SET @proj_gastos := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'control-gastos-mensuales');
SET @proj_password := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'generador-contrasenas-seguras');

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_gestion, skill_id FROM (
  SELECT @skill_react AS skill_id UNION ALL
  SELECT @skill_tailwind UNION ALL
  SELECT @skill_js UNION ALL
  SELECT @skill_vite
) AS gestion_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_gastos, skill_id FROM (
  SELECT @skill_html AS skill_id UNION ALL
  SELECT @skill_css UNION ALL
  SELECT @skill_js
) AS gastos_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_password, skill_id FROM (
  SELECT @skill_html AS skill_id UNION ALL
  SELECT @skill_css UNION ALL
  SELECT @skill_js
) AS password_skills;

INSERT INTO experience (profile_id, company, role, location, start_date, end_date, current, description)
VALUES
    (@profile_id, 'SERVISERC', 'Desarrollador Web', 'Lima, Perú', '2024-07-01', '2024-12-31', FALSE,
    CONCAT(
     'Diseño, desarrollo y mantenimiento de sitios web responsivos en HTML, CSS, JavaScript y PHP.', CHAR(10),
     'Implementación de funcionalidades a medida según requerimientos del cliente.', CHAR(10),
     'Optimización del rendimiento y mejoras en la experiencia de usuario.', CHAR(10),
     'Aseguramiento de compatibilidad multiplataforma y buenas prácticas de desarrollo.'
    )),
    (@profile_id, 'JHARDSYSTEX', 'Desarrollador Web', 'Lima, Perú', '2025-02-01', '2025-06-30', FALSE,
    CONCAT(
     'Desarrollo de una aplicación web para la gestión de tickets con frontend en HTML, CSS y JavaScript.', CHAR(10),
     'Construcción del backend en PHP y participación en el diseño de la base de datos relacional.', CHAR(10),
     'Coordinación con el equipo para pruebas funcionales y despliegue del sistema.'
    )),
    (@profile_id, 'LUBRICANTES CLAUDIA', 'Desarrollador Frontend', 'Lima, Perú', '2025-08-01', NULL, TRUE,
    CONCAT(
     'Desarrollo de la interfaz principal para un sistema de administración y facturación de una empresa automotriz.', CHAR(10),
     'Implementación de componentes en React 18, Vite 7 y TailwindCSS 3.4.14 consumiendo servicios API.', CHAR(10),
     'Colaboración con el equipo del cliente para refinar flujos de usuario y asegurar escalabilidad.'
    ));

INSERT INTO education (profile_id, institution, degree, field, start_date, end_date, description)
VALUES
  (@profile_id,
   'SENATI – Servicio Nacional de Adiestramiento en Trabajo Industrial',
   'Carrera Profesional de Desarrollo de Software',
   'Desarrollo de Software',
   '2023-01-01',
   NULL,
   CONCAT(
     'Estudiante del 6to ciclo con enfoque en desarrollo de aplicaciones web.', CHAR(10),
     'Participación constante en proyectos colaborativos fortaleciendo comunicación y pensamiento crítico.'
   ));
