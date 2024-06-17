USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.sensor_type
 DROP CONSTRAINT IF EXISTS type_check;

UPDATE airqualityhome.sensor_type SET type = 'VOC' WHERE id = 3;

ALTER TABLE IF EXISTS  airqualityhome.sensor_type
 ADD CONSTRAINT type_check CHECK ( type IN ('TEMPERATURE', 'HUMIDITY', 'PRESSURE', 'VOC', 'H2', 'LPG', 'CO', 'ALCOHOL', 'PROPANE', 'PARTICLE', 'LIGHT') )