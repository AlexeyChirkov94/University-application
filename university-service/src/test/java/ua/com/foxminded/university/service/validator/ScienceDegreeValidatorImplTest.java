package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import ua.com.foxminded.university.entity.ScienceDegree;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScienceDegreeValidatorImplTest {

    ScienceDegreeValidator  scienceDegreeValidator;

    {
        scienceDegreeValidator = new ScienceDegreeValidatorImpl ();
    }

    @Test
    void validateShouldThrowExceptionIfScienceDegreeIsNull() {
        assertThatThrownBy(() -> scienceDegreeValidator.validate(null)).hasMessage("Science Degree with this id doesn`t exist");
    }

    @Test
    void validateShouldThrowNoExceptionIfScienceDegreeIsExistAndInRangeOfIds() {
        assertThatCode(() -> scienceDegreeValidator.validate(ScienceDegree.getById(1))).doesNotThrowAnyException();
    }

}
