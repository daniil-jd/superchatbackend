package ru.itpark.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = ChatMembersValidator.class)
public @interface ChatMembers {
    String message() default "chat members must be more than 1";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
