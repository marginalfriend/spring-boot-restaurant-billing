package io.abun.wmb.ErrorHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler( {ResponseStatusException.class} )
    public ResponseEntity<?> responseStatusException (ResponseStatusException exception) {
        return ResponseEntity
                .status(exception.getStatusCode())
                .body(exception.getReason());
    }

}
