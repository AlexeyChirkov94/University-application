package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;

class FormOfEducationRequestMapperImplTest {

    ApplicationContext context;
    FormOfEducationRequestMapper formOfEducationRequestMapper;
    FormOfEducation formOfEducation;
    FormOfEducationRequest formOfEducationRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        formOfEducationRequestMapper = context.getBean(FormOfEducationRequestMapperImpl.class);
        formOfEducation = FormOfEducation.builder().withId(1L).withName("FormOfEducation 1").build();
        formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(1L);
        formOfEducationRequest.setName("FormOfEducation 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfEducationRequestDto() {
        FormOfEducation expected = formOfEducation;
        FormOfEducation actual = formOfEducationRequestMapper.mapDtoToEntity(formOfEducationRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(formOfEducationRequestMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfEducationEntity() {
        FormOfEducationRequest expected = formOfEducationRequest;
        FormOfEducationRequest actual = formOfEducationRequestMapper.mapEntityToDto(formOfEducation);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(formOfEducationRequestMapper.mapEntityToDto(null)).isNull();
    }

}