package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.ScienceDegree;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessorMapperTest {

    ApplicationContext context;
    ProfessorMapper professorMapper;
    Professor professor;
    Professor emptyProfessor;
    ProfessorResponse professorResponse;
    ProfessorRequest professorRequest;
    ProfessorResponse emptyProfessorResponse;


    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        professorMapper = context.getBean(ProfessorMapper.class);

        professor = Professor.builder().withId(1L).withFirstName("Alex").withLastName("Chirkov")
                .withEmail("gmail").withPassword("1234").withScienceDegree(ScienceDegree.GRADUATE).build();
        emptyProfessor = Professor.builder().build();

        professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setFirstName("Alex");
        professorRequest.setLastName("Chirkov");
        professorRequest.setPassword("1234");
        professorRequest.setEmail("gmail");

        professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        professorResponse.setFirstName("Alex");
        professorResponse.setLastName("Chirkov");
        professorResponse.setPassword("1234");
        professorResponse.setEmail("gmail");
        professorResponse.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);

        emptyProfessorResponse = new ProfessorResponse();
        emptyProfessorResponse.setId(0L);
        emptyProfessorResponse.setFirstName("");
        emptyProfessorResponse.setLastName("");
        emptyProfessorResponse.setEmail("");
        emptyProfessorResponse.setPassword("");
        emptyProfessorResponse.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorEntity() {
        ProfessorResponse expected = professorResponse;
        ProfessorResponse actual = professorMapper.mapEntityToDto(professor);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorEntityWhichNotFoundedInDB() {
        ProfessorResponse expected = emptyProfessorResponse;
        ProfessorResponse actual = professorMapper.mapEntityToDto(emptyProfessor);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(professorMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsProfessorRequestDto() {
        Professor expected = professor;
        Professor actual = professorMapper.mapDtoToEntity(professorRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(professorMapper.mapDtoToEntity(null)).isNull();
    }

}
