package nl.hva.kieskeurig.exception;

public class VoteLoadingException extends RuntimeException {
    public VoteLoadingException(String message) {
        super(message);
    }

    public VoteLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}

