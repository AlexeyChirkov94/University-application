package ua.com.foxminded.university.service.validator;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.service.exception.ValidateException;
import java.util.function.Function;
import java.util.regex.Pattern;

@Component
public class UserValidatorImpl implements UserValidator{
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&*+/=?`{}~^.-]+@[a-zA-Z0-9.-]+$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[_!-?@#$%]).{6,20})");

    @Override
    public void validate(UserRequest userDto) {
        if (userDto == null) {
            throw new ValidateException("Field User is null");
        }
        validateEmail(userDto);
        validatePassword(userDto);
    }

    private static void validateEmail(UserRequest userRequest){
        validateString(EMAIL_PATTERN, userRequest, UserRequest::getEmail, "Email do not match the pattern");
    }

    private static void validatePassword(UserRequest userRequest){
        validateString(PASSWORD_PATTERN, userRequest, UserRequest::getPassword, "Password do not match the pattern");
    }

    private static void validateString(Pattern pattern, UserRequest userRequest, Function<UserRequest, String> function,
                                       String exceptionMessage) {
        if (!pattern.matcher(function.apply(userRequest)).matches()) {
            throw new ValidateException(exceptionMessage);
        }
    }

}
