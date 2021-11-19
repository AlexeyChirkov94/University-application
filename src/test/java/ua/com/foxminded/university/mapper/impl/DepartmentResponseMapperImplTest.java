package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;
import static org.assertj.core.api.Assertions.assertThat;

class DepartmentResponseMapperImplTest {

    ApplicationContext context;
    DepartmentResponseMapper departmentResponseMapper;
    Department department;
    Department emptyDepartment;
    DepartmentResponse departmentResponse;
    DepartmentResponse emptyDepartmentResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        departmentResponseMapper = context.getBean(DepartmentResponseMapperImpl.class);
        department = Department.builder().withId(1L).withName("Dep 1").build();
        emptyDepartment = Department.builder().withId(0L).build();
        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("Dep 1");
        emptyDepartmentResponse = new DepartmentResponse();
        emptyDepartmentResponse.setId(0L);
        emptyDepartmentResponse.setName("");
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsDepartmentEntity() {
        DepartmentResponse expected = departmentResponse;
        DepartmentResponse actual = departmentResponseMapper.mapEntityToDto(department);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsDepartmentEntityWhichNotFoundedInDB() {
        DepartmentResponse expected = emptyDepartmentResponse;
        DepartmentResponse actual = departmentResponseMapper.mapEntityToDto(emptyDepartment);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(departmentResponseMapper.mapEntityToDto(null)).isNull();
    }

}
