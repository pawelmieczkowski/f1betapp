CREATE TABLE app_user
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `app_user_role` varchar(255) not null,
    `email`         varchar(255) not null UNIQUE,
    `first_name`    varchar(255) not null,
    `last_name`     varchar(50)  not null,
    `password`      varchar(100) not null,
    `enabled`       boolean      not null,
    `locked`        boolean      not null
);

CREATE TABLE confirmation_token
(
    `id`           BIGINT AUTO_INCREMENT PRIMARY KEY,
    `token`        varchar(255) not null,
    `created_At`   timestamp    not null,
    `expires_at`   timestamp    not null,
    `confirmed_at` timestamp,
    `app_user_id`  BIGINT       not null
);

ALTER TABLE confirmation_token
    ADD CONSTRAINT confirmation_token_app_user_id
        FOREIGN KEY (app_user_id) REFERENCES app_user (id);

CREATE TABLE circuit
(
    `id`        BIGINT PRIMARY KEY,
    `name`      varchar(255) not null,
    `location`  varchar(255) not null,
    `country`   varchar(255) not null,
    `latitude`  varchar(255),
    `longitude` varchar(255),
    `altitude`  varchar(255),
    `url`       varchar(255)
);

CREATE TABLE grand_prix
(
    `id`           BIGINT PRIMARY KEY,
    `year`         varchar(255),
    `round`        varchar(255),
    `circuit`      BIGINT,
    `name`         varchar(255),
    `date`         date,
    `localization` varchar(255),
    `country`      varchar(255),
    `time`         varchar(8),
    `url`          varchar(255),
    CONSTRAINT fk_circuit
        FOREIGN KEY (circuit)
            REFERENCES circuit (id)
)


