USE airqualityhome;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS user_roles_ibfk_1;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS user_user_role_fk1;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS user_roles_ibfk_2;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS user_user_role_fk2;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS user_user_role_uc;

ALTER TABLE airqualityhome.aq_user_roles
    DROP INDEX IF EXISTS user_id;

ALTER TABLE airqualityhome.aq_user_roles
    DROP INDEX IF EXISTS role_id;

ALTER TABLE airqualityhome.aq_user_roles
    CHANGE COLUMN `user_id` `user_entity_id` bigint NOT NULL;

ALTER TABLE airqualityhome.aq_user_roles
    CHANGE COLUMN `role_id` `user_role_entity_id` bigint NOT NULL;

ALTER TABLE airqualityhome.aq_user_roles
    ADD CONSTRAINT fk_user_roles_1 FOREIGN KEY (user_entity_id) REFERENCES airqualityhome.aq_user(id);

ALTER TABLE airqualityhome.aq_user_roles
    ADD CONSTRAINT fk_user_roles_2 FOREIGN KEY (user_role_entity_id) REFERENCES airqualityhome.aq_role(id);

ALTER TABLE airqualityhome.aq_user_roles
    ADD CONSTRAINT uc_user_roles_1 UNIQUE (user_entity_id, user_role_entity_id);
