USE airqualityhome;

ALTER TABLE IF EXISTS `register_request`
    ADD COLUMN `location` varchar(50) NOT NULL;