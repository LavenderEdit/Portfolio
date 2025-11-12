/**
 * Author:  Studios TKOH!
 * Created: Nov 12, 2025
 */
USE `studiostkoh.portafolio`;

CREATE TABLE certificate (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    created_at datetime(6) NOT NULL,
    created_by VARCHAR(150),
    updated_at datetime(6),
    updated_by VARCHAR(150),
    version BIGINT,
    
    profile_id BIGINT NOT NULL,
    education_id BIGINT NULL,
    
    name VARCHAR(255) NOT NULL,
    description TEXT,
    image_url VARCHAR(512),
    file_id VARCHAR(255),
    
    CONSTRAINT fk_certificate_profile
        FOREIGN KEY (profile_id) REFERENCES profile(id)
        ON DELETE CASCADE,
        
    CONSTRAINT fk_certificate_education
        FOREIGN KEY (education_id) REFERENCES education(id)
        ON DELETE SET NULL
);