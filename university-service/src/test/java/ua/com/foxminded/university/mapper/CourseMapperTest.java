package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;

import static org.assertj.core.api.Assertions.assertThat;

class CourseMapperTest {

    ApplicationContext context;
    CourseMapper courseMapper;
    Course course;
    Course courseWithEntities;
    Course emptyCourse;
    CourseResponse courseResponse;
    CourseRequest courseRequest;
    CourseResponse emptyCourseResponse;
    DepartmentResponse departmentResponse;
    DepartmentResponse emptyDepartmentResponse;


    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        courseMapper = context.getBean(CourseMapper.class);

        course = Course.builder().withId(1L).withName("Course 1").build();
        emptyCourse = Course.builder().build();
        courseWithEntities = Course.builder().withId(1L).withName("Course 1")
                .withDepartment(Department.builder().withId(1L).withName("Dep 1").build()).build();

        courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setName("Course 1");

        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("Dep 1");

        emptyDepartmentResponse = new DepartmentResponse();
        emptyDepartmentResponse.setId(0L);
        emptyDepartmentResponse.setName("");

        courseResponse = new CourseResponse();
        courseResponse.setId(1L);
        courseResponse.setName("Course 1");
        courseResponse.setDepartmentResponse(departmentResponse);

        emptyCourseResponse = new CourseResponse();
        emptyCourseResponse.setId(0L);
        emptyCourseResponse.setName("");
        emptyCourseResponse.setDepartmentResponse(emptyDepartmentResponse);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsCourseEntity() {
        CourseResponse expected = courseResponse;
        CourseResponse actual = courseMapper.mapEntityToDto(courseWithEntities);

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
