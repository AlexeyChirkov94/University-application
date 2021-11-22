package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.entity.FormOfLesson;
import static org.assertj.core.api.Assertions.assertThat;

class FormOfLessonMapperTest {

    ApplicationContext context;
    FormOfLessonMapper formOfLessonMapper;
    FormOfLesson formOfLesson;
    FormOfLesson emptyFormOfLesson;
    FormOfLessonRequest formOfLessonRequest;
    FormOfLessonResponse formOfLessonResponse;
    FormOfLessonResponse emptyFormOfLessonResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        formOfLessonMapper = context.getBean(FormOfLessonMapper.class);

        formOfLesson = FormOfLesson.builder().withId(1L).withName("Dep 1").withDuration(100).build();
        emptyFormOfLesson = FormOfLesson.builder().build();

        formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId(1L);
        formOfLessonRequest.setName("Dep 1");
        formOfLessonRequest.setDuration(100);

        formOfLessonResponse = new FormOfLessonResponse();
        formOfLessonResponse.setId(1L);
        formOfLessonResponse.setName("Dep 1");
        formOfLessonResponse.setDuration(100);

        emptyFormOfLessonResponse = new FormOfLessonResponse();
        emptyFormOfLessonResponse.setId(0L);
        emptyFormOfLessonResponse.setName("");
        emptyFormOfLessonResponse.setDuration(0);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        FormOfLessonResponse expected = formOfLessonResponse;
        FormOfLessonResponse actual = formOfLessonMapper.mapEntityToDto(formOfLesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntityWhichNotFoundedInDB() {
        FormOfLessonResponse expected = emptyFormOfLessonResponse;
        FormOfLessonResponse actual = formOfLessonMapper.mapEntityToDto(emptyFormOfLesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(formOfLessonMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDto() {
        FormOfLesson expected = formOfLesson;
        FormOfLesson actual = formOfLessonMapper.mapDtoToEntity(formOfLessonRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(formOfLessonMapper.mapDtoToEntity(null)).isNull();
    }

}
