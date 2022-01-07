package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.entity.FormOfEducation;
import static org.assertj.core.api.Assertions.assertThat;

class FormOfEducationMapperTest {

    ApplicationContext context;
    FormOfEducationMapper formOfEducationMapper;
    FormOfEducation formOfEducation;
    FormOfEducation emptyFormOfEducation;
    FormOfEducationRequest formOfEducationRequest;
    FormOfEducationResponse formOfEducationResponse;
    FormOfEducationResponse emptyFormOfEducationResponse;

    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        formOfEducationMapper = context.getBean(FormOfEducationMapper.class);

        formOfEducation = FormOfEducation.builder().withId(1L).withName("Dep 1").build();
        emptyFormOfEducation = FormOfEducation.builder().build();

        formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(1L);
        formOfEducationRequest.setName("Dep 1");

        formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId(1L);
        formOfEducationResponse.setName("Dep 1");

        emptyFormOfEducationResponse = new FormOfEducationResponse();
        emptyFormOfEducationResponse.setId(0L);
        emptyFormOfEducationResponse.setName("");
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfEducationEntity() {
        FormOfEducationResponse expected = formOfEducationResponse;
        FormOfEducationResponse actual = formOfEducationMapper.mapEntityToDto(formOfEducation);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfEducationEntityWhichNotFoundedInDB() {
        FormOfEducationResponse expected = emptyFormOfEducationResponse;
        FormOfEducationResponse actual = formOfEducationMapper.mapEntityToDto(emptyFormOfEducation);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(formOfEducationMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfEducationRequestDto() {
        FormOfEducation expected = formOfEducation;
        FormOfEducation actual = formOfEducationMapper.mapDtoToEntity(formOfEducationRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(formOfEducationMapper.mapDtoToEntity(null)).isNull();
    }

}
