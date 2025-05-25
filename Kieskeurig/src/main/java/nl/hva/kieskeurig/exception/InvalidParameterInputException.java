package nl.hva.kieskeurig.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidParameterInputException extends RuntimeException {
    public InvalidParameterInputException(String message) {
        super(message);
    }
}
