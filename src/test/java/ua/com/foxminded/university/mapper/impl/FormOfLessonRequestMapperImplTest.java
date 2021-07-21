package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;

class FormOfLessonRequestMapperImplTest {

    ApplicationContext context;
    FormOfLessonRequestMapper formOfLessonRequestMapper;
    FormOfLesson formOfLesson;
    FormOfLessonRequest formOfLessonRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        formOfLessonRequestMapper = context.getBean(FormOfLessonRequestMapperImpl.class);
        formOfLesson = FormOfLesson.builder().withId((long)1).withName("FormOfLesson 1").build();
        formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId((long)1);
        formOfLessonRequest.setName("FormOfLesson 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDto() {
        FormOfLesson expected = formOfLesson;
        FormOfLesson actual = formOfLessonRequestMapper.mapDtoToEntity(formOfLessonRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(formOfLessonRequestMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        FormOfLessonRequest expected = formOfLessonRequest;
        FormOfLessonRequest actual = formOfLessonRequestMapper.mapEntityToDto(formOfLesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(formOfLessonRequestMapper.mapEntityToDto(null)).isNull();
    }

}