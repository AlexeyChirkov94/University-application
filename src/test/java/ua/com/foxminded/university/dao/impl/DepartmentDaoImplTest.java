package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.entity.Department;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertDepartments;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsers;

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
        Department readingDepartment = departmentDao.findById(3L).get();

        assertDepartments(readingDepartment, addingDepartment);
    }

    @Test
    void createAndReadShouldAddListOfNewDepartmentsToDatabaseIfArgumentIsListOfDepartments(){
        List<Department> addingDepartmentEntities = Arrays.asList (Department.builder().withName("new Department").build());
        departmentDao.saveAll(addingDepartmentEntities);
        List<Department> readingDepartmentEntities = Arrays.asList(departmentDao.findById(3L).get());

        assertDepartments(readingDepartmentEntities, addingDepartmentEntities);
    }

    @Test
    void updateShouldUpdateDataOfDepartmentIfArgumentIsDepartment(){
        Department expected = Department.builder()
                .withId(1L)
                .withName("History")
                .build();

        departmentDao.update(expected);
        Department actual = departmentDao.findById(1L).get();

        assertDepartments(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfDepartmentsIfArgumentIsListOfDepartments() {
        List<Department> expected = Arrays.asList(Department.builder()
                .withId(1L)
                .withName("History")
                .build());
        departmentDao.updateAll(expected);
        List<Department> actual = Arrays.asList(departmentDao.findById(1L).get());

        assertDepartments(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfDepartmentIfArgumentIsIdOfDepartment(){
        Optional<Department> expected = Optional.empty();
        departmentDao.deleteById(3L);
        Optional<Department> actual = departmentDao.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfDepartmentEntityIfArgumentIsName(){
        Department expected = Department.builder()
                .withName("Department of History")
                .build();
        Department actual = departmentDao.findByName("Department of History").get();

        assertDepartments(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        Optional<Department> expected = Optional.empty();
        Optional<Department> actual = departmentDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }
}
