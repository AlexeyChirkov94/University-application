package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertCourses;
class CourseDaoImplTest {

    ApplicationContext context;
    CourseDao courseDao;
    DepartmentDao departmentDao;
    Department departmentForTest;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        courseDao = context.getBean(CourseDaoImpl.class);
        departmentDao = context.getBean(DepartmentDaoImpl.class);
        departmentForTest = Department.builder().withId(1L).withName("Department of History").build();
    }

    @Test
    void createAndReadShouldAddNewCourseToDatabaseIfArgumentIsCourse(){
        Course addingCourse = Course.builder()
                .withName("Rome History")
                .withDepartment(departmentForTest)
                .build();

        courseDao.save(addingCourse);
        Course readingCourse = courseDao.findById(5L).get();

        assertCourses(readingCourse, addingCourse);
    }

    @Test
    void createAndReadShouldAddListOfNewCoursesToDatabaseIfArgumentIsListOfCourses(){
        List<Course> addingCourseEntities = Arrays.asList(Course.builder()
                .withName("Rome History")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build())
                .build());
        courseDao.saveAll(addingCourseEntities);
        List<Course> readingCourseEntities = Arrays.asList(courseDao.findById(5L).get());

        assertCourses(readingCourseEntities, addingCourseEntities);
    }

    @Test
    void updateShouldUpdateDataOfCoursesIfArgumentIsCourses(){
        Course expected = Course.builder()
                .withId(4L)
                .withName("Rome History")
                .withDepartment(departmentDao.findById(1L).get())
                .build();
        courseDao.update(expected);
        Course actual = courseDao.findById(4L).get();

        assertCourses(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfProfessorIfArgumentIsListOfProfessor(){
        List<Course> expected = Arrays.asList(Course.builder()
                .withId(4L)
                .withName("Rome History")
                .withDepartment(departmentDao.findById(1L).get())
                .build());
        courseDao.updateAll(expected);
        List<Course> actual = Arrays.asList(courseDao.findById(4L).get());

        assertCourses(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Course> expected = Optional.empty();
        courseDao.deleteById(10L);
        Optional<Course> actual = courseDao.findById(10L);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addCourseToProfessorCourseListShouldAddCourseToProfessorCourseListIfArgumentIsIdOfProfessorAndIdOfCourse(){
        List<Course> expectedBeforeAdding = Arrays.asList (courseDao.findById(1L).get(),
                courseDao.findById(2L).get());
        List<Course> actualBeforeAdding = courseDao.findByProfessorId(7);
        assertCourses(actualBeforeAdding, expectedBeforeAdding);

        courseDao.addCourseToProfessorCourseList(3, 7);

        List<Course> expectedAfterAdding = Arrays.asList (courseDao.findById(1L).get(),
                courseDao.findById(2L).get(), courseDao.findById(3L).get());
        List<Course> actualAfterAdding = courseDao.findByProfessorId(7);
        assertCourses(actualAfterAdding, expectedAfterAdding);
    }

    @Test
    void changeDepartmentShouldChangeDepartmentIfArgumentIsIdOfCourseAndIDOfDepartment(){
        Course expectedBeforeChanging = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build()).build();
        Course actualBeforeChanging = courseDao.findById(1L).get();
        assertCourses (actualBeforeChanging, expectedBeforeChanging);

        courseDao.changeDepartment(1, 2);

        Course expectedAfterChanging = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(2L).withName("Department of Math").build()).build();
        Course actualAfterChanging = courseDao.findById(1L).get();
        assertCourses (actualBeforeChanging, expectedBeforeChanging);
        assertCourses(expectedAfterChanging, actualAfterChanging);
    }

    @Test
    void removeCourseToProfessorCourseListShouldRemoveCourseToProfessorCourseListIfArgumentIsIdOfProfessorAndIdOfCourse(){
        List<Course> expectedBeforeRemoving = Arrays.asList (courseDao.findById(1L).get(),
                courseDao.findById(2L).get());
        List<Course> actualBeforeRemoving = courseDao.findByProfessorId(7);
        assertCourses(actualBeforeRemoving, expectedBeforeRemoving);

        courseDao.removeCourseFromProfessorCourseList(2, 7);

        List<Course> expectedAfterRemoving = Arrays.asList (courseDao.findById(1L).get());
        List<Course> actualAfterRemoving = courseDao.findByProfessorId(7);
        assertCourses(actualAfterRemoving, expectedAfterRemoving);
    }

    @Test
    void removeDepartmentShouldChangeDepartmentIfArgumentIsIdOfCourseAndIDOfDepartment(){
        Course expectedBeforeRemoving = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build()).build();
        Course actualBeforeRemoving = courseDao.findById(1L).get();
        assertCourses (expectedBeforeRemoving, actualBeforeRemoving);

        courseDao.removeDepartmentFromCourse(1);

        Course expectedAfterRemoving = Course.builder().withName("Russia History")
                .withDepartment(Department.builder().withId(0L).build()).build();
        Course actualAfterRemoving = courseDao.findById(1L).get();

        assertThat(actualAfterRemoving.getName()).isEqualTo(expectedAfterRemoving.getName());
        assertThat(actualAfterRemoving.getDepartment()).isNull();
    }

    @Test
    void findByNameShouldReturnOptionalOfDepartmentEntityIfArgumentIsName(){
        Course expected = Course.builder()
                .withName("Russia History")
                .withDepartment(departmentDao.findById(1L).get())
                .build();
        Course actual = courseDao.findByName("Russia History").get(0);

        assertCourses(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<Course> expected = Collections.emptyList();
        List<Course> actual = courseDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByDepartmentIdShouldReturnOptionalOfDepartmentEntityIfDepartmentId(){
        List<Course> expected = Arrays.asList(courseDao.findById(1L).get(), courseDao.findById(2L).get(),
                courseDao.findById(3L).get(), courseDao.findById(4L).get());
        List<Course> actual = courseDao.findByDepartmentId(1L);

        assertCourses(actual, expected);
    }

}
