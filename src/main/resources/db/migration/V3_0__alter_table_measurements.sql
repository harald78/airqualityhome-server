USE airqualityhome;

ALTER TABLE IF EXISTS airqualityhome.measurement
    DROP CONSTRAINT IF EXISTS unit_check;

UPDATE airqualityhome.measurement SET unit='PPB' WHERE airqualityhome.measurement.sensor_id = 3;

ALTER TABLE IF EXISTS airqualityhome.measurement
    ADD CONSTRAINT unit_check CHECK (unit IN ('CELSIUS', 'FAHRENHEIT', 'M_BAR', 'PERCENT', 'PPM', 'PPB'))