USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`register_request` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` bigint NOT NULL,
    `active` boolean DEFAULT true,
    `sensor_base_id` bigint NOT NULL,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    CONSTRAINT register_request_fk1 FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id),
    CONSTRAINT register_request_fk2 FOREIGN KEY (sensor_base_id) REFERENCES airqualityhome.sensor_base(id)
);

