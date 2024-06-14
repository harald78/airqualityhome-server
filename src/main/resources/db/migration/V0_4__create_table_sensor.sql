USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`sensor` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `uuid` UUID NOT NULL,
    `sensor_base_sensor_type_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    `location` varchar(255),
    `alarm_max` double,
    `alarm_min`double,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    CONSTRAINT fk_1 FOREIGN KEY (sensor_base_sensor_type_id) REFERENCES airqualityhome.sensor_base_sensor_type(id),
    CONSTRAINT fk_2 FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id),
    CONSTRAINT uc_uuid_sensor_base_sensor_type UNIQUE (uuid, sensor_base_sensor_type_id)
) ENGINE=InnoDB;

