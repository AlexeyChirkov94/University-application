package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.domain.Department;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertDepartments;

public class DepartmentDaoImplTest {

    ApplicationContext context;
    DepartmentDao departmentDao;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        departmentDao = context.getBean(DepartmentDaoImpl.class);
    }

    @Test
    void createAndReadShouldAddNewDepartmentToDatabaseIfArgumentIsDepartment(){
        Department addingDepartment = Department.builder().withName("new Department").build();
        departmentDao.save(addingDepartment);
        Department readingDepartment = departmentDao.findById((long)3).get();

        assertDepartments(readingDepartment, addingDepartment);
    }

    @Test
    void createAndReadShouldAddListOfNewDepartmentsToDatabaseIfArgumentIsListOfDepartments(){
        List<Department> addingDepartment = Arrays.asList (Department.builder().withName("new Department").build());
        departmentDao.saveAll(addingDepartment);
        List<Department> readingDepartment = Arrays.asList(departmentDao.findById((long)3).get());

        assertDepartments(readingDepartment, addingDepartment);
    }

    @Test
    void updateShouldUpdateDataOfDepartmentIfArgumentIsDepartment(){
        Department expected = Department.builder()
                .withId((long)1)
                .withName("History")
                .build();

        departmentDao.update(expected);
        Department actual = departmentDao.findById((long)1).get();

        assertDepartments(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfDepartmentsIfArgumentIsListOfDepartments() {
        List<Department> expected = Arrays.asList(Department.builder()
                .withId((long)1)
                .withName("History")
                .build());
        departmentDao.updateAll(expected);
        List<Department> actual = Arrays.asList(departmentDao.findById((long)1).get());

        assertDepartments(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfDepartmentIfArgumentIsIdOfDepartment(){
        Optional<Department> expected = Optional.empty();
        departmentDao.deleteById((long)3);
        Optional<Department> actual = departmentDao.findById((long)3);

        assertThat(expected).isEqualTo(actual);
    }
}
