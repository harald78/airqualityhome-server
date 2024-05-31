USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`refresh_token` (
   `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `token` varchar(4096) NOT NULL,
   `expiry_date` timestamp NOT NULL,
   `user_id` bigint NOT NULL,
   CONSTRAINT refresh_token_fk1 FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id)
);