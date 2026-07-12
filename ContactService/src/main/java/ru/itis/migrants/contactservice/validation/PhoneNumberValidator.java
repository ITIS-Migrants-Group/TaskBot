package ru.itis.migrants.contactservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.itis.migrants.contactservice.annotation.PhoneNumber;

import java.util.regex.Pattern;


public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    private final static Pattern PHONE_PATTERN = Pattern.compile("^\\+\\d+$");

    private int minLength;
    private int maxLength;

    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        minLength = constraintAnnotation.minLength();
        maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return true;
        }
        String trimmed = phoneNumber.trim();
        if (trimmed.isEmpty()) {
            return false;
        }
        if (!trimmed.startsWith("+")) {
            return false;
        }
        int digitsAfterPlus = trimmed.length() - 1;
        if (digitsAfterPlus < minLength || digitsAfterPlus > maxLength) {
            return false;
        }
        return PHONE_PATTERN.matcher(trimmed).matches();
    }
}
