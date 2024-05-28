use airqualityhome;

ALTER TABLE airqualityhome.user_roles
    DROP CONSTRAINT IF EXISTS user_roles_ibfk_1;

ALTER TABLE airqualityhome.user_roles
    DROP CONSTRAINT IF EXISTS user_roles_ibfk_2;

ALTER TABLE airqualityhome.user_roles
    DROP INDEX IF EXISTS user_id;

ALTER TABLE airqualityhome.user_roles
    DROP INDEX IF EXISTS role_id;

ALTER TABLE airqualityhome.user_roles
    CHANGE COLUMN IF EXISTS  `user_id` `user_entity_id` bigint NOT NULL;

ALTER TABLE airqualityhome.user_roles
    CHANGE COLUMN IF EXISTS `role_id` `user_role_entity_id` bigint NOT NULL;

ALTER TABLE airqualityhome.user_roles
    ADD CONSTRAINT FOREIGN KEY (user_entity_id) REFERENCES airqualityhome.user(id);

ALTER TABLE airqualityhome.user_roles
    ADD CONSTRAINT FOREIGN KEY (user_role_entity_id) REFERENCES airqualityhome.role(id);

ALTER TABLE airqualityhome.user_roles
    ADD CONSTRAINT UNIQUE (user_entity_id, user_role_entity_id);