/**
 * Author:  Joan Lavender
 * Created: Oct 7, 2025
 */
-- Skill.level debe ser SMALLINT para alinear con la entidad
ALTER TABLE skill
  MODIFY COLUMN level SMALLINT NOT NULL DEFAULT 3;
