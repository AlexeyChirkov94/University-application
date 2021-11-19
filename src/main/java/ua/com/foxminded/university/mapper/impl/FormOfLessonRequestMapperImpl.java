package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonRequestMapper;

@Component
public class FormOfLessonRequestMapperImpl implements FormOfLessonRequestMapper {

    @Override
    public FormOfLesson mapDtoToEntity(FormOfLessonRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return FormOfLesson.builder()
                    .withId(dto.getId())
                    .withName(dto.getName())
                    .withDuration(dto.getDuration())
                    .build();
        }
    }

}
