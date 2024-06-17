USE airqualityhome;

INSERT INTO airqualityhome.sensor_type (id, name, type, min_value, max_value, created, updated)
    values (4, 'MQ-2', 'H2',100.0, 10000.0, now(), now());

INSERT INTO airqualityhome.sensor_type (id, name, type, min_value, max_value, created, updated)
    values (5, 'MQ-2', 'LPG', 100.0, 10000.0, now(), now());

INSERT INTO airqualityhome.sensor_type (id, name, type, min_value, max_value, created, updated)
values (6, 'MQ-2', 'CO', 100.0, 10000.0, now(), now());

INSERT INTO airqualityhome.sensor_type (id, name, type, min_value, max_value, created, updated)
values (7, 'MQ-2', 'ALCOHOL', 100.0, 10000.0, now(), now());

INSERT INTO airqualityhome.sensor_type (id, name, type, min_value, max_value, created, updated)
values (8, 'MQ-2', 'PROPANE', 100.0, 10000.0, now(), now());

INSERT INTO airqualityhome.sensor_base_sensor_types (sensor_base_entity_id, sensor_types_id, created, updated)
    values (1, 4, now(), now());

INSERT INTO airqualityhome.sensor_base_sensor_types (sensor_base_entity_id, sensor_types_id, created, updated)
values (1, 5, now(), now());

INSERT INTO airqualityhome.sensor_base_sensor_types (sensor_base_entity_id, sensor_types_id, created, updated)
values (1, 6, now(), now());

INSERT INTO airqualityhome.sensor_base_sensor_types (sensor_base_entity_id, sensor_types_id, created, updated)
values (1, 7, now(), now());

INSERT INTO airqualityhome.sensor_base_sensor_types (sensor_base_entity_id, sensor_types_id, created, updated)
values (1, 8, now(), now());




