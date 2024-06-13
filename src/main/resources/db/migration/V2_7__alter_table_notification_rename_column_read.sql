USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.notification
    CHANGE COLUMN `read` `acknowledged` boolean default false;