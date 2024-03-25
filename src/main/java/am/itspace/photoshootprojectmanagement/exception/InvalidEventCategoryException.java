package am.itspace.photoshootprojectmanagement.exception;

public class InvalidEventCategoryException extends RuntimeException {

    public InvalidEventCategoryException() {
        super();
    }

    public InvalidEventCategoryException(String message) {
        super(message);
    }

    public InvalidEventCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEventCategoryException(Throwable cause) {
        super(cause);
    }
}