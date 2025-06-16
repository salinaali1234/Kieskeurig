package nl.hva.kieskeurig.config;

import nl.hva.kieskeurig.dto.ErrorResponse;
import nl.hva.kieskeurig.exception.BadRequestException;
import nl.hva.kieskeurig.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application.
 * <p>
 * This class handles specific custom exceptions and returns a consistent error response structure
 * to the client. It's annotated with {@link ControllerAdvice} to apply globally to all controllers.
 */
@ControllerAdvice
public class GlobalErrorHandler {

    /**
     * Handles {@link NotFoundException} exceptions.
     *
     * @param ex the exception thrown when a resource is not found
     * @return a {@link ResponseEntity} containing an {@link ErrorResponse} with a NOT_FOUND status
     */
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), new Date());
        return ResponseEntity.status(getHttpStatus(ex)).body(response);
    }

    /**
     * Handles {@link BadRequestException} exceptions.
     *
     * @param ex the exception thrown when a bad request occurs (e.g., invalid input)
     * @return a {@link ResponseEntity} containing a structured error message with a BAD_REQUEST status
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Helper method to determine the appropriate {@link HttpStatus} based on the exception type.
     *
     * @param ex the exception to evaluate
     * @return the corresponding {@link HttpStatus}
     */
    private HttpStatus getHttpStatus(Exception ex) {
        if (ex instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}