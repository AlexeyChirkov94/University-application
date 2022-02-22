package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.entity.Department;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertDepartments;

class DepartmentRepositoryImplTest {

    ApplicationContext context;
    DepartmentRepository departmentRepository;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        departmentRepository = context.getBean(DepartmentRepository.class);
    }

    @Test
    void createAndReadShouldAddNewDepartmentToDatabaseIfArgumentIsDepartment(){
        Department addingDepartment = Department.builder().withName("new Department").build();
        departmentRepository.save(addingDepartment);
        Department readingDepartment = departmentRepository.findById(4L).get();

        assertDepartments(readingDepartment, addingDepartment);
    }

    @Test
    void createAndReadShouldAddListOfNewDepartmentsToDatabaseIfArgumentIsListOfDepartments(){
        List<Department> addingDepartmentEntities = Arrays.asList (Department.builder().withName("new Department").build());
        departmentRepository.saveAll(addingDepartmentEntities);
        List<Department> readingDepartmentEntities = Arrays.asList(departmentRepository.findById(4L).get());

        assertDepartments(readingDepartmentEntities, addingDepartmentEntities);
    }

    @Test
    void updateShouldUpdateDataOfDepartmentIfArgumentIsDepartment(){
        Department expected = Department.builder()
                .withId(1L)
                .withName("History")
                .build();

        departmentRepository.save(expected);
        Department actual = departmentRepository.findById(1L).get();

        assertDepartments(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfDepartmentsIfArgumentIsListOfDepartments() {
        List<Department> expected = Arrays.asList(Department.builder()
                .withId(1L)
                .withName("History")
                .build());
        departmentRepository.saveAll(expected);
        List<Department> actual = Arrays.asList(departmentRepository.findById(1L).get());

        assertDepartments(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfDepartmentIfArgumentIsIdOfDepartment(){
        Optional<Department> expected = Optional.empty();
        departmentRepository.deleteById(3L);
        Optional<Department> actual = departmentRepository.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfDepartmentEntityIfArgumentIsName(){
        Department expected = Department.builder()
                .withName("Department of History")
                .build();
        Department actual = departmentRepository.findAllByName("Department of History").get(0);

        assertDepartments(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<Department> expected = Collections.emptyList();
        List<Department> actual = departmentRepository.findAllByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }
}
