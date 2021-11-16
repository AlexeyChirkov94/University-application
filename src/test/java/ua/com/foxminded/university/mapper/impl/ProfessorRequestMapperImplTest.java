package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ProfessorRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;

class ProfessorRequestMapperImplTest {

    ApplicationContext context;
    ProfessorRequestMapper professorRequestMapper;
    Professor professor;
    ProfessorRequest professorRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        professorRequestMapper = context.getBean(ProfessorRequestMapperImpl.class);
        professor = Professor.builder().withId(1L).withFirstName("Professor 1")
                .withScienceDegree(ScienceDegree.GRADUATE).build();
        professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setFirstName("Professor 1");
    }



    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsProfessorRequestDto() {
        Professor expected = professor;
        Professor actual = professorRequestMapper.mapDtoToEntity(professorRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(professorRequestMapper.mapDtoToEntity(null)).isNull();
    }

}
