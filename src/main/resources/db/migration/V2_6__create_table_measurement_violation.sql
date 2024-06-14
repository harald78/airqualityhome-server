USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`measurement_violation` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `sensor_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `measurement_id` BIGINT NOT NULL,
    `type` VARCHAR(3) NOT NULL,
    `alarm_value` double NOT NULL,
    `sensor_value` double NOT NULL,
    `corrected_value` double NOT NULL,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    CONSTRAINT fk_1_mv FOREIGN KEY (sensor_id) REFERENCES airqualityhome.sensor(id),
    CONSTRAINT fk_2_mv FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id),
    CONSTRAINT fk_3_mv FOREIGN KEY (measurement_id) REFERENCES airqualityhome.measurement(id)
) ENGINE=InnoDB;

