USE airqualityhome;

CREATE TABLE IF NOT EXISTS `sensor_type` (
     `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `name` varchar(50) NOT NULL,
     `type` ENUM('TEMPERATURE', 'HUMIDITY', 'PRESSURE', 'GAS', 'PARTICLE', 'LIGHT') NOT NULL,
     `max_value` double NOT NULL,
     `min_value` double NOT NULL,
     `created` timestamp NOT NULL,
     `updated` timestamp NOT NULL
);

