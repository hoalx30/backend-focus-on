package spring.boot.apis.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpperCaseConstraint implements ConstraintValidator<UpperCase, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.equals(value.toUpperCase());
    }
}
