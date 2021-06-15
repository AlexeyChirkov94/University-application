package ua.com.foxminded.university.service.validator;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.service.exception.ValidateException;

import java.util.Arrays;

@Component
public class ScienceDegreeValidatorImpl implements ScienceDegreeValidator {

    @Override
    public void validate(ScienceDegree entity) {

        if (entity == null){
            throw new ValidateException("Science Degree with this id doesn`t exist");
        }
        if (!Arrays.asList(ScienceDegree.values()).contains(entity)){
            throw new ValidateException("Science Degree with this id doesn`t exist");
        }
    }

}
