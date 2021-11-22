package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Department;
import static org.assertj.core.api.Assertions.assertThat;

class DepartmentMapperTest {

    ApplicationContext context;
    DepartmentMapper departmentMapper;
    Department department;
    Department emptyDepartment;
    DepartmentRequest departmentRequest;
    DepartmentResponse departmentResponse;
    DepartmentResponse emptyDepartmentResponse;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        departmentMapper = context.getBean(DepartmentMapper.class);

        department = Department.builder().withId(1L).withName("Dep 1").build();
        emptyDepartment = Department.builder().build();

        departmentRequest = new DepartmentRequest();
        departmentRequest.setId(1L);
        departmentRequest.setName("Dep 1");

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
        DepartmentResponse actual = departmentMapper.mapEntityToDto(department);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsDepartmentEntityWhichNotFoundedInDB() {
        DepartmentResponse expected = emptyDepartmentResponse;
        DepartmentResponse actual = departmentMapper.mapEntityToDto(emptyDepartment);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(departmentMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsDepartmentRequestDto() {
        Department expected = department;
        Department actual = departmentMapper.mapDtoToEntity(departmentRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(departmentMapper.mapDtoToEntity(null)).isNull();
    }

}
