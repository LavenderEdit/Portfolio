/**
 * Author:  Studios TKOH!
 * Created: Oct 7, 2025
 */
SET @profile_slug := 'andriy-lionel-pastrana';
SET @profile_email := 'andriylionel5@gmail.com';

SET @bio_text := CONCAT(
  'Estudiante de la carrera de Desarrollo de Software en SENATI con proyección a culminar en 2025.', CHAR(10), CHAR(10),
  'Cuenta con experiencia creando interfaces web en React + Vite, desarrollando aplicaciones de escritorio en Java y automatizando flujos con Python.', CHAR(10), CHAR(10),
  'Se caracteriza por su liderazgo, proactividad y compromiso al impulsar el trabajo en equipo y la mejora continua.'
);

INSERT INTO profile (full_name, headline, bio, email, location, avatar_url, resume_url, slug)
SELECT
  'Andriy Lionel Pastrana Cajavilca',
  'Estudiante de Desarrollo de Software | Frontend React & Automatización',
  @bio_text,
  @profile_email,
  'Lima, Perú',
  'https://avatars.githubusercontent.com/u/170670806?v=4',
  NULL,
  @profile_slug
WHERE NOT EXISTS (SELECT 1 FROM profile WHERE slug = @profile_slug);

SET @profile_id := (SELECT id FROM profile WHERE slug = @profile_slug LIMIT 1);
SET @profile_id := IFNULL(@profile_id, (SELECT id FROM profile WHERE LOWER(email) = LOWER(@profile_email) ORDER BY id LIMIT 1));

UPDATE profile
SET full_name = 'Andriy Lionel Pastrana Cajavilca',
    headline = 'Estudiante de Desarrollo de Software | Frontend React & Automatización',
    bio = @bio_text,
    email = @profile_email,
    location = 'Lima, Perú',
    avatar_url = 'https://avatars.githubusercontent.com/u/170670806?v=4',
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
  (@profile_id, 'LINKEDIN', 'https://www.linkedin.com/in/andriy-pastrana-841ba4308/', 10),
  (@profile_id, 'GITHUB', 'https://github.com/LionelPastranaMK500', 20),
  (@profile_id, 'GMAIL', 'mailto:andriylionel5@gmail.com', 30);

INSERT INTO skill_category (profile_id, name, sort_order)
VALUES
  (@profile_id, 'Frontend', 10),
  (@profile_id, 'Desktop y Automatización', 20),
  (@profile_id, 'Competencias Técnicas Generales', 30),
  (@profile_id, 'Habilidades Blandas', 40),
  (@profile_id, 'Idiomas', 50);

SET @cat_frontend := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Frontend');
SET @cat_desktop := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Desktop y Automatización');
SET @cat_general := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Competencias Técnicas Generales');
SET @cat_softskills := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Habilidades Blandas');
SET @cat_languages := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Idiomas');

INSERT INTO skill (category_id, name, level, icon, sort_order)
VALUES
  (@cat_frontend, 'React', 4, NULL, 10),
  (@cat_frontend, 'Vite', 4, NULL, 20),
  (@cat_frontend, 'JavaScript', 4, NULL, 30),
  (@cat_frontend, 'TypeScript', 3, NULL, 40),
  (@cat_frontend, 'Tailwind CSS', 4, NULL, 50),
  (@cat_frontend, 'HTML5', 5, NULL, 60),
  (@cat_frontend, 'CSS3', 5, NULL, 70),
  (@cat_frontend, 'Bootstrap', 4, NULL, 80),

  (@cat_desktop, 'Java', 4, NULL, 10),
  (@cat_desktop, 'Electron', 3, NULL, 20),
  (@cat_desktop, 'Python', 3, NULL, 30),

  (@cat_general, 'Programación', 4, NULL, 10),
  (@cat_general, 'Uso de múltiples IDE', 4, NULL, 20),

  (@cat_softskills, 'Simpatía y asertividad', 4, NULL, 10),
  (@cat_softskills, 'Perseverancia y tenacidad', 5, NULL, 20),
  (@cat_softskills, 'Solución de problemas', 4, NULL, 30),
  (@cat_softskills, 'Puntualidad y compromiso', 5, NULL, 40),
  (@cat_softskills, 'Proactivo y trabajo en equipo', 5, NULL, 50),
  (@cat_softskills, 'Compromiso', 5, NULL, 60),

  (@cat_languages, 'Español (Nativo)', 5, NULL, 10),
  (@cat_languages, 'Inglés (Básico)', 2, NULL, 20);

SET @skill_react := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'React');
SET @skill_vite := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'Vite');
SET @skill_js := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'JavaScript');
SET @skill_ts := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'TypeScript');
SET @skill_tailwind := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'Tailwind CSS');
SET @skill_html := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'HTML5');
SET @skill_css := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'CSS3');
SET @skill_bootstrap := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'Bootstrap');
SET @skill_java := (SELECT id FROM skill WHERE category_id = @cat_desktop AND name = 'Java');
SET @skill_electron := (SELECT id FROM skill WHERE category_id = @cat_desktop AND name = 'Electron');
SET @skill_python := (SELECT id FROM skill WHERE category_id = @cat_desktop AND name = 'Python');

INSERT INTO project (profile_id, title, slug, summary, description, repo_url, live_url, cover_image, start_date, end_date, featured, sort_order)
VALUES
  (@profile_id, 'Tributo a mi madre', 'dia-de-la-madre',
   'Sitio conmemorativo construido con React, Vite y TailwindCSS.',
   CONCAT(
     'Diseño de una página web reactiva con temática de TinkerBell para homenajear a su madre.', CHAR(10),
     'Implementado con React 18 + Vite y estilizado con TailwindCSS para lograr un resultado dinámico y responsivo.', CHAR(10),
     'Incluye animaciones ligeras y componentes reutilizables que refuerzan la narrativa visual.'
   ),
   'https://github.com/LionelPastranaMK500/dia-de-la-madre-RVT',
   'https://lionelpastranamk500.github.io/dia-de-la-madre-RVT/',
   NULL,
   NULL,
   NULL,
   TRUE,
   10),
  (@profile_id, 'Festejo para Sami', 'cumple-sami',
   'Landing page festiva en HTML, CSS y Bootstrap.',
   CONCAT(
     'Página diseñada para celebrar el cumpleaños de su hermana con elementos visuales de TinkerBell y Frozen.', CHAR(10),
     'Construida con HTML, CSS y Bootstrap para lograr una experiencia responsiva y rápida de iterar.', CHAR(10),
     'Se despliega en GitHub Pages para compartirla fácilmente con la familia y amistades.'
   ),
   'https://github.com/LionelPastranaMK500/CSami',
   'https://lionelpastranamk500.github.io/CSami/',
   NULL,
   NULL,
   NULL,
   FALSE,
   20),
  (@profile_id, 'Feliz San Valentín', 'san-valentin',
   'Página temática para celebrar San Valentín en HTML, CSS y JavaScript.',
   CONCAT(
     'Landing interactiva enfocada en mensajes personalizados y animaciones para el día de San Valentín.', CHAR(10),
     'Implementada con HTML5, CSS3 y JavaScript para reforzar fundamentos de DOM y efectos visuales.', CHAR(10),
     'Publicada en GitHub Pages permitiendo compartirla rápidamente con amigos y familiares.'
   ),
   'https://github.com/LionelPastranaMK500/San-valentin',
   'https://lionelpastranamk500.github.io/San-valentin/',
   NULL,
   NULL,
   NULL,
   FALSE,
   30),
  (@profile_id, 'Hoyoverse Redemption Codes', 'hoyoverse-codes',
   'Interfaz GUI para gestionar códigos de canje de Hoyoverse.',
   CONCAT(
     'Aplicación basada en React y Electron que consume una API para reclamar códigos de juegos de Hoyoverse.', CHAR(10),
     'Incluye componentes en TypeScript y estilos con TailwindCSS para mantener consistencia visual.', CHAR(10),
     'Pensada para simplificar el flujo de redención sin depender del navegador web.'
   ),
   'https://github.com/LionelPastranaMK500/ZZZ-codes',
   NULL,
   NULL,
   NULL,
   NULL,
   FALSE,
   40),
  (@profile_id, 'Gestor de ventas', 'sistema-ventas',
   'Frontend para un sistema de gestión de ventas y facturación.',
   CONCAT(
     'Interfaz desarrollada en React y TailwindCSS que consume una API de facturación electrónica.', CHAR(10),
     'Permite administrar productos, clientes y comprobantes enfocándose en la usabilidad.', CHAR(10),
     'Optimizada con Vite para obtener tiempos de compilación y despliegue rápidos.'
   ),
   'https://github.com/LionelPastranaMK500/Sistema-de-Gestion',
   'https://sistema-de-gestion.pages.dev/',
   NULL,
   NULL,
   NULL,
   TRUE,
   50);

SET @proj_tributo := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'dia-de-la-madre');
SET @proj_sami := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'cumple-sami');
SET @proj_valentin := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'san-valentin');
SET @proj_hoyoverse := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'hoyoverse-codes');
SET @proj_gestor := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'sistema-ventas');

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_tributo, skill_id FROM (
  SELECT @skill_react AS skill_id UNION ALL
  SELECT @skill_vite UNION ALL
  SELECT @skill_tailwind UNION ALL
  SELECT @skill_js
) AS tributo_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_sami, skill_id FROM (
  SELECT @skill_html AS skill_id UNION ALL
  SELECT @skill_css UNION ALL
  SELECT @skill_bootstrap
) AS sami_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_valentin, skill_id FROM (
  SELECT @skill_html AS skill_id UNION ALL
  SELECT @skill_css UNION ALL
  SELECT @skill_js
) AS valentin_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_hoyoverse, skill_id FROM (
  SELECT @skill_react AS skill_id UNION ALL
  SELECT @skill_electron UNION ALL
  SELECT @skill_tailwind UNION ALL
  SELECT @skill_vite UNION ALL
  SELECT @skill_js UNION ALL
  SELECT @skill_ts
) AS hoyoverse_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_gestor, skill_id FROM (
  SELECT @skill_react AS skill_id UNION ALL
  SELECT @skill_tailwind UNION ALL
  SELECT @skill_vite UNION ALL
  SELECT @skill_js
) AS gestor_skills;

INSERT INTO experience (profile_id, company, role, location, start_date, end_date, current, description)
VALUES
(@profile_id, 'SERVISERC', 'Desarrollador Python', 'Remoto', '2023-06-11', '2023-11-28', FALSE,
    CONCAT(
     'Construcción de scripts para migrar datos desde archivos Excel hacia bases de datos.', CHAR(10),
     'Implementación de automatizaciones propias para optimizar tareas recurrentes con Python.', CHAR(10),
     'Estandarización de procesos para asegurar consistencia y confiabilidad en las migraciones.'
    )),
    (@profile_id, 'SERVISERC', 'Desarrollador de Software', 'Lima, Perú', '2024-02-01', '2024-06-30', FALSE,
    CONCAT(
     'Creación de una aplicación de escritorio en Java con Swing siguiendo el patrón MVC.', CHAR(10),
     'Automatización del control y gestión de pagos para una consultoría, manteniéndose en producción.', CHAR(10),
     'Responsable de todo el ciclo de desarrollo, pruebas y documentación técnica.'
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
   '2023-02-01',
   '2025-11-30',
   CONCAT(
     'Estudiante comprometido con el 6.º ciclo del programa, combinando estudios con proyectos personales y colaborativos.', CHAR(10),
     'Busca oportunidades laborales para seguir creciendo profesionalmente y aplicar sus conocimientos técnicos.'
   ));

