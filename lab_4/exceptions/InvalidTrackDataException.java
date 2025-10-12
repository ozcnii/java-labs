package lab_4.exceptions;

public class InvalidTrackDataException extends RuntimeException {
    public InvalidTrackDataException(String message) {
        super(message);
    }
}
