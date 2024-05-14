package io.abun.wmb.ErrorHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ValidatorUtil {
    private final Validator validator;
    public void validate(Object toValidate) {
        Set<ConstraintViolation<Object>> violations = validator.validate(toValidate);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
