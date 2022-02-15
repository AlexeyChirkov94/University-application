package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertCourses;

class CourseRepositoryImplTest {

    ApplicationContext context;
    CourseRepository courseRepository;
    DepartmentRepository departmentRepository;
    Department departmentForTest;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        courseRepository = context.getBean(CourseRepository.class);
        departmentRepository = context.getBean(DepartmentRepository.class);
        departmentForTest = Department.builder().withId(1L).withName("Department of History").build();
    }

    @Test
    void createAndReadShouldAddNewCourseToDatabaseIfArgumentIsCourse(){
        Course addingCourse = Course.builder()
                .withName("Rome History")
                .withDepartment(departmentForTest)
                .build();

        courseRepository.save(addingCourse);
        Course readingCourse = courseRepository.findById(6L).get();

        assertCourses(readingCourse, addingCourse);
    }

    @Test
    void createAndReadShouldAddListOfNewCoursesToDatabaseIfArgumentIsListOfCourses(){
        List<Course> addingCourseEntities = Arrays.asList(Course.builder()
                .withName("Rome History")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build())
                .build());
        courseRepository.saveAll(addingCourseEntities);
        List<Course> readingCourseEntities = Arrays.asList(courseRepository.findById(6L).get());

        assertCourses(readingCourseEntities, addingCourseEntities);
    }

    @Test
    void updateShouldUpdateDataOfCoursesIfArgumentIsCourses(){
        Course expected = Course.builder()
                .withId(4L)
                .withName("Rome History")
                .withDepartment(departmentRepository.findById(1L).get())
                .build();
        courseRepository.save(expected);
        Course actual = courseRepository.findById(4L).get();

        assertCourses(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfProfessorIfArgumentIsListOfProfessor(){
        List<Course> expected = Arrays.asList(Course.builder()
                .withId(4L)
                .withName("Rome History")
                .withDepartment(departmentRepository.findById(1L).get())
                .build());
        courseRepository.saveAll(expected);
        List<Course> actual = Arrays.asList(courseRepository.findById(4L).get());

        assertCourses(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Course> expected = Optional.empty();
        courseRepository.deleteById(5L);
        Optional<Course> actual = courseRepository.findById(5L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addCourseToProfessorCourseListShouldAddCourseToProfessorCourseListIfArgumentIsIdOfProfessorAndIdOfCourse(){
        List<Course> expectedBeforeAdding = Arrays.asList (courseRepository.findById(1L).get(),
                courseRepository.findById(2L).get());
        List<Course> actualBeforeAdding = courseRepository.findByProfessorId(7);
        assertCourses(actualBeforeAdding, expectedBeforeAdding);

        courseRepository.addCourseToProfessorCourseList(3, 7);

        List<Course> expectedAfterAdding = Arrays.asList (courseRepository.findById(1L).get(),
                courseRepository.findById(2L).get(), courseRepository.findById(3L).get());
        List<Course> actualAfterAdding = courseRepository.findByProfessorId(7);
        assertCourses(actualAfterAdding, expectedAfterAdding);
    }

    @Test
    void changeDepartmentShouldChangeDepartmentIfArgumentIsIdOfCourseAndIDOfDepartment(){
        Course expectedBeforeChanging = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build()).build();
        Course actualBeforeChanging = courseRepository.findById(1L).get();
        assertCourses (actualBeforeChanging, expectedBeforeChanging);

        courseRepository.changeDepartment(1, 2);

        Course expectedAfterChanging = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(2L).withName("Department of Math").build()).build();
        Course actualAfterChanging = courseRepository.findById(1L).get();
        assertCourses(actualAfterChanging, expectedAfterChanging);
    }

    @Test
    void removeCourseToProfessorCourseListShouldRemoveCourseToProfessorCourseListIfArgumentIsIdOfProfessorAndIdOfCourse(){
        List<Course> expectedBeforeRemoving = Arrays.asList (courseRepository.findById(1L).get(),
                courseRepository.findById(2L).get());
        List<Course> actualBeforeRemoving = courseRepository.findByProfessorId(7);
        assertCourses(actualBeforeRemoving, expectedBeforeRemoving);

        courseRepository.removeCourseFromProfessorCourseList(2, 7);

        List<Course> expectedAfterRemoving = Arrays.asList (courseRepository.findById(1L).get());
        List<Course> actualAfterRemoving = courseRepository.findByProfessorId(7);
        assertCourses(actualAfterRemoving, expectedAfterRemoving);
    }

    @Test
    void removeDepartmentShouldChangeDepartmentIfArgumentIsIdOfCourseAndIDOfDepartment(){
        Course expectedBeforeRemoving = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build()).build();
        Course actualBeforeRemoving = courseRepository.findById(1L).get();
        assertCourses (expectedBeforeRemoving, actualBeforeRemoving);

        courseRepository.removeDepartmentFromCourse(1);

        Course expectedAfterRemoving = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(0L).build()).build();
        Course actualAfterRemoving = courseRepository.findById(1L).get();

        assertThat(actualAfterRemoving.getName()).isEqualTo(expectedAfterRemoving.getName());
        assertThat(actualAfterRemoving.getDepartment()).isNull();
    }

    @Test
    void findByNameShouldReturnOptionalOfDepartmentEntityIfArgumentIsName(){
        Course expected = Course.builder()
                .withName("Russia History")
                .withDepartment(departmentRepository.findById(1L).get())
                .build();
        Course actual = courseRepository.findAllByName("Russia History").get(0);

        assertCourses(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<Course> expected = Collections.emptyList();
        List<Course> actual = courseRepository.findAllByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByDepartmentIdShouldReturnOptionalOfDepartmentEntityIfDepartmentId(){
        List<Course> expected = Arrays.asList(courseRepository.findById(1L).get(), courseRepository.findById(2L).get(),
                courseRepository.findById(3L).get(), courseRepository.findById(4L).get());
        List<Course> actual = courseRepository.findByDepartmentId(1L);

        assertCourses(actual, expected);
    }

}
