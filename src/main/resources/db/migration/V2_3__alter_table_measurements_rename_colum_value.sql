USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.measurement
    CHANGE COLUMN `value` `sensorValue` DOUBLE NOT NULL;