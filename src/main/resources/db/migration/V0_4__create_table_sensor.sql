USE airqualityhome;

CREATE TABLE IF NOT EXISTS `sensor` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY, # https://mariadb.com/kb/en/guiduuid-performance/
    `uuid` UUID NOT NULL,
    `sensor_base_sensor_type_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    `location` varchar(255),
    `alarm_max` double,
    `alarm_min`double,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    FOREIGN KEY (sensor_base_sensor_type_id) REFERENCES airqualityhome.sensor_base_sensor_type(id),
    FOREIGN KEY (user_id) REFERENCES airqualityhome.user(id),
    UNIQUE (uuid, sensor_base_sensor_type_id)
);

