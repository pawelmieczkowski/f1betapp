CREATE TABLE app_user
(
    `id`            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `app_user_role` varchar(255) not null,
    `email`         varchar(255) not null UNIQUE,
    `username`      varchar(255) not null,
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
    `year`         SMALLINT,
    `round`        varchar(255),
    `circuit_id`   BIGINT,
    `name`         varchar(255),
    `date`         date,
    `localization` varchar(255),
    `country`      varchar(255),
    `time`         varchar(8),
    `url`          varchar(255),
    `driver_name`  varchar(255),
    CONSTRAINT fk_circuit
        FOREIGN KEY (circuit_id)
            REFERENCES circuit (id)
);

CREATE TABLE race_finish_status
(
    `id`     BIGINT PRIMARY KEY,
    `status` varchar(255) not null
);

CREATE TABLE driver
(
    `id`            BIGINT PRIMARY KEY,
    `driver_number` varchar(255),
    `driver_code`   varchar(255),
    `forename`      varchar(255),
    `surname`       varchar(255),
    `date_of_birth` varchar(255),
    `nationality`   varchar(255),
    `url`           varchar(2555)
);

CREATE TABLE team
(
    `id`          BIGINT PRIMARY KEY,
    `name`        varchar(255),
    `nationality` varchar(255),
    `url`         varchar(255)
);

CREATE TABLE race_result
(
    `id`                     BIGINT PRIMARY KEY,
    `grand_prix_id`          BIGINT,
    `driver_id`              BIGINT,
    `driver_number`          varchar(255),
    `driver_name`            varchar(255),
    `team_name`              varchar(255),
    `starting_grid_position` TINYINT,
    `finishing_position`     varchar(255),
    `points`                 FLOAT,
    `laps`                   varchar(255),
    `time`                   varchar(255),
    `time_in_milliseconds`   BIGINT,
    `fastest_lap_time`       varchar(255),
    `fastest_lap_speed`      varchar(255),
    `status`                 varchar(255),
    `ranking`                TINYINT,

    CONSTRAINT fk_grand_prix_race_result
        FOREIGN KEY (grand_prix_id)
            REFERENCES grand_prix (id)
);

CREATE TABLE qualification_result
(
    `id`            BIGINT PRIMARY KEY,
    `grand_prix_id` BIGINT,
    `driver_number` varchar(255),
    `driver_name`   varchar(255),
    `driver_id`     BIGINT,
    `team_name`     varchar(255),
    `result`        TINYINT,
    `q1time`        varchar(255),
    `q2time`        varchar(255),
    `q3time`        varchar(255),

    CONSTRAINT fk_grand_prix_qualification_result
        FOREIGN KEY (grand_prix_id)
            REFERENCES grand_prix (id)
);

CREATE TABLE refresh_token
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY,
    `app_user_id` BIGINT,
    `token`       varchar(255),
    `expiry_date` datetime(6),
    CONSTRAINT fk_app_user_refresh_token
        FOREIGN KEY (app_user_id)
            REFERENCES app_user (id)
);