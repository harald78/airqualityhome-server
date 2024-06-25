USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.push_subscription
    CHANGE COLUMN `publicKey` `public_key` varchar(1024) NOT NULL;