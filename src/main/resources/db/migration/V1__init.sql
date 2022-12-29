CREATE TABLE app_user
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NULL,
    updated_at   datetime              NULL,
    username     VARCHAR(255)          NOT NULL,
    password     VARCHAR(255)          NULL,
    name         VARCHAR(255)          NULL,
    email        VARCHAR(255)          NULL,
    phone_number VARCHAR(255)          NULL,
    CONSTRAINT pk_app_user PRIMARY KEY (id)
);

CREATE TABLE app_user_refresh_token
(
    id           BIGINT AUTO_INCREMENT NOT NULL,
    created_at   datetime              NULL,
    updated_at   datetime              NULL,
    user_id      BIGINT                NULL,
    payload      TEXT                  NOT NULL,
    last_used_at datetime              NULL,
    CONSTRAINT pk_app_user_refresh_token PRIMARY KEY (id)
);

CREATE TABLE app_user_roles
(
    user_id BIGINT       NOT NULL,
    roles   VARCHAR(255) NULL
);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_id UNIQUE (id);

ALTER TABLE app_user_refresh_token
    ADD CONSTRAINT uc_app_user_refresh_token_id UNIQUE (id);

ALTER TABLE app_user_refresh_token
    ADD CONSTRAINT uc_app_user_refresh_token_payload UNIQUE (payload);

ALTER TABLE app_user_roles
    ADD CONSTRAINT uc_app_user_roles_user UNIQUE (user_id);

ALTER TABLE app_user
    ADD CONSTRAINT uc_app_user_username UNIQUE (username);

CREATE UNIQUE INDEX app_user_username_uindex ON app_user (username);

CREATE UNIQUE INDEX refresh_token_payload_uindex ON app_user_refresh_token (payload);

ALTER TABLE app_user_refresh_token
    ADD CONSTRAINT fk_app_user_refresh_token_on_user FOREIGN KEY (user_id) REFERENCES app_user (id);

CREATE INDEX refresh_token_user_id_index ON app_user_refresh_token (user_id);

ALTER TABLE app_user_roles
    ADD CONSTRAINT fk_app_user_roles_on_user FOREIGN KEY (user_id) REFERENCES app_user (id);
