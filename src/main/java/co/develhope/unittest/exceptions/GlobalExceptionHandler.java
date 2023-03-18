package co.develhope.unittest.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException(EntityNotFoundException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity handleIllegalArgumentException(IllegalArgumentException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity handleNullPointerException(NullPointerException e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error - null object");
    }

    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataIntegrityViolationException.class})
    public ResponseEntity handleDataAccessException(DataAccessException e) {
        System.out.println(e.getMessage());
        if(e instanceof DataIntegrityViolationException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database persistence statement error");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

}
