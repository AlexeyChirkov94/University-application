package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.StudentResponseMapper;

import static org.assertj.core.api.Assertions.assertThat;

class StudentResponseMapperImplTest {

    ApplicationContext context;
    StudentResponseMapper studentResponseMapper;
    Student student;
    StudentResponse studentResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        studentResponseMapper = context.getBean(StudentResponseMapperImpl.class);
        student = Student.builder().withId(1L).build();
        studentResponse = new StudentResponse();
        studentResponse.setId(1L);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfStudentResponseDto() {
        Student expected = student;
        Student actual = studentResponseMapper.mapDtoToEntity(studentResponse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(studentResponseMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfStudentEntity() {
        StudentResponse expected = studentResponse;
        StudentResponse actual = studentResponseMapper.mapEntityToDto(student);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(studentResponseMapper.mapEntityToDto(null)).isNull();
    }

}