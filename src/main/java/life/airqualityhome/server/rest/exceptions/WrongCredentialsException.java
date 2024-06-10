package life.airqualityhome.server.rest.exceptions;

public class WrongCredentialsException extends RuntimeException {
    public WrongCredentialsException() {
        super("Credentials are wrong");
    }
}
