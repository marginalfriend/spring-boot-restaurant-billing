package io.abun.wmb.ErrorHandler;

import io.abun.wmb.CommonResponse.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler( {ResponseStatusException.class} )
    public ResponseEntity<?> responseStatusException (ResponseStatusException exception) {
        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(exception.getStatusCode().value())
                .message(exception.getReason())
                .build();

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(response);
    }

    @ExceptionHandler( {ConstraintViolationException.class} )
    public ResponseEntity<?> constraintViolationException (ConstraintViolationException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        CommonResponse<?> response = CommonResponse.builder()
                .statusCode(badRequest.value())
                .message(exception.getMessage())
                .build();
        return ResponseEntity
                .status(badRequest)
                .body(response);
    }
}
