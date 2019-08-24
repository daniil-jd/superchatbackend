package ru.itpark.validation;

import ru.itpark.dto.chat.room.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class ChatMembersValidator implements ConstraintValidator<ChatMembers, Set<UserDto>> {
    private int membersCount;
    @Override
    public void initialize(ChatMembers constraintAnnotation) {
    }

    @Override
    public boolean isValid(Set<UserDto> strings, ConstraintValidatorContext constraintValidatorContext) {
        if (strings == null) {
            return true;
        }
        return strings.size() > 1;
    }
}
