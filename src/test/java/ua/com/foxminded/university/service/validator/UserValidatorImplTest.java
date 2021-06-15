package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.dto.StudentRequest;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserValidatorImplTest {

    UserValidator userValidator;

    {
        userValidator = new UserValidatorImpl();
    }

    @Test
    void validateShouldThrowExceptionIfUserIsNull() {
        assertThatThrownBy(() -> userValidator.validate(null)).hasMessage("Field User is null");
    }

    @Test
    void validateShouldThrowExceptionIfUserEmailDontMatchThePattern() {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail("chirkov");
        studentRequest.setPassword("12!dF$5Gt4Kl&");

        assertThatThrownBy(() -> userValidator.validate(studentRequest)).hasMessage("Email do not match the pattern");
    }

    @Test
    void validateShouldThrowExceptionIfUserPasswordDontMatchThePattern() {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail("chirkov94@gamil.com");
        studentRequest.setPassword("12");

        assertThatThrownBy(() -> userValidator.validate(studentRequest)).hasMessage("Password do not match the pattern");
    }

    @Test
    void validateShouldThrowNoExceptionIfUserEmailAndPasswordMatchThePattern() {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail("chirkov94@gamil.com");
        studentRequest.setPassword("12!dF$5Gt4Kl&");

        assertThatCode(() -> userValidator.validate(studentRequest)).doesNotThrowAnyException();
    }

}
