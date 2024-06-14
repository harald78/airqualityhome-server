USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`sensor_type` (
     `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
     `name` varchar(50) NOT NULL,
     `type` varchar(11) NOT NULL,
     `max_value` double NOT NULL,
     `min_value` double NOT NULL,
     `created` timestamp NOT NULL,
     `updated` timestamp NOT NULL,
    CONSTRAINT type_check CHECK (type IN ('TEMPERATURE', 'HUMIDITY', 'PRESSURE', 'GAS', 'PARTICLE', 'LIGHT'))

);

