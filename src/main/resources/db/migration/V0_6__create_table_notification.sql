USE airqualityhome;

CREATE TABLE IF NOT EXISTS `notification` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` bigint NOT NULL,
    `measurement_id` bigint NOT NULL,
    `message` varchar(255),
    `read` boolean,
    `read_at` timestamp,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    FOREIGN KEY (user_id) REFERENCES airqualityhome.user(id),
    FOREIGN KEY (measurement_id) REFERENCES airqualityhome.measurement(id)
);

