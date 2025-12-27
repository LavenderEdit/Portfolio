/**
 * Author:  Studios TKOH!
 * Created: Dec 27, 2025
 */

CREATE TABLE global_skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    name VARCHAR(80) NOT NULL,
    icon_url VARCHAR(512),
    UNIQUE KEY uk_global_skill_name (name)
);

INSERT INTO global_skill (created_at, name, icon_url, created_by)
SELECT NOW(), DISTINCT_NAMES.name, MAX(DISTINCT_NAMES.icon), 'system_migration'
FROM (SELECT name, icon FROM skill) AS DISTINCT_NAMES
GROUP BY DISTINCT_NAMES.name;

ALTER TABLE skill ADD COLUMN global_skill_id BIGINT;

UPDATE skill s
JOIN global_skill gs ON s.name = gs.name
SET s.global_skill_id = gs.id;

ALTER TABLE skill MODIFY COLUMN name VARCHAR(80) NULL;
ALTER TABLE skill MODIFY COLUMN icon VARCHAR(255) NULL;

ALTER TABLE skill
ADD CONSTRAINT fk_skill_global
FOREIGN KEY (global_skill_id) REFERENCES global_skill(id)
ON DELETE RESTRICT;

DELETE FROM skill WHERE global_skill_id IS NULL;

ALTER TABLE skill MODIFY COLUMN global_skill_id BIGINT NOT NULL;