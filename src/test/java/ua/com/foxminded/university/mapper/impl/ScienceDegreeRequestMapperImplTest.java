package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.ScienceDegreeRequest;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ScienceDegreeRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;

class ScienceDegreeRequestMapperImplTest {

    ApplicationContext context;
    ScienceDegreeRequestMapper scienceDegreeRequestMapper;
    ScienceDegree scienceDegree;
    ScienceDegreeRequest scienceDegreeRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        scienceDegreeRequestMapper = context.getBean(ScienceDegreeRequestMapperImpl.class);
        scienceDegree = ScienceDegree.GRADUATE;
        scienceDegreeRequest = ScienceDegreeRequest.GRADUATE;
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDto() {
        ScienceDegree expected = scienceDegree;
        ScienceDegree actual = scienceDegreeRequestMapper.mapDtoToEntity(scienceDegreeRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(scienceDegreeRequestMapper.mapDtoToEntity(null)).isNull();
    }

}
