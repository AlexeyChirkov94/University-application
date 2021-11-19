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
    Professor emptyProfessorWithNoCourses;
    ProfessorResponse professorResponseWithCourses;
    ProfessorResponse professorResponseWithNoCourses;
    ProfessorResponse emptyProfessorResponseWithNoCourses;

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

        professorWithNoCourses = Professor.builder().withId(1L).withFirstName("Professor 1")
                .withScienceDegree(ScienceDegree.GRADUATE).build();
        professorResponseWithNoCourses = new ProfessorResponse();
        professorResponseWithNoCourses.setId(1L);
        professorResponseWithNoCourses.setFirstName("Professor 1");
        professorResponseWithNoCourses.setCoursesResponse(emptyList());
        professorResponseWithNoCourses.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);

        emptyProfessorWithNoCourses = Professor.builder().withId(0L).build();
        emptyProfessorResponseWithNoCourses = new ProfessorResponse();
        emptyProfessorResponseWithNoCourses.setId(0L);
        emptyProfessorResponseWithNoCourses.setFirstName("");
        emptyProfessorResponseWithNoCourses.setLastName("");
        emptyProfessorResponseWithNoCourses.setEmail("");
        emptyProfessorResponseWithNoCourses.setPassword("");
        emptyProfessorResponseWithNoCourses.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);
        emptyProfessorResponseWithNoCourses.setCoursesResponse(emptyList());
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorWithCoursesEntity() {
        ProfessorResponse expected = professorResponseWithCourses;
        ProfessorResponse actual = professorResponseMapper.mapEntityToDto(professorWithCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorWithNoCoursesEntity() {
        ProfessorResponse expected = professorResponseWithNoCourses;
        ProfessorResponse actual = professorResponseMapper.mapEntityToDto(professorWithNoCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsProfessorWithNoCoursesEntityWhichNotFoundedInDB() {
        ProfessorResponse expected = emptyProfessorResponseWithNoCourses;
        ProfessorResponse actual = professorResponseMapper.mapEntityToDto(emptyProfessorWithNoCourses);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(professorResponseMapper.mapEntityToDto(null)).isNull();
    }

}
