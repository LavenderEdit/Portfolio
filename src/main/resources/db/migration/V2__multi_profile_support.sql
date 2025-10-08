/**
 * Author:  Studios TKOH!
 * Created: Oct 7, 2025
 */

/* V2 - multi profile support (idempotente + collation-safe para MySQL 8.0) */

-- Guardamos y seteamos a utf8mb4 para toda la sesión
SET @old_cs_client := @@character_set_client;
SET @old_cs_results := @@character_set_results;
SET @old_collation_connection := @@collation_connection;
SET NAMES utf8mb4 COLLATE utf8mb4_0900_ai_ci;

-- Usaremos el schema actual como utf8mb4 con collation fija
SET @schema_name4 := CONVERT(DATABASE() USING utf8mb4) COLLATE utf8mb4_0900_ai_ci;

/* ---------- PROFILE.slug ---------- */
-- add column si falta
SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'profile'
    AND CONVERT(column_name  USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'slug'
);
SET @ddl := IF(@col_exists=0, 'ALTER TABLE profile ADD COLUMN slug VARCHAR(80)', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- backfill + NOT NULL
UPDATE profile
SET slug = CONCAT(LOWER(REPLACE(full_name, ' ', '-')), '-', id)
WHERE slug IS NULL OR slug = '';
ALTER TABLE profile MODIFY COLUMN slug VARCHAR(80) NOT NULL;

-- unique si falta
SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'profile'
    AND CONVERT(index_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'uk_profile_slug'
);
SET @ddl := IF(@idx_exists=0,
  'ALTER TABLE profile ADD CONSTRAINT uk_profile_slug UNIQUE (slug)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- perfil semilla para backfill
SET @first_profile_id := (SELECT id FROM profile ORDER BY id LIMIT 1);

/* ---------- SKILL_CATEGORY.profile_id + FK + índice ---------- */
SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'skill_category'
    AND CONVERT(column_name  USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'profile_id'
);
SET @ddl := IF(@col_exists=0,
  'ALTER TABLE skill_category ADD COLUMN profile_id BIGINT',
  'SELECT 1'
); PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE skill_category SET profile_id=@first_profile_id WHERE profile_id IS NULL;
ALTER TABLE skill_category MODIFY COLUMN profile_id BIGINT NOT NULL;

SET @fk_exists := (
  SELECT COUNT(*)
  FROM information_schema.referential_constraints
  WHERE CONVERT(constraint_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(constraint_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'fk_skill_category_profile'
);
SET @ddl := IF(@fk_exists=0,
  'ALTER TABLE skill_category ADD CONSTRAINT fk_skill_category_profile
     FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE',
  'SELECT 1'
); PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'skill_category'
    AND CONVERT(index_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'idx_skill_category_profile'
);
SET @ddl := IF(@idx_exists=0,
  'CREATE INDEX idx_skill_category_profile ON skill_category(profile_id, sort_order)',
  'SELECT 1'
); PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

/* ---------- PROJECT.profile_id + FK + índices + UNIQUE compuesto ---------- */
SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'project'
    AND CONVERT(column_name  USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'profile_id'
);
SET @ddl := IF(@col_exists=0, 'ALTER TABLE project ADD COLUMN profile_id BIGINT', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE project SET profile_id=@first_profile_id WHERE profile_id IS NULL;
ALTER TABLE project MODIFY COLUMN profile_id BIGINT NOT NULL;

-- dropear unique de slug si existiera con cualquier nombre
SET @uniq_slug_idx := (
  SELECT index_name
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'project'
    AND CONVERT(column_name  USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'slug'
    AND non_unique = 0
  LIMIT 1
);
SET @ddl := IF(@uniq_slug_idx IS NOT NULL AND @uniq_slug_idx <> '',
  CONCAT('ALTER TABLE project DROP INDEX `', CAST(@uniq_slug_idx AS CHAR CHARACTER SET utf8mb4) , '`'),
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- unique (profile_id, slug)
SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'project'
    AND CONVERT(index_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'uk_project_profile_slug'
);
SET @ddl := IF(@idx_exists=0,
  'ALTER TABLE project ADD CONSTRAINT uk_project_profile_slug UNIQUE (profile_id, slug)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- FK
SET @fk_exists := (
  SELECT COUNT(*)
  FROM information_schema.referential_constraints
  WHERE CONVERT(constraint_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(constraint_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'fk_project_profile'
);
SET @ddl := IF(@fk_exists=0,
  'ALTER TABLE project ADD CONSTRAINT fk_project_profile
     FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- índice auxiliar
SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'project'
    AND CONVERT(index_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'idx_project_profile_featured'
);
SET @ddl := IF(@idx_exists=0,
  'CREATE INDEX idx_project_profile_featured ON project(profile_id, featured, sort_order)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

/* ---------- EXPERIENCE.profile_id + FK + índice ---------- */
SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'experience'
    AND CONVERT(column_name  USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'profile_id'
);
SET @ddl := IF(@col_exists=0, 'ALTER TABLE experience ADD COLUMN profile_id BIGINT', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE experience SET profile_id=@first_profile_id WHERE profile_id IS NULL;
ALTER TABLE experience MODIFY COLUMN profile_id BIGINT NOT NULL;

SET @fk_exists := (
  SELECT COUNT(*)
  FROM information_schema.referential_constraints
  WHERE CONVERT(constraint_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(constraint_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'fk_experience_profile'
);
SET @ddl := IF(@fk_exists=0,
  'ALTER TABLE experience ADD CONSTRAINT fk_experience_profile
     FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'experience'
    AND CONVERT(index_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'idx_experience_profile'
);
SET @ddl := IF(@idx_exists=0,
  'CREATE INDEX idx_experience_profile ON experience(profile_id, start_date)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

/* ---------- EDUCATION.profile_id + FK + índice ---------- */
SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'education'
    AND CONVERT(column_name  USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'profile_id'
);
SET @ddl := IF(@col_exists=0, 'ALTER TABLE education ADD COLUMN profile_id BIGINT', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE education SET profile_id=@first_profile_id WHERE profile_id IS NULL;
ALTER TABLE education MODIFY COLUMN profile_id BIGINT NOT NULL;

SET @fk_exists := (
  SELECT COUNT(*)
  FROM information_schema.referential_constraints
  WHERE CONVERT(constraint_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(constraint_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'fk_education_profile'
);
SET @ddl := IF(@fk_exists=0,
  'ALTER TABLE education ADD CONSTRAINT fk_education_profile
     FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'education'
    AND CONVERT(index_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'idx_education_profile'
);
SET @ddl := IF(@idx_exists=0,
  'CREATE INDEX idx_education_profile ON education(profile_id, start_date)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

/* ---------- CONTACT_MESSAGE.profile_id + FK + índice ---------- */
SET @col_exists := (
  SELECT COUNT(*)
  FROM information_schema.COLUMNS
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'contact_message'
    AND CONVERT(column_name  USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'profile_id'
);
SET @ddl := IF(@col_exists=0, 'ALTER TABLE contact_message ADD COLUMN profile_id BIGINT', 'SELECT 1');
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

UPDATE contact_message SET profile_id=@first_profile_id WHERE profile_id IS NULL;
ALTER TABLE contact_message MODIFY COLUMN profile_id BIGINT NOT NULL;

SET @fk_exists := (
  SELECT COUNT(*)
  FROM information_schema.referential_constraints
  WHERE CONVERT(constraint_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(constraint_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'fk_contact_message_profile'
);
SET @ddl := IF(@fk_exists=0,
  'ALTER TABLE contact_message ADD CONSTRAINT fk_contact_message_profile
     FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @idx_exists := (
  SELECT COUNT(*)
  FROM information_schema.statistics
  WHERE CONVERT(table_schema USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = @schema_name4
    AND CONVERT(table_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'contact_message'
    AND CONVERT(index_name   USING utf8mb4) COLLATE utf8mb4_0900_ai_ci = 'idx_contact_profile'
);
SET @ddl := IF(@idx_exists=0,
  'CREATE INDEX idx_contact_profile ON contact_message(profile_id, created_at)',
  'SELECT 1'
);
PREPARE stmt FROM @ddl; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Limpieza
SET @schema_name4 = NULL;
SET @first_profile_id = NULL;
SET @col_exists=NULL; SET @idx_exists=NULL; SET @fk_exists=NULL; SET @uniq_slug_idx=NULL; SET @ddl=NULL;
