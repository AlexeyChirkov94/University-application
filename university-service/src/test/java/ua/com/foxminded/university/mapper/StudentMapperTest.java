package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import static org.assertj.core.api.Assertions.assertThat;

class StudentMapperTest {

    ApplicationContext context;
    StudentMapper studentMapper;
    Student student;
    Student emptyStudent;
    Student studentWithEntities;
    StudentResponse studentResponse;
    StudentRequest studentRequest;
    StudentResponse emptyStudentResponse;

    GroupResponse groupResponse;
    GroupResponse emptyGroupResponse;

    DepartmentResponse departmentResponse;
    FormOfEducationResponse formOfEducationResponse;

    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        studentMapper = context.getBean(StudentMapper.class);

        student = Student.builder().withId(1L).withFirstName("Alex").withLastName("Chirkov")
                .withEmail("gmail").withPassword("1234").build();
        emptyStudent = Student.builder().build();
        studentWithEntities = Student.builder().withId(1L).withFirstName("Alex").withLastName("Chirkov")
                .withEmail("gmail").withPassword("1234").withGroup(Group.builder().withId(1L).withName("Group 1").build()).build();

        studentRequest = new StudentRequest();
        studentRequest.setId(1L);
        studentRequest.setFirstName("Alex");
        studentRequest.setLastName("Chirkov");
        studentRequest.setPassword("1234");
        studentRequest.setEmail("gmail");

        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(0L);
        departmentResponse.setName("");
        formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId(0L);
        formOfEducationResponse.setName("");

        groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");
        groupResponse.setDepartmentResponse(departmentResponse);
        groupResponse.setFormOfEducationResponse(formOfEducationResponse);
        emptyGroupResponse = new GroupResponse();
        emptyGroupResponse.setId(0L);
        emptyGroupResponse.setName("");

        studentResponse = new StudentResponse();
        studentResponse.setId(1L);
        studentResponse.setFirstName("Alex");
        studentResponse.setLastName("Chirkov");
        studentResponse.setPassword("1234");
        studentResponse.setEmail("gmail");
        studentResponse.setGroupResponse(groupResponse);

        emptyStudentResponse = new StudentResponse();
        emptyStudentResponse.setId(0L);
        emptyStudentResponse.setFirstName("");
        emptyStudentResponse.setLastName("");
        emptyStudentResponse.setEmail("");
        emptyStudentResponse.setPassword("");
        emptyStudentResponse.setGroupResponse(emptyGroupResponse);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsStudentEntity() {
        StudentResponse expected = studentResponse;
        StudentResponse actual = studentMapper.mapEntityToDto(studentWithEntities);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsStudentEntityWhichNotFoundedInDB() {
        StudentResponse expected = emptyStudentResponse;
        StudentResponse actual = studentMapper.mapEntityToDto(emptyStudent);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(studentMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsStudentRequestDto() {
        Student expected = student;
        Student actual = studentMapper.mapDtoToEntity(studentRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(studentMapper.mapDtoToEntity(null)).isNull();
    }

}
