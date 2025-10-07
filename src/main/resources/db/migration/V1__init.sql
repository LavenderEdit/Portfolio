/**
 * Author:  Joan Lavender
 * Created: Oct 7, 2025
 */

-- PROFILE
CREATE TABLE profile (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  full_name        VARCHAR(120) NOT NULL,
  headline         VARCHAR(160) NOT NULL,
  bio              TEXT         NOT NULL,
  email            VARCHAR(160) NOT NULL,
  location         VARCHAR(120),
  avatar_url       VARCHAR(512),
  resume_url       VARCHAR(512),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- SOCIAL LINKS
CREATE TABLE social_link (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  profile_id BIGINT NOT NULL,
  platform   VARCHAR(50) NOT NULL,   -- e.g., GITHUB, LINKEDIN, GMAIL, X, PORTFOLIO
  url        VARCHAR(512) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_social_profile FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE
);

-- SKILLS
CREATE TABLE skill_category (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(80) NOT NULL,
  sort_order INT NOT NULL DEFAULT 0
);

CREATE TABLE skill (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_id BIGINT NOT NULL,
  name        VARCHAR(80) NOT NULL,
  level       TINYINT NOT NULL DEFAULT 3, -- 1-5 scale if you want
  icon        VARCHAR(120),               -- optional icon key/class
  sort_order  INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_skill_category FOREIGN KEY (category_id) REFERENCES skill_category(id) ON DELETE CASCADE
);

-- PROJECTS
CREATE TABLE project (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title        VARCHAR(140) NOT NULL,
  slug         VARCHAR(160) NOT NULL UNIQUE,
  summary      VARCHAR(280) NOT NULL,
  description  TEXT,
  repo_url     VARCHAR(512),
  live_url     VARCHAR(512),
  cover_image  VARCHAR(512),
  start_date   DATE,
  end_date     DATE,
  featured     BOOLEAN NOT NULL DEFAULT FALSE,
  sort_order   INT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE project_skill (
  project_id BIGINT NOT NULL,
  skill_id   BIGINT NOT NULL,
  PRIMARY KEY (project_id, skill_id),
  CONSTRAINT fk_ps_project FOREIGN KEY (project_id) REFERENCES project(id) ON DELETE CASCADE,
  CONSTRAINT fk_ps_skill   FOREIGN KEY (skill_id)   REFERENCES skill(id)   ON DELETE CASCADE
);

-- EXPERIENCE
CREATE TABLE experience (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  company     VARCHAR(140) NOT NULL,
  role        VARCHAR(140) NOT NULL,
  location    VARCHAR(120),
  start_date  DATE NOT NULL,
  end_date    DATE,
  current     BOOLEAN NOT NULL DEFAULT FALSE,
  description TEXT
);

-- EDUCATION
CREATE TABLE education (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  institution VARCHAR(160) NOT NULL,
  degree      VARCHAR(160) NOT NULL,
  field       VARCHAR(160),
  start_date  DATE NOT NULL,
  end_date    DATE,
  description TEXT
);

-- CONTACT MESSAGES
CREATE TABLE contact_message (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(120) NOT NULL,
  email      VARCHAR(160) NOT NULL,
  message    TEXT NOT NULL,
  status     VARCHAR(20) NOT NULL DEFAULT 'NEW', -- NEW, REPLIED, ARCHIVED
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed minimal profile row so home renders day 1
INSERT INTO profile (full_name, headline, bio, email, location, avatar_url)
VALUES ('Juan Lavender', 'Estudiante de Desarrollo de Software | Software Development Student',
        'Apasionado por construir…', 'lavenderedit@gmail.com', 'Lima, Perú', NULL);

