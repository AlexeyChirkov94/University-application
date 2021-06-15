package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ScienceDegreeResponseMapper;
import static org.assertj.core.api.Assertions.assertThat;


class ScienceDegreeResponseMapperImplTest {

    ApplicationContext context;
    ScienceDegreeResponseMapper scienceDegreeResponseMapper;
    ScienceDegree scienceDegree;
    ScienceDegreeResponse scienceDegreeResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        scienceDegreeResponseMapper = context.getBean(ScienceDegreeResponseMapperImpl.class);
        scienceDegree = ScienceDegree.GRADUATE;
        scienceDegreeResponse = ScienceDegreeResponse.GRADUATE;
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonResponseDto() {
        ScienceDegree expected = scienceDegree;
        ScienceDegree actual = scienceDegreeResponseMapper.mapDtoToEntity(scienceDegreeResponse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(scienceDegreeResponseMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntity() {
        ScienceDegreeResponse expected = scienceDegreeResponse;
        ScienceDegreeResponse actual = scienceDegreeResponseMapper.mapEntityToDto(scienceDegree);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(scienceDegreeResponseMapper.mapEntityToDto(null)).isNull();
    }

}