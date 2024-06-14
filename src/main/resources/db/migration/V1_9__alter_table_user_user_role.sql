USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.sensor_base_sensor_types
    DROP CONSTRAINT IF EXISTS sensor_base_sensor_type_fk_1;

ALTER TABLE IF EXISTS airqualityhome.sensor_base_sensor_types
    DROP CONSTRAINT IF EXISTS sensor_base_sensor_type_fk_2;

ALTER TABLE IF EXISTS airqualityhome.sensor_base_sensor_types
    DROP INDEX IF EXISTS sensor_base_sensor_type_uc_1;

ALTER TABLE IF EXISTS airqualityhome.sensor_base_sensor_types
    DROP INDEX IF EXISTS sensor_base_sensor_type_fk_2;

ALTER TABLE IF EXISTS airqualityhome.sensor_base_sensor_types
    CHANGE COLUMN `sensor_base_id` `sensor_base_entity_id` bigint NOT NULL;

ALTER TABLE IF EXISTS airqualityhome.sensor_base_sensor_types
    CHANGE COLUMN `sensor_type_id` `sensor_types_id` bigint NOT NULL;

ALTER TABLE airqualityhome.sensor_base_sensor_types
    ADD CONSTRAINT sensor_base_sensor_type_fk_1 FOREIGN KEY (sensor_base_entity_id) REFERENCES airqualityhome.sensor_base(id);

ALTER TABLE airqualityhome.sensor_base_sensor_types
    ADD CONSTRAINT sensor_base_sensor_type_fk_2 FOREIGN KEY (sensor_types_id) REFERENCES airqualityhome.sensor_type(id);

ALTER TABLE airqualityhome.sensor_base_sensor_types
    ADD CONSTRAINT sensor_base_sensor_type_uc_1 UNIQUE (sensor_base_entity_id, sensor_types_id);
