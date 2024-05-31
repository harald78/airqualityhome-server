USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`measurement` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `sensor_id` bigint NOT NULL,
    `timestamp` timestamp NOT NULL,
    `unit` varchar(10) NOT NULL,
    `value` double NOT NULL,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    CONSTRAINT measurement_fk_1 FOREIGN KEY (sensor_id) REFERENCES airqualityhome.sensor(id),
    CONSTRAINT unit_check CHECK (unit IN ('CELSIUS', 'FAHRENHEIT', 'M_BAR', 'PERCENT', 'PPM'))
) ENGINE=InnoDB;

