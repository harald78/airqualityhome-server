use airqualityhome;

CREATE TABLE IF NOT EXISTS `refresh_token` (
   `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `token` varchar(4096) NOT NULL,
   `expiry_date` timestamp NOT NULL,
   `user_id` bigint NOT NULL,
   FOREIGN KEY (user_id) REFERENCES airqualityhome.user(id)
);