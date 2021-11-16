package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationResponseMapper;
import static org.assertj.core.api.Assertions.assertThat;

class FormOfEducationResponseMapperImplTest {

    ApplicationContext context;
    FormOfEducationResponseMapper formOfEducationResponseMapper;
    FormOfEducation formOfEducation;
    FormOfEducation emptyFormOfEducation;
    FormOfEducationResponse formOfEducationResponse;
    FormOfEducationResponse emptyFormOfEducationResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        formOfEducationResponseMapper = context.getBean(FormOfEducationResponseMapperImpl.class);
        formOfEducation = FormOfEducation.builder().withId(1L).withName("FormOfEducation 1").build();
        emptyFormOfEducation = FormOfEducation.builder().withId(0L).build();
        formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId(1L);
        formOfEducationResponse.setName("FormOfEducation 1");
        emptyFormOfEducationResponse = new FormOfEducationResponse();
        emptyFormOfEducationResponse.setId(0L);
        emptyFormOfEducationResponse.setName("");
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfEducationEntity() {
        FormOfEducationResponse expected = formOfEducationResponse;
        FormOfEducationResponse actual = formOfEducationResponseMapper.mapEntityToDto(formOfEducation);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfEducationEntityWhichNotFoundedInDB() {
        FormOfEducationResponse expected = emptyFormOfEducationResponse;
        FormOfEducationResponse actual = formOfEducationResponseMapper.mapEntityToDto(emptyFormOfEducation);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(formOfEducationResponseMapper.mapEntityToDto(null)).isNull();
    }

}
