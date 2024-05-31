USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`sensor_base_sensor_type` (
     `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `sensor_base_id` bigint NOT NULL,
     `sensor_type_id` bigint NOT NULL,
     `created` timestamp NOT NULL,
     `updated` timestamp NOT NULL,
     CONSTRAINT sensor_base_sensor_type_fk_1 FOREIGN KEY (sensor_base_id) REFERENCES airqualityhome.sensor_base(id),
     CONSTRAINT sensor_base_sensor_type_fk_2 FOREIGN KEY (sensor_type_id) REFERENCES airqualityhome.sensor_type(id),
     CONSTRAINT sensor_base_sensor_type_uc_1 UNIQUE (sensor_base_id, sensor_type_id)
);

