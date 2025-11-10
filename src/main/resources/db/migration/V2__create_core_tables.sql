/**
 * Author:  Studios TKOH!
 * Created: Nov 10, 2025
 */
USE `studiostkoh.portafolio`;

-- 1. app_user: Almacena credenciales de seguridad.
CREATE TABLE app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    email VARCHAR(150) NOT NULL,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(50) NOT NULL,
    UNIQUE KEY uk_user_email (email)
);

-- 2. profile: El portafolio central, vinculado 1-a-1 con app_user.
CREATE TABLE profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    user_id BIGINT NOT NULL,
    slug VARCHAR(80) NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    headline VARCHAR(160) NOT NULL,
    bio TEXT NOT NULL,
    contact_email VARCHAR(160) NOT NULL,
    location VARCHAR(100),
    avatar_url VARCHAR(512),
    resume_url VARCHAR(512),
    UNIQUE KEY uk_profile_slug (slug),
    UNIQUE KEY uk_profile_user_id (user_id),
    CONSTRAINT fk_profile_user
        FOREIGN KEY (user_id) REFERENCES app_user(id)
        ON DELETE CASCADE
);

-- 3. social_link
CREATE TABLE social_link (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    profile_id BIGINT NOT NULL,
    platform VARCHAR(50) NOT NULL,
    url VARCHAR(512) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_social_profile
        FOREIGN KEY (profile_id) REFERENCES profile(id)
        ON DELETE CASCADE
);

-- 4. skill_category
CREATE TABLE skill_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    profile_id BIGINT NOT NULL,
    name VARCHAR(80) NOT NULL,
    sort_order INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_skillcategory_profile
        FOREIGN KEY (profile_id) REFERENCES profile(id)
        ON DELETE CASCADE
);

-- 5. skill
CREATE TABLE skill (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    category_id BIGINT NOT NULL,
    name VARCHAR(80) NOT NULL,
    level SMALLINT,
    icon VARCHAR(255),
    sort_order INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_skill_category
        FOREIGN KEY (category_id) REFERENCES skill_category(id)
        ON DELETE CASCADE
);

-- 6. project
CREATE TABLE project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    profile_id BIGINT NOT NULL,
    title VARCHAR(140) NOT NULL,
    slug VARCHAR(160) NOT NULL,
    summary VARCHAR(280) NOT NULL,
    description TEXT,
    repo_url VARCHAR(512),
    live_url VARCHAR(512),
    cover_image VARCHAR(512),
    start_date DATE,
    end_date DATE,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    sort_order INT NOT NULL DEFAULT 0,
    UNIQUE KEY uk_project_profile_slug (profile_id, slug),
    INDEX idx_project_featured (profile_id, featured, sort_order),
    CONSTRAINT fk_project_profile
        FOREIGN KEY (profile_id) REFERENCES profile(id)
        ON DELETE CASCADE
);

-- 7. experience
CREATE TABLE experience (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    profile_id BIGINT NOT NULL,
    company VARCHAR(150) NOT NULL,
    role VARCHAR(150) NOT NULL,
    location VARCHAR(100),
    start_date DATE NOT NULL,
    end_date DATE,
    current BOOLEAN NOT NULL DEFAULT FALSE,
    description TEXT,
    CONSTRAINT fk_experience_profile
        FOREIGN KEY (profile_id) REFERENCES profile(id)
        ON DELETE CASCADE
);

-- 8. education
CREATE TABLE education (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    profile_id BIGINT NOT NULL,
    institution VARCHAR(150) NOT NULL,
    degree VARCHAR(150) NOT NULL,
    field VARCHAR(150),
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT,
    CONSTRAINT fk_education_profile
        FOREIGN KEY (profile_id) REFERENCES profile(id)
        ON DELETE CASCADE
);

-- 9. contact_message
CREATE TABLE contact_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    profile_id BIGINT NOT NULL,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL,
    message TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    CONSTRAINT fk_contact_profile
        FOREIGN KEY (profile_id) REFERENCES profile(id)
        ON DELETE CASCADE
);

-- 10. project_skill (Relación @ManyToMany entre Project y Skill)
CREATE TABLE project_skill (
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    PRIMARY KEY (project_id, skill_id),
    CONSTRAINT fk_projectskill_project
        FOREIGN KEY (project_id) REFERENCES project(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_projectskill_skill
        FOREIGN KEY (skill_id) REFERENCES skill(id)
        ON DELETE CASCADE
);
