USE airqualityhome;

ALTER TABLE airqualityhome.aq_user
    CHANGE COLUMN `name` `username` varchar(255) NOT NULL;