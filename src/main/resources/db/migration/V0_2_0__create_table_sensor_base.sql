USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`sensor_base` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(50) NOT NULL UNIQUE,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL
);

