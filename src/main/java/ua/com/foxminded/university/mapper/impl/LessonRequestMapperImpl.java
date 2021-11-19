package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.interfaces.LessonRequestMapper;

@Component
public class LessonRequestMapperImpl implements LessonRequestMapper {

    @Override
    public Lesson mapDtoToEntity(LessonRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Lesson.builder()
                    .withId(dto.getId())
                    .withTimeOfStartLesson(dto.getTimeOfStartLesson())
                    .build();
        }
    }

}
