USE airqualityhome;

ALTER TABLE IF EXISTS `sensor`
  ADD COLUMN `alarm_active` BOOLEAN DEFAULT false;