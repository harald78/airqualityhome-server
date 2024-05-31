USE airqualityhome;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS user_roles_ibfk_1;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS fk_user_roles_1;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS user_roles_ibfk_2;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS fk_user_roles_2;

ALTER TABLE airqualityhome.aq_user_roles
    DROP INDEX IF EXISTS user_entity_id;

ALTER TABLE airqualityhome.aq_user_roles
    DROP INDEX IF EXISTS user_role_entity_id;

ALTER TABLE airqualityhome.aq_user_roles
    DROP CONSTRAINT IF EXISTS uc_user_roles_1;

ALTER TABLE airqualityhome.aq_user_roles
    CHANGE COLUMN `user_role_entity_id` `roles_id` bigint NOT NULL;

ALTER TABLE airqualityhome.aq_user_roles
    ADD CONSTRAINT user_roles_fk_1 FOREIGN KEY (user_entity_id) REFERENCES airqualityhome.aq_user(id);

ALTER TABLE airqualityhome.aq_user_roles
    ADD CONSTRAINT user_roles_fk_2 FOREIGN KEY (roles_id) REFERENCES airqualityhome.aq_role(id);

ALTER TABLE airqualityhome.aq_user_roles
    ADD CONSTRAINT uc_user_roles_1 UNIQUE (user_entity_id, roles_id);
