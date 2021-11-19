package ua.com.foxminded.university.mapper.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;
import static org.assertj.core.api.Assertions.assertThat;

class DepartmentRequestMapperImplTest {

    ApplicationContext context;
    DepartmentRequestMapper departmentRequestMapper;
    Department department;
    DepartmentRequest departmentRequest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        departmentRequestMapper = context.getBean(DepartmentRequestMapperImpl.class);
        department = Department.builder().withId(1L).withName("Dep 1").build();
        departmentRequest = new DepartmentRequest();
        departmentRequest.setId(1L);
        departmentRequest.setName("Dep 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsDepartmentRequestDto() {
        Department expected = department;
        Department actual = departmentRequestMapper.mapDtoToEntity(departmentRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(departmentRequestMapper.mapDtoToEntity(null)).isNull();
    }

}
