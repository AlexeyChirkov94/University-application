package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;


class CourseRequestMapperImplTest {

    ApplicationContext context;
    CourseRequestMapper courseRequestMapper;
    Course course;
    CourseRequest courseRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        courseRequestMapper = context.getBean(CourseRequestMapperImpl.class);
        course = Course.builder().withId((long)1).withName("Course 1").build();
        courseRequest = new CourseRequest();
        courseRequest.setId((long)1);
        courseRequest.setName("Course 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsCourseRequestDto() {
        Course expected = course;
        Course actual = courseRequestMapper.mapDtoToEntity(courseRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(courseRequestMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsCourseEntity() {
        CourseRequest expected = courseRequest;
        CourseRequest actual = courseRequestMapper.mapEntityToDto(course);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(courseRequestMapper.mapEntityToDto(null)).isNull();
    }

}