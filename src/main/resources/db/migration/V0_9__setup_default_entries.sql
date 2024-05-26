USE airqualityhome;

INSERT INTO user (email, name, created, updated, password)
    values ('default@default.de', 'default', now(), now(), '$2b$12$rhti4e5sO6VCewjLxb5pJenr4bdeqEBYqSOUDuxA.qwiBOkq8kEG.');

INSERT INTO sensor_base (id, name, created, updated)
    values (1, 'AZEnvy', now(), now());

INSERT INTO sensor_type (id, name, type, min_value, max_value, created, updated)
    values (1, 'SHT30', 'TEMPERATURE', -40.0, 125.0, now(), now());

INSERT INTO sensor_type (id, name, type, min_value, max_value, created, updated)
    values (2, 'SHT30', 'HUMIDITY', 0.0, 100.0, now(), now());

INSERT INTO sensor_type (id, name, type, min_value, max_value, created, updated)
    values (3, 'MQ-2', 'GAS', 100.0, 10000.0, now(), now());

INSERT INTO sensor_base_sensor_type (sensor_base_id, sensor_type_id, created, updated)
    values (1, 1, now(), now());

INSERT INTO sensor_base_sensor_type (sensor_base_id, sensor_type_id, created, updated)
values (1, 2, now(), now());

INSERT INTO sensor_base_sensor_type (sensor_base_id, sensor_type_id, created, updated)
values (1, 3, now(), now());




