package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.StudentRequestMapper;

import static org.assertj.core.api.Assertions.assertThat;

class StudentRequestMapperImplTest {

    ApplicationContext context;
    StudentRequestMapper studentRequestMapper;
    Student student;
    StudentRequest studentRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        studentRequestMapper = context.getBean(StudentRequestMapperImpl.class);
        student = Student.builder().withId((long)1).build();
        studentRequest = new StudentRequest();
        studentRequest.setId((long)1);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsFormOfStudentRequestDto() {
        Student expected = student;
        Student actual = studentRequestMapper.mapDtoToEntity(studentRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(studentRequestMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsFormOfStudentEntity() {
        StudentRequest expected = studentRequest;
        StudentRequest actual = studentRequestMapper.mapEntityToDto(student);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(studentRequestMapper.mapEntityToDto(null)).isNull();
    }

}