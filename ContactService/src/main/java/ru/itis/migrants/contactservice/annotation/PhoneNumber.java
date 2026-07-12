package ru.itis.migrants.contactservice.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.itis.migrants.contactservice.validation.PhoneNumberValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {
    int minLength() default 11;
    int maxLength() default 15;

    String message() default "Phone number don't correct";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
