/**
 * Author:  Studios TKOH!
 * Created: Oct 7, 2025
 */

SET @profile_id := (SELECT id FROM profile WHERE slug = 'juan-lavender-1' LIMIT 1);
SET @profile_id := IFNULL(@profile_id, (SELECT id FROM profile WHERE LOWER(full_name) = 'juan lavender' ORDER BY id LIMIT 1));

-- Experiencia profesional
INSERT INTO experience (profile_id, company, role, location, start_date, end_date, current, description)
VALUES
   (@profile_id, 'LUBRICANTES CLAUDIA', 'Practicante en Desarrollo de Software', 'Lima, Perú', '2024-08-01', '2024-11-30', FALSE,
   CONCAT(
     'Desarrollo de un sistema completo de facturación electrónica con integración SUNAT.', CHAR(10),
     'Gestión de inventario, ventas y clientes mediante una interfaz web.', CHAR(10),
     'Tecnologías utilizadas: Java, JSP, Hibernate/JPA y MySQL.', CHAR(10),
     'Validación de requerimientos con usuarios clave y documentación funcional del sistema.'
    )),
   (@profile_id, 'DIGITAL BUHO SAC', 'Practicante en Desarrollo de Software', 'Callao, Perú', '2025-02-01', '2025-06-30', FALSE,
   CONCAT(
     'Creación de una aplicación de scraping para extraer datos de productos (Macs, Apple Watch).', CHAR(10),
     'Implementación de pipelines para automatizar la extracción y procesamiento de datos.', CHAR(10),
     'Uso de Node.js y JavaScript para manejar estructuras JSON y orquestar la lógica de negocio.'
    )),
   (@profile_id, 'LUBRICANTES CLAUDIA', 'Practicante en Desarrollo de Software', 'Lima, Perú', '2025-08-01', NULL, TRUE,
   CONCAT(
     'Desarrollo de API con Java (Spring Boot).', CHAR(10),
     'Mantenimiento y mejora del sistema de facturación e inventario.', CHAR(10),
     'Colaboración con áreas administrativas para adaptar soluciones a necesidades reales.', CHAR(10),
     'Implementación de principios de modularización y desarrollo escalable.'
    ));

-- Formación académica
INSERT INTO education (profile_id, institution, degree, field, start_date, end_date, description)
VALUES
  (@profile_id,
   'SENATI – Servicio Nacional de Adiestramiento en Trabajo Industrial',
   'Desarrollo de Software',
   'Carrera Profesional',
   '2022-03-01',
   NULL,
   CONCAT(
     'Estudiante activo cursando el 6to ciclo.', CHAR(10),
     'Formación técnica en desarrollo de software con enfoque práctico en Lima, Perú.'
   ));

