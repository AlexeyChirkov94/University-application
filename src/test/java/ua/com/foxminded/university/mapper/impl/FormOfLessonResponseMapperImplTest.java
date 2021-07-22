package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonResponseMapper;
import static org.assertj.core.api.Assertions.assertThat;

class FormOfLessonResponseMapperImplTest {

    ApplicationContext context;
    FormOfLessonResponseMapper formOfLessonResponseMapper;
    FormOfLesson formOfLesson;
    FormOfLessonResponse formOfLessonResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        formOfLessonResponseMapper = context.getBean(FormOfLessonResponseMapperImpl.class);
        formOfLesson = FormOfLesson.builder().withId(1L).withName("FormOfLesson 1").build();
        formOfLessonResponse = new FormOfLessonResponse();
        formOfLessonResponse.setId(1L);
        formOfLessonResponse.setName("FormOfLesson 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonResponseDto() {
        FormOfLesson expected = formOfLesson;
        FormOfLesson actual = formOfLessonResponseMapper.mapDtoToEntity(formOfLessonResponse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(formOfLessonResponseMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        FormOfLessonResponse expected = formOfLessonResponse;
        FormOfLessonResponse actual = formOfLessonResponseMapper.mapEntityToDto(formOfLesson);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(formOfLessonResponseMapper.mapEntityToDto(null)).isNull();
    }

}