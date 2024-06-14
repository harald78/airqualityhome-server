package life.airqualityhome.server.rest.exceptions;

public class PasswordRequiredException extends RuntimeException {
    public PasswordRequiredException() {
        super("Password is required");
    }
}
