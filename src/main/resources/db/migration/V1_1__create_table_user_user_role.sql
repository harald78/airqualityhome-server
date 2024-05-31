USE airqualityhome;

CREATE TABLE IF NOT EXISTS airqualityhome.`user_user_role` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` bigint NOT NULL,
    `role_id` bigint NOT NULL,
    CONSTRAINT user_user_role_fk1 FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id),
    CONSTRAINT user_user_role_fk2 FOREIGN KEY (role_id) REFERENCES airqualityhome.aq_role(id),
    CONSTRAINT user_user_role_uc UNIQUE (user_id, role_id)
);