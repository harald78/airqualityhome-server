USE airqualityhome;

CREATE TABLE IF NOT EXISTS `sensor_base_sensor_type` (
     `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `sensor_base_id` bigint NOT NULL,
     `sensor_type_id` bigint NOT NULL,
     `created` timestamp NOT NULL,
     `updated` timestamp NOT NULL,
     FOREIGN KEY (sensor_base_id) REFERENCES airqualityhome.sensor_base(id),
     FOREIGN KEY (sensor_type_id) REFERENCES airqualityhome.sensor_type(id),
     UNIQUE (sensor_base_id, sensor_type_id)
);

