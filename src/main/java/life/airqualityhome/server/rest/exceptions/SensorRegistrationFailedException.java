package life.airqualityhome.server.rest.exceptions;

public class SensorRegistrationFailedException extends RuntimeException {
    public SensorRegistrationFailedException(String message) {
        super(message);
    }
}
