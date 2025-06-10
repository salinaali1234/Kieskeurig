package nl.hva.kieskeurig.config;

import nl.hva.kieskeurig.dto.ErrorResponse;
import nl.hva.kieskeurig.exception.BadRequestException;
import nl.hva.kieskeurig.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {

        ErrorResponse response = new ErrorResponse(ex.getMessage(), new Date());

        return ResponseEntity.status(getHttpStatus(ex)).body(response);
    }


    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        ErrorResponse response = new ErrorResponse(ex.getMessage(), new Date());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private  HttpStatus getHttpStatus(Exception ex) {
        if (ex instanceof NotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
