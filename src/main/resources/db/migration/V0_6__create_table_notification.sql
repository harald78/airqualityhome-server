USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`notification` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` bigint NOT NULL,
    `measurement_id` bigint NOT NULL,
    `message` varchar(255),
    `read` boolean,
    `read_at` timestamp,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    CONSTRAINT notification_fk_1 FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id),
    CONSTRAINT notification_fk_2 FOREIGN KEY (measurement_id) REFERENCES airqualityhome.measurement(id)
);

