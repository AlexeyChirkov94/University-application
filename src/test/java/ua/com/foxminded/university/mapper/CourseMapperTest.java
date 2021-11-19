package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.entity.Course;
import static org.assertj.core.api.Assertions.assertThat;

class CourseMapperTest {

    ApplicationContext context;
    CourseMapper courseMapper;
    Course course;
    Course emptyCourse;
    CourseResponse courseResponse;
    CourseRequest courseRequest;
    CourseResponse emptyCourseResponse;


    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        courseMapper = context.getBean(CourseMapper.class);

        course = Course.builder().withId(1L).withName("Course 1").build();
        emptyCourse = Course.builder().build();

        courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setName("Course 1");

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
        CourseResponse actual = courseMapper.mapEntityToDto(course);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsCourseEntityWhichNotFoundedInDB() {
        CourseResponse expected = emptyCourseResponse;
        CourseResponse actual = courseMapper.mapEntityToDto(emptyCourse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(courseMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsCourseRequestDto() {
        Course expected = course;
        Course actual = courseMapper.mapDtoToEntity(courseRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(courseMapper.mapDtoToEntity(null)).isNull();
    }

}
