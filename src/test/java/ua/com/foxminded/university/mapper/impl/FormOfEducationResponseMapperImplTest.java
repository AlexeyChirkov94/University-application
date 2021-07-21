package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationResponseMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FormOfEducationResponseMapperImplTest {

    ApplicationContext context;
    FormOfEducationResponseMapper formOfEducationResponseMapper;
    FormOfEducation formOfEducation;
    FormOfEducationResponse formOfEducationResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        formOfEducationResponseMapper = context.getBean(FormOfEducationResponseMapperImpl.class);
        formOfEducation = FormOfEducation.builder().withId((long)1).withName("FormOfEducation 1").build();
        formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId((long)1);
        formOfEducationResponse.setName("FormOfEducation 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfEducationResponseDto() {
        FormOfEducation expected = formOfEducation;
        FormOfEducation actual = formOfEducationResponseMapper.mapDtoToEntity(formOfEducationResponse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(formOfEducationResponseMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfEducationEntity() {
        FormOfEducationResponse expected = formOfEducationResponse;
        FormOfEducationResponse actual = formOfEducationResponseMapper.mapEntityToDto(formOfEducation);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(formOfEducationResponseMapper.mapEntityToDto(null)).isNull();
    }

}