USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`aq_role` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` varchar(255) NOT NULL,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL
);