use airqualityhome;

ALTER TABLE user
    CHANGE COLUMN `name` `username` varchar(255) NOT NULL;