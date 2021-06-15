package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ScienceDegreeRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ProfessorRequestMapper;
import java.util.Arrays;
import java.util.List;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

class ProfessorRequestMapperImplTest {

    ApplicationContext context;
    ProfessorRequestMapper professorRequestMapper;
    Professor professorWithCourses;
    Professor professorWithNoCourses;
    ProfessorRequest professorRequestWithCourses;
    ProfessorRequest professorRequestWithNoCourses;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        List<Course> courses = Arrays.asList(Course.builder().withId((long)1).build(), Course.builder().withId((long)2).build());
        CourseRequest courseRequest1 = new CourseRequest();
        CourseRequest courseRequest2 = new CourseRequest();
        courseRequest1.setId((long)1);
        courseRequest2.setId((long)2);
        List<CourseRequest> coursesRequest = Arrays.asList(courseRequest1, courseRequest2);
        professorRequestMapper = context.getBean(ProfessorRequestMapperImpl.class);
        professorWithCourses = Professor.builder().withId((long)1).withFirstName("Professor 1")
                .withScienceDegree(ScienceDegree.GRADUATE).withCourses(courses).build();
        professorRequestWithCourses = new ProfessorRequest();
        professorRequestWithCourses.setId((long)1);
        professorRequestWithCourses.setFirstName("Professor 1");
        professorRequestWithCourses.setScienceDegreeRequest(ScienceDegreeRequest.GRADUATE);
        professorRequestWithCourses.setCoursesRequest(coursesRequest);
    }



    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsProfessorWithCoursesRequestDto() {
        Professor expected = professorWithCourses;
        Professor actual = professorRequestMapper.mapDtoToEntity(professorRequestWithCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsProfessorWithNoCoursesRequestDto() {
        professorWithNoCourses = Professor.builder().withId((long)1).withFirstName("Professor 1").
                withCourses(emptyList()).withScienceDegree(ScienceDegree.GRADUATE).build();
        professorRequestWithNoCourses = new ProfessorRequest();
        professorRequestWithNoCourses.setId((long)1);
        professorRequestWithNoCourses.setFirstName("Professor 1");
        professorRequestWithNoCourses.setScienceDegreeRequest(ScienceDegreeRequest.GRADUATE);

        Professor expected = professorWithNoCourses;
        Professor actual = professorRequestMapper.mapDtoToEntity(professorRequestWithNoCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(professorRequestMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorWithCoursesEntity() {
        ProfessorRequest expected = professorRequestWithCourses;
        ProfessorRequest actual = professorRequestMapper.mapEntityToDto(professorWithCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorWithNoCoursesEntity() {
        professorRequestWithNoCourses = new ProfessorRequest();
        professorRequestWithNoCourses.setId((long)1);
        professorRequestWithNoCourses.setFirstName("Professor 1");
        professorRequestWithNoCourses.setCoursesRequest(emptyList());
        professorRequestWithNoCourses.setScienceDegreeRequest(ScienceDegreeRequest.GRADUATE);
        professorWithNoCourses = Professor.builder().withId((long)1).withFirstName("Professor 1")
                .withScienceDegree(ScienceDegree.GRADUATE).build();

        ProfessorRequest expected = professorRequestWithNoCourses;
        ProfessorRequest actual = professorRequestMapper.mapEntityToDto(professorWithNoCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(professorRequestMapper.mapEntityToDto(null)).isNull();
    }

}