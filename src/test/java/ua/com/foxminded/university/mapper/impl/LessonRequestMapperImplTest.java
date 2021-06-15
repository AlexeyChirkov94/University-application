package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.interfaces.LessonRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;

class LessonRequestMapperImplTest {

    ApplicationContext context;
    LessonRequestMapper lessonRequestMapper;
    Lesson lesson;
    LessonRequest lessonRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        lessonRequestMapper = context.getBean(LessonRequestMapperImpl.class);
        lesson = Lesson.builder().withId((long)1).build();
        lessonRequest = new LessonRequest();
        lessonRequest.setId((long)1);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDto() {
        Lesson expected = lesson;
        Lesson actual = lessonRequestMapper.mapDtoToEntity(lessonRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(lessonRequestMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        LessonRequest expected = lessonRequest;
        LessonRequest actual = lessonRequestMapper.mapEntityToDto(lesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(lessonRequestMapper.mapEntityToDto(null)).isNull();
    }

}