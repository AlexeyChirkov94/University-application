package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Student;
import static org.assertj.core.api.Assertions.assertThat;

class StudentMapperTest {

    ApplicationContext context;
    StudentMapper studentMapper;
    Student student;
    Student emptyStudent;
    StudentResponse studentResponse;
    StudentRequest studentRequest;
    StudentResponse emptyStudentResponse;


    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        studentMapper = context.getBean(StudentMapper.class);

        student = Student.builder().withId(1L).withFirstName("Alex").withLastName("Chirkov")
                .withEmail("gmail").withPassword("1234").build();
        emptyStudent = Student.builder().build();

        studentRequest = new StudentRequest();
        studentRequest.setId(1L);
        studentRequest.setFirstName("Alex");
        studentRequest.setLastName("Chirkov");
        studentRequest.setPassword("1234");
        studentRequest.setEmail("gmail");

        studentResponse = new StudentResponse();
        studentResponse.setId(1L);
        studentResponse.setFirstName("Alex");
        studentResponse.setLastName("Chirkov");
        studentResponse.setPassword("1234");
        studentResponse.setEmail("gmail");

        emptyStudentResponse = new StudentResponse();
        emptyStudentResponse.setId(0L);
        emptyStudentResponse.setFirstName("");
        emptyStudentResponse.setLastName("");
        emptyStudentResponse.setEmail("");
        emptyStudentResponse.setPassword("");
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsStudentEntity() {
        StudentResponse expected = studentResponse;
        StudentResponse actual = studentMapper.mapEntityToDto(student);

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
