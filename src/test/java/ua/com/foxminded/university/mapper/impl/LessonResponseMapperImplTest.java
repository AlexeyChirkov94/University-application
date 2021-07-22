package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.interfaces.LessonResponseMapper;
import static org.assertj.core.api.Assertions.assertThat;

class LessonResponseMapperImplTest {

    ApplicationContext context;
    LessonResponseMapper lessonResponseMapper;
    Lesson lesson;
    LessonResponse lessonResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        lessonResponseMapper = context.getBean(LessonResponseMapperImpl.class);
        lesson = Lesson.builder().withId(1L).build();
        lessonResponse = new LessonResponse();
        lessonResponse.setId(1L);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonResponseDto() {
        Lesson expected = lesson;
        Lesson actual = lessonResponseMapper.mapDtoToEntity(lessonResponse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(lessonResponseMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        LessonResponse expected = lessonResponse;
        LessonResponse actual = lessonResponseMapper.mapEntityToDto(lesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(lessonResponseMapper.mapEntityToDto(null)).isNull();
    }

}