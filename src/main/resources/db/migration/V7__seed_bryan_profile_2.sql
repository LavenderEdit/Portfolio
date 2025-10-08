/**
 * Author:  Studios TKOH!
 * Created: Oct 7, 2025
 */
SET @profile_slug := 'bryan-alexander-vidal-crispin';
SET @profile_email := 'vidalbryanalexander@gmail.com';

SET @profile_id := (SELECT id FROM profile WHERE slug = @profile_slug LIMIT 1);
SET @profile_id := IFNULL(@profile_id, (SELECT id FROM profile WHERE LOWER(email) = LOWER(@profile_email) ORDER BY id LIMIT 1));

SET @cat_frontend := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Frontend');
SET @cat_backend := (SELECT id FROM skill_category WHERE profile_id = @profile_id AND name = 'Backend');

INSERT INTO skill (category_id, name, level, icon, sort_order)
VALUES
  (@cat_backend, 'NodeJs', 5, NULL, 40);

SET @skill_react := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'React');
SET @skill_vite := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'Vite');
SET @skill_node := (SELECT id FROM skill WHERE category_id = @cat_backend AND name = 'NodeJs');
SET @skill_js := (SELECT id FROM skill WHERE category_id = @cat_frontend AND name = 'JavaScript');

INSERT INTO project (profile_id, title, slug, summary, description, repo_url, live_url, cover_image, start_date, end_date, featured, sort_order)
VALUES
  (@profile_id, 'Stremio Discord Rich Presence', 'stremio-drp',
   'Extensión para obtener los datos de una película o serie.',
   CONCAT(
     'Stremio Discord Rich Presence es un servicio en Node.js que actúa como puente entre Stremio, TMDB y', CHAR(10),
     'Discord Rich Presence. Al detectar la reproducción de una película en Stremio, consulta TMDB para', CHAR(10),
     'enriquecer la sesión con metadatos (título, año, sinopsis breve, duración, rating y arte) y publica en Discord', CHAR(10),
     'un estado en tiempo real con lo que estás viendo. El objetivo: ofrecer una presencia rica y precisa —sin', CHAR(10),
     'fricción— para que tus contactos vean título actual, progreso y hora estimada de finalización, con soporte', CHAR(10),
     'para estados reproduciendo / pausa.'
   ),
   'https://github.com/VaCris/StremioRPC_BACKEND.git',
   NULL,
   NULL,
   NULL,
   NULL,
   TRUE,
   40),
  (@profile_id, 'Trending Movies and Series', 'sp-tv',
   'Realización de un sitio web que muestra peliculas en tendencia y series.',
   CONCAT(
     'SP-TV es una SPA ligera en React + Vite orientada al content discovery: muestra películas y series en', CHAR(10),
     'tendencia con una UI responsiva y tiempos de carga agresivamente bajos. El frontend consume una API de', CHAR(10),
     'catálogo (proveedor configurable) mediante un data adapter desacoplado, lo que facilita cambiar de fuente', CHAR(10),
     'sin tocar la UI. En producción corre en Render, optimizada para cold starts y entrega estática.'
   ),
   NULL,
   'https://sp-tv.onrender.com/',
   NULL,
   NULL,
   NULL,
   FALSE,
   50);

SET @proj_rpc := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'stremio-drp');
SET @proj_sp := (SELECT id FROM project WHERE profile_id = @profile_id AND slug = 'sp-tv');

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_rpc, skill_id FROM (
  SELECT @skill_node AS skill_id UNION ALL
  SELECT @skill_js
) AS rpc_skills;

INSERT INTO project_skill (project_id, skill_id)
SELECT @proj_sp, skill_id FROM (
  SELECT @skill_react AS skill_id UNION ALL
  SELECT @skill_vite UNION ALL
  SELECT @skill_js
) AS sp_skills;
