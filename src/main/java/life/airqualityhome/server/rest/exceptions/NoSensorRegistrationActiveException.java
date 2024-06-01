package life.airqualityhome.server.rest.exceptions;

public class NoSensorRegistrationActiveException extends RuntimeException {

    public NoSensorRegistrationActiveException(String message) {
        super(message);
    }
}
