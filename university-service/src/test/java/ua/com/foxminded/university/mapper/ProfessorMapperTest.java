package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.ScienceDegree;
import static org.assertj.core.api.Assertions.assertThat;

class ProfessorMapperTest {

    ApplicationContext context;
    ProfessorMapper professorMapper;
    Professor professor;
    Professor professorWithEntities;
    Professor emptyProfessor;
    ProfessorResponse professorResponse;
    ProfessorRequest professorRequest;
    ProfessorResponse emptyProfessorResponse;
    DepartmentResponse departmentResponse;
    DepartmentResponse emptyDepartmentResponse;

    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        professorMapper = context.getBean(ProfessorMapper.class);

        professor = Professor.builder().withId(1L).withFirstName("Alex").withLastName("Chirkov")
                .withEmail("gmail").withPassword("1234").withScienceDegree(ScienceDegree.GRADUATE).build();
        emptyProfessor = Professor.builder().withScienceDegree(null).build();
        professorWithEntities = Professor.builder().withId(1L).withFirstName("Alex").withLastName("Chirkov")
                .withEmail("gmail").withPassword("1234").withScienceDegree(ScienceDegree.GRADUATE)
                .withDepartment(Department.builder().withId(1L).withName("Dep 1").build()).build();

        professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setFirstName("Alex");
        professorRequest.setLastName("Chirkov");
        professorRequest.setPassword("1234");
        professorRequest.setEmail("gmail");

        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("Dep 1");

        emptyDepartmentResponse = new DepartmentResponse();
        emptyDepartmentResponse.setId(0L);
        emptyDepartmentResponse.setName("");

        professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        professorResponse.setFirstName("Alex");
        professorResponse.setLastName("Chirkov");
        professorResponse.setPassword("1234");
        professorResponse.setEmail("gmail");
        professorResponse.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);
        professorResponse.setDepartmentResponse(departmentResponse);

        emptyProfessorResponse = new ProfessorResponse();
        emptyProfessorResponse.setId(0L);
        emptyProfessorResponse.setFirstName("");
        emptyProfessorResponse.setLastName("");
        emptyProfessorResponse.setEmail("");
        emptyProfessorResponse.setPassword("");
        emptyProfessorResponse.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);
        emptyProfessorResponse.setDepartmentResponse(emptyDepartmentResponse);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorEntity() {
        ProfessorResponse expected = professorResponse;
        ProfessorResponse actual = professorMapper.mapEntityToDto(professorWithEntities);

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
