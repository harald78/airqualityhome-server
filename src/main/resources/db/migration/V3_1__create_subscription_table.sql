USE airqualityhome;

CREATE TABLE if not exists airqualityhome.`push_subscription` (
   `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
   `user_id` bigint NOT NULL,
   `endpoint` varchar(1024) NOT NULL,
   `publicKey` varchar(1024) NOT NULL,
   `auth` varchar(512) NOT NULL,
   `created` timestamp NOT NULL,
   `updated` timestamp NOT NULL,
   CONSTRAINT user_id_fk1 FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id),
   CONSTRAINT user_unique UNIQUE (user_id)
);