USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.push_subscription
    DROP CONSTRAINT IF EXISTS user_id_fk1;

ALTER TABLE IF EXISTS airqualityhome.push_subscription
    DROP INDEX IF EXISTS user_unique;

ALTER TABLE airqualityhome.push_subscription
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES airqualityhome.aq_user(id);