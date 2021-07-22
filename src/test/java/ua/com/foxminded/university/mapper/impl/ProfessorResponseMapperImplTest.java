package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ProfessorResponseMapper;
import static java.util.Collections.emptyList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProfessorResponseMapperImplTest {

    ApplicationContext context;
    ProfessorResponseMapper professorResponseMapper;
    Professor professorWithCourses;
    Professor professorWithNoCourses;
    ProfessorResponse professorResponseWithCourses;
    ProfessorResponse professorResponseWithNoCourses;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        List<Course> courses = Arrays.asList(Course.builder().withId(1L).build(), Course.builder().withId(2L).build());
        CourseResponse courseResponse1 = new CourseResponse();
        CourseResponse courseResponse2 = new CourseResponse();
        courseResponse1.setId(1L);
        courseResponse2.setId(2L);
        List<CourseResponse> coursesResponse = Arrays.asList(courseResponse1, courseResponse2);
        professorResponseMapper = context.getBean(ProfessorResponseMapperImpl.class);
        professorWithCourses = Professor.builder().withId(1L).withFirstName("Professor 1")
                .withScienceDegree(ScienceDegree.GRADUATE).withCourses(courses).build();
        professorResponseWithCourses = new ProfessorResponse();
        professorResponseWithCourses.setId(1L);
        professorResponseWithCourses.setFirstName("Professor 1");
        professorResponseWithCourses.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);
        professorResponseWithCourses.setCoursesResponse(coursesResponse);
    }



    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsProfessorWithCoursesResponseDto() {
        Professor expected = professorWithCourses;
        Professor actual = professorResponseMapper.mapDtoToEntity(professorResponseWithCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsProfessorWithNoCoursesResponseDto() {
        professorWithNoCourses = Professor.builder().withId(1L).withFirstName("Professor 1")
                .withCourses(emptyList()).withScienceDegree(ScienceDegree.GRADUATE).build();
        professorResponseWithNoCourses = new ProfessorResponse();
        professorResponseWithNoCourses.setId(1L);
        professorResponseWithNoCourses.setFirstName("Professor 1");
        professorResponseWithNoCourses.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);

        Professor expected = professorWithNoCourses;
        Professor actual = professorResponseMapper.mapDtoToEntity(professorResponseWithNoCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(professorResponseMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorWithCoursesEntity() {
        ProfessorResponse expected = professorResponseWithCourses;
        ProfessorResponse actual = professorResponseMapper.mapEntityToDto(professorWithCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorWithNoCoursesEntity() {
        professorResponseWithNoCourses = new ProfessorResponse();
        professorResponseWithNoCourses.setId(1L);
        professorResponseWithNoCourses.setFirstName("Professor 1");
        professorResponseWithNoCourses.setCoursesResponse(emptyList());
        professorResponseWithNoCourses.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);
        professorWithNoCourses = Professor.builder().withId(1L).withFirstName("Professor 1")
                .withScienceDegree(ScienceDegree.GRADUATE).build();

        ProfessorResponse expected = professorResponseWithNoCourses;
        ProfessorResponse actual = professorResponseMapper.mapEntityToDto(professorWithNoCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(professorResponseMapper.mapEntityToDto(null)).isNull();
    }

}