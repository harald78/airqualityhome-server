USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.measurement
    CHANGE COLUMN `sensorValue` `sensor_value` DOUBLE NOT NULL;