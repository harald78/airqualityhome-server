USE airqualityhome;

CREATE TABLE IF NOT EXISTS `register_request` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` bigint NOT NULL,
    `active` boolean DEFAULT true,
    `sensor_base_id` bigint NOT NULL,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    FOREIGN KEY (user_id) REFERENCES airqualityhome.user(id),
    FOREIGN KEY (sensor_base_id) REFERENCES airqualityhome.sensor_base(id)
);

