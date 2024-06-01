USE airqualityhome;

CREATE INDEX IF NOT EXISTS idx_email ON airqualityhome.aq_user(email);
CREATE INDEX IF NOT EXISTS idx_sensor_base_name ON airqualityhome.sensor_base(name);
CREATE INDEX IF NOT EXISTS idx_sensor_type_type ON airqualityhome.sensor_type(type);
CREATE INDEX IF NOT EXISTS idx_sensor_type_name ON airqualityhome.sensor_type(name);
CREATE INDEX IF NOT EXISTS idx_sensor_uuid ON airqualityhome.sensor(uuid);
CREATE INDEX IF NOT EXISTS idx_sensor_sensor_base_sensor_type_id ON airqualityhome.sensor(sensor_base_sensor_type_id);
CREATE INDEX IF NOT EXISTS idx_measurement_sensor_id ON airqualityhome.measurement(sensor_id);
CREATE INDEX IF NOT EXISTS idx_notification_user_id ON airqualityhome.notification(user_id);
CREATE INDEX IF NOT EXISTS idx_register_request_user_id ON airqualityhome.register_request(user_id);
CREATE INDEX IF NOT EXISTS idx_register_request_sensor_base_id ON airqualityhome.register_request(sensor_base_id);




