package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.mapper.interfaces.CourseResponseMapper;
import static org.assertj.core.api.Assertions.assertThat;

class CourseResponseMapperImplTest {

    ApplicationContext context;
    CourseResponseMapper courseResponseMapper;
    Course course;
    Course emptyCourse;
    CourseResponse courseResponse;
    CourseResponse emptyCourseResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        courseResponseMapper = context.getBean(CourseResponseMapperImpl.class);
        course = Course.builder().withId(1L).withName("Course 1").build();
        emptyCourse = Course.builder().withId(0L).build();
        courseResponse = new CourseResponse();
        courseResponse.setId(1L);
        courseResponse.setName("Course 1");
        emptyCourseResponse = new CourseResponse();
        emptyCourseResponse.setId(0L);
        emptyCourseResponse.setName("");
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsCourseEntity() {
        CourseResponse expected = courseResponse;
        CourseResponse actual = courseResponseMapper.mapEntityToDto(course);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsCourseEntityWhichNotFoundedInDB() {
        CourseResponse expected = emptyCourseResponse;
        CourseResponse actual = courseResponseMapper.mapEntityToDto(emptyCourse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(courseResponseMapper.mapEntityToDto(null)).isNull();
    }

}
