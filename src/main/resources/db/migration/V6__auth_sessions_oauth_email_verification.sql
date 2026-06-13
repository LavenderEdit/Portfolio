ALTER TABLE app_user
    MODIFY COLUMN password VARCHAR(255) NULL,
    ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN email_verified_at TIMESTAMP NULL,
    ADD COLUMN account_status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE';

CREATE TABLE refresh_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(128) NOT NULL,
    issued_at TIMESTAMP NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    revoked_at TIMESTAMP NULL,
    replaced_by_token_id BIGINT NULL,
    ip_address VARCHAR(80) NULL,
    user_agent VARCHAR(512) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_refresh_token_hash (token_hash),
    INDEX idx_refresh_token_user_revoked (user_id, revoked_at),
    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES app_user(id),
    CONSTRAINT fk_refresh_token_replaced_by FOREIGN KEY (replaced_by_token_id) REFERENCES refresh_token(id)
);

CREATE TABLE user_identity (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    provider VARCHAR(40) NOT NULL,
    provider_subject VARCHAR(190) NOT NULL,
    provider_email VARCHAR(190) NOT NULL,
    provider_email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    provider_avatar_url VARCHAR(512) NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_identity_provider_subject (provider, provider_subject),
    INDEX idx_user_identity_user (user_id),
    CONSTRAINT fk_user_identity_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);

CREATE TABLE email_verification_token (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(128) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    consumed_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL,
    resend_count INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_email_verification_token_hash (token_hash),
    INDEX idx_email_verification_user (user_id),
    CONSTRAINT fk_email_verification_user FOREIGN KEY (user_id) REFERENCES app_user(id)
);
