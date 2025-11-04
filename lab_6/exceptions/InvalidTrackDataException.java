package lab_6.exceptions;

public class InvalidTrackDataException extends RuntimeException {
    public InvalidTrackDataException(String message) {
        super(message);
    }
}
