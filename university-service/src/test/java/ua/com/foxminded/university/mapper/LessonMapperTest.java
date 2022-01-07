package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Lesson;
import static org.assertj.core.api.Assertions.assertThat;

class LessonMapperTest {

    ApplicationContext context;
    LessonMapper lessonMapper;
    Lesson lesson;
    Lesson emptyLesson;
    LessonResponse lessonResponse;
    LessonRequest lessonRequest;
    LessonResponse emptyLessonResponse;


    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        lessonMapper = context.getBean(LessonMapper.class);

        lesson = Lesson.builder().withId(1L).build();
        emptyLesson = Lesson.builder().build();

        lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);

        lessonResponse = new LessonResponse();
        lessonResponse.setId(1L);

        emptyLessonResponse = new LessonResponse();
        emptyLessonResponse.setId(0L);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsLessonEntity() {
        LessonResponse expected = lessonResponse;
        LessonResponse actual = lessonMapper.mapEntityToDto(lesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsLessonEntityWhichNotFoundedInDB() {
        LessonResponse expected = emptyLessonResponse;
        LessonResponse actual = lessonMapper.mapEntityToDto(emptyLesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(lessonMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsLessonRequestDto() {
        Lesson expected = lesson;
        Lesson actual = lessonMapper.mapDtoToEntity(lessonRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(lessonMapper.mapDtoToEntity(null)).isNull();
    }

}
