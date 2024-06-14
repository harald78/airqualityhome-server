USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.sensor
    ADD COLUMN `warning_threshold` DOUBLE NOT NULL DEFAULT 0.0;

ALTER TABLE IF EXISTS airqualityhome.sensor
    ADD COLUMN `linear_correction_value` DOUBLE NOT NULL DEFAULT 0.0;