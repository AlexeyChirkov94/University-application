package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertCourses;

public class CourseDaoImplTest {

    ApplicationContext context;
    CourseDao courseDao;
    DepartmentDao departmentDao;
    Department departmentForTest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        courseDao = context.getBean(CourseDaoImpl.class);
        departmentDao = context.getBean(DepartmentDaoImpl.class);
        departmentForTest = Department.builder().withId((long)0).build();
    }

    @Test
    void createAndReadShouldAddNewCourseToDatabaseIfArgumentIsCourse(){
        Course addingCourse = Course.builder()
                .withName("Rome History")
                .withDepartment(departmentForTest)
                .build();

        courseDao.save(addingCourse);
        Course readingCourse = courseDao.findById((long)5).get();

        assertCourses(readingCourse, addingCourse);
    }

    @Test
    void createAndReadShouldAddListOfNewCoursesToDatabaseIfArgumentIsListOfCourses(){
        List<Course> addingCourseEntities = Arrays.asList(Course.builder()
                .withName("Rome History")
                .withDepartment(departmentForTest)
                .build());
        courseDao.saveAll(addingCourseEntities);
        List<Course> readingCourseEntities = Arrays.asList(courseDao.findById((long)5).get());

        assertCourses(readingCourseEntities, addingCourseEntities);
    }

    @Test
    void updateShouldUpdateDataOfCoursesIfArgumentIsCourses(){
        Course expected = Course.builder()
                .withId((long)4)
                .withName("Rome History")
                .withDepartment(departmentDao.findById((long)1).get())
                .build();
        courseDao.update(expected);
        Course actual = courseDao.findById((long)4).get();

        assertCourses(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfProfessorIfArgumentIsListOfProfessor(){
        List<Course> expected = Arrays.asList(Course.builder()
                .withId((long)4)
                .withName("Rome History")
                .withDepartment(departmentDao.findById((long)1).get())
                .build());
        courseDao.updateAll(expected);
        List<Course> actual = Arrays.asList(courseDao.findById((long)4).get());

        assertCourses(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Course> expected = Optional.empty();
        courseDao.deleteById((long)10);
        Optional<Course> actual = courseDao.findById((long)10);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addCourseToProfessorCourseListShouldAddCourseToProfessorCourseListIfArgumentIsIdOfProfessorAndIdOfCourse(){
        List<Course> expectedBeforeAdding = Arrays.asList (courseDao.findById((long)1).get(),
                courseDao.findById((long)2).get());
        List<Course> actualBeforeAdding = courseDao.findByProfessorId(7);
        assertCourses(actualBeforeAdding, expectedBeforeAdding);

        courseDao.addCourseToProfessorCourseList(3, 7);

        List<Course> expectedAfterAdding = Arrays.asList (courseDao.findById((long)1).get(),
                courseDao.findById((long)2).get(), courseDao.findById((long)3).get());
        List<Course> actualAfterAdding = courseDao.findByProfessorId(7);
        assertCourses(actualAfterAdding, expectedAfterAdding);
    }

    @Test
    void removeCourseToProfessorCourseListShouldRemoveCourseToProfessorCourseListIfArgumentIsIdOfProfessorAndIdOfCourse(){
        List<Course> expectedBeforeRemoving = Arrays.asList (courseDao.findById((long)1).get(),
                courseDao.findById((long)2).get());
        List<Course> actualBeforeRemoving = courseDao.findByProfessorId(7);
        assertCourses(actualBeforeRemoving, expectedBeforeRemoving);

        courseDao.removeCourseFromProfessorCourseList(2, 7);

        List<Course> expectedAfterRemoving = Arrays.asList (courseDao.findById((long)1).get());
        List<Course> actualAfterRemoving = courseDao.findByProfessorId(7);
        assertCourses(actualAfterRemoving, expectedAfterRemoving);
    }

    @Test
    void findByNameShouldReturnOptionalOfDepartmentEntityIfArgumentIsName(){
        Course expected = Course.builder()
                .withName("Russia History")
                .withDepartment(departmentDao.findById((long)1).get())
                .build();
        Course actual = courseDao.findByName("Russia History").get();

        assertCourses(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        Optional<Course> expected = Optional.empty();
        Optional<Course> actual = courseDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
