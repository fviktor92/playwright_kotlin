package common.exceptions;

public class MissingSystemPropertyException extends Exception {
    public MissingSystemPropertyException(String message) {
        super(message);
    }
}
