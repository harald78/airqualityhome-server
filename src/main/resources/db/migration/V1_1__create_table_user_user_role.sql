USE airqualityhome;

CREATE TABLE IF NOT EXISTS `user_user_role` (
    `id` bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` bigint NOT NULL,
    `role_id` bigint NOT NULL,
    FOREIGN KEY (user_id) REFERENCES airqualityhome.user(id),
    FOREIGN KEY (role_id) REFERENCES airqualityhome.role(id),
    UNIQUE (user_id, role_id)
);