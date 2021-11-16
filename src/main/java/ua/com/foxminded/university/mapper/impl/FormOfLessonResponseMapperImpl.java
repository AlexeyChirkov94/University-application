package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonResponseMapper;

@Component
public class FormOfLessonResponseMapperImpl implements FormOfLessonResponseMapper {

    @Override
    public FormOfLessonResponse mapEntityToDto(FormOfLesson entity) {
        FormOfLessonResponse formOfLessonResponse = new FormOfLessonResponse();
        if (entity == null) {
            return null;
        } else if (entity.getId() == 0L){
            formOfLessonResponse.setId(0L);
            formOfLessonResponse.setName("");
            formOfLessonResponse.setDuration(0);
        } else {
            formOfLessonResponse.setId(entity.getId());
            formOfLessonResponse.setName(entity.getName());
            formOfLessonResponse.setDuration(entity.getDuration());
        }

        return formOfLessonResponse;
    }

}
