package life.airqualityhome.server.rest.exceptions;

public class RegistrationPendingException extends RuntimeException {

    public RegistrationPendingException(String message) {
        super(message);
    }
}
