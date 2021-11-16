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
    Lesson emptyLesson;
    LessonResponse lessonResponse;
    LessonResponse emptyLessonResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        lessonResponseMapper = context.getBean(LessonResponseMapperImpl.class);
        lesson = Lesson.builder().withId(1L).build();
        emptyLesson = Lesson.builder().withId(0L).build();
        lessonResponse = new LessonResponse();
        lessonResponse.setId(1L);
        emptyLessonResponse = new LessonResponse();
        emptyLessonResponse.setId(0L);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        LessonResponse expected = lessonResponse;
        LessonResponse actual = lessonResponseMapper.mapEntityToDto(lesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntityWhichNotFoundedInDB() {
        LessonResponse expected = emptyLessonResponse;
        LessonResponse actual = lessonResponseMapper.mapEntityToDto(emptyLesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(lessonResponseMapper.mapEntityToDto(null)).isNull();
    }

}
