USE airqualityhome;

CREATE TABLE IF NOT EXISTS `measurement` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `sensor_id` bigint NOT NULL,
    `timestamp` timestamp NOT NULL,
    `unit` ENUM('CELSIUS', 'FAHRENHEIT', 'M_BAR', 'PERCENT', 'PPM') NOT NULL,
    `value` double NOT NULL,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    FOREIGN KEY (sensor_id) REFERENCES airqualityhome.sensor(id)
);

