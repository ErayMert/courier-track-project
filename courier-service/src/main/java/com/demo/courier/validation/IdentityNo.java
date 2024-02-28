package com.demo.courier.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityNoValidator.class)
public @interface IdentityNo {
    String message() default "Identity number must be 11 characters long";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
