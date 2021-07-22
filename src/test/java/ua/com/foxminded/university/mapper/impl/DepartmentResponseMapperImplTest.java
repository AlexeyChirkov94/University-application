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
    DepartmentResponse departmentResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        departmentResponseMapper = context.getBean(DepartmentResponseMapperImpl.class);
        department = Department.builder().withId(1L).withName("Dep 1").build();
        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("Dep 1");
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsDepartmentResponseDto() {
        Department expected = department;
        Department actual = departmentResponseMapper.mapDtoToEntity(departmentResponse);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(departmentResponseMapper.mapDtoToEntity(null)).isNull();
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsDepartmentEntity() {
        DepartmentResponse expected = departmentResponse;
        DepartmentResponse actual = departmentResponseMapper.mapEntityToDto(department);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(departmentResponseMapper.mapEntityToDto(null)).isNull();
    }

}