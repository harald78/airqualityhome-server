USE airqualityhome;

CREATE TABLE IF NOT EXISTS `user` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `email` varchar(255) NOT NULL,
    `name` varchar(255) NOT NULL,
    `created` timestamp NOT NULL,
    `updated` timestamp NOT NULL,
    `password` varchar(512) NOT NULL
);

