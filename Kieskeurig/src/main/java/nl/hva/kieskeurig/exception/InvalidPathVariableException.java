package nl.hva.kieskeurig.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidPathVariableException extends RuntimeException {
    public InvalidPathVariableException(String message) {
        super(message);
    }
}
