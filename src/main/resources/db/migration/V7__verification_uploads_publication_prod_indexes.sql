ALTER TABLE profile
    ADD COLUMN portfolio_status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED',
    ADD COLUMN meta_title VARCHAR(160) NULL,
    ADD COLUMN meta_description VARCHAR(300) NULL,
    ADD COLUMN og_image VARCHAR(255) NULL;

ALTER TABLE project
    ADD COLUMN project_status VARCHAR(30) NOT NULL DEFAULT 'PUBLISHED',
    ADD COLUMN meta_title VARCHAR(160) NULL,
    ADD COLUMN meta_description VARCHAR(300) NULL,
    ADD COLUMN og_image VARCHAR(255) NULL;

CREATE TABLE stored_file (
    id BIGINT NOT NULL AUTO_INCREMENT,
    profile_id BIGINT NOT NULL,
    google_file_id VARCHAR(190) NOT NULL,
    public_url VARCHAR(600) NULL,
    file_type VARCHAR(40) NOT NULL,
    visibility VARCHAR(30) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size_bytes BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_stored_file_profile_type (profile_id, file_type),
    CONSTRAINT fk_stored_file_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

CREATE INDEX idx_profile_status_slug ON profile(portfolio_status, slug);
CREATE INDEX idx_project_profile_status_slug ON project(profile_id, project_status, slug);
CREATE INDEX idx_project_profile_featured_sort ON project(profile_id, featured, sort_order);
CREATE INDEX idx_skill_category_profile_sort ON skill_category(profile_id, sort_order);
CREATE INDEX idx_skill_category_sort ON skill(category_id, sort_order);
CREATE INDEX idx_global_skill_name ON global_skill(name);
CREATE INDEX idx_contact_message_profile_status_created ON contact_message(profile_id, status, created_at);
