package life.airqualityhome.server.rest.exceptions;

public class UsernameRequiredException extends RuntimeException {
    public UsernameRequiredException() {
        super("Username is required");
    }
}
