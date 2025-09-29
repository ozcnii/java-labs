package lab_3.exceptions;

public class InvalidTrackDataException extends RuntimeException {
    public InvalidTrackDataException(String message) {
        super(message);
    }
}