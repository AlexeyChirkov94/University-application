package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.ScienceDegreeRequest;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.ScienceDegree;
import static org.assertj.core.api.Assertions.assertThat;

public class ScienceDegreeMapperTest {

    ApplicationContext context;
    ScienceDegreeMapper scienceDegreeMapper;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        scienceDegreeMapper = context.getBean(ScienceDegreeMapper.class);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDtoIfItGraduate() {
        ScienceDegree expected = ScienceDegree.GRADUATE;
        ScienceDegreeRequest scienceDegreeRequest = ScienceDegreeRequest.GRADUATE;

        ScienceDegree actual = scienceDegreeMapper.mapDtoToEntity(scienceDegreeRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntityIfItGraduate() {
        ScienceDegree scienceDegree = ScienceDegree.GRADUATE;
        ScienceDegreeResponse expected = ScienceDegreeResponse.GRADUATE;
        ScienceDegreeResponse actual = scienceDegreeMapper.mapEntityToDto(scienceDegree);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDtoIfItMaster() {
        ScienceDegree expected = ScienceDegree.MASTER;
        ScienceDegreeRequest scienceDegreeRequest = ScienceDegreeRequest.MASTER;

        ScienceDegree actual = scienceDegreeMapper.mapDtoToEntity(scienceDegreeRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntityIfItMaster() {
        ScienceDegree scienceDegree = ScienceDegree.MASTER;
        ScienceDegreeResponse expected = ScienceDegreeResponse.MASTER;
        ScienceDegreeResponse actual = scienceDegreeMapper.mapEntityToDto(scienceDegree);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDtoIfItPHDCandidate() {
        ScienceDegree expected = ScienceDegree.PH_D_CANDIDATE;
        ScienceDegreeRequest scienceDegreeRequest = ScienceDegreeRequest.PH_D_CANDIDATE;

        ScienceDegree actual = scienceDegreeMapper.mapDtoToEntity(scienceDegreeRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntityIfItPHDCandidate() {
        ScienceDegree scienceDegree = ScienceDegree.PH_D_CANDIDATE;
        ScienceDegreeResponse expected = ScienceDegreeResponse.PH_D_CANDIDATE;
        ScienceDegreeResponse actual = scienceDegreeMapper.mapEntityToDto(scienceDegree);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfLessonRequestDtoIfItPHD() {
        ScienceDegree expected = ScienceDegree.PH_D;
        ScienceDegreeRequest scienceDegreeRequest = ScienceDegreeRequest.PH_D;

        ScienceDegree actual = scienceDegreeMapper.mapDtoToEntity(scienceDegreeRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfLessonEntityIfItPHD() {
        ScienceDegree scienceDegree = ScienceDegree.PH_D;
        ScienceDegreeResponse expected = ScienceDegreeResponse.PH_D;
        ScienceDegreeResponse actual = scienceDegreeMapper.mapEntityToDto(scienceDegree);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(scienceDegreeMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(scienceDegreeMapper.mapEntityToDto(null)).isNull();
    }

}
