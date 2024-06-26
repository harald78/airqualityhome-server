USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.push_subscription
    DROP CONSTRAINT IF EXISTS user_unique;