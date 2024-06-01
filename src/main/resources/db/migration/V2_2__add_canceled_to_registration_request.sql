USE airqualityhome;

ALTER TABLE IF EXISTS `register_request`
  ADD COLUMN `canceled` BOOLEAN DEFAULT false;