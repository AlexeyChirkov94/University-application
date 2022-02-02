package ua.com.foxminded.university.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;

import static org.assertj.core.api.Assertions.assertThat;

class GroupMapperTest {

    ApplicationContext context;
    GroupMapper groupMapper;
    Group group;
    Group groupWithEntities;
    Group emptyGroup;
    GroupResponse groupResponse;
    GroupRequest groupRequest;
    GroupResponse emptyGroupResponse;
    DepartmentResponse departmentResponse;
    DepartmentResponse emptyDepartmentResponse;
    FormOfEducationResponse formOfEducationResponse;
    FormOfEducationResponse emptyFormOfEducationResponse;


    {
        context = new AnnotationConfigApplicationContext("ua.com.foxminded.university.mapper");
        groupMapper = context.getBean(GroupMapper.class);

        group = Group.builder().withId(1L).withName("Group 1").build();
        emptyGroup = Group.builder().build();
        groupWithEntities = Group.builder().withId(1L).withName("Group 1")
                .withDepartment(Department.builder().withId(1L).withName("Dep 1").build())
                .withFormOfEducation(FormOfEducation.builder().withId(1L).withName("Form 1").build())
                .build();

        groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName("Group 1");

        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("Dep 1");

        emptyDepartmentResponse = new DepartmentResponse();
        emptyDepartmentResponse.setId(0L);
        emptyDepartmentResponse.setName("");

        formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId(1L);
        formOfEducationResponse.setName("Form 1");

        emptyFormOfEducationResponse = new FormOfEducationResponse();
        emptyFormOfEducationResponse.setId(0L);
        emptyFormOfEducationResponse.setName("");

        groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");
        groupResponse.setDepartmentResponse(departmentResponse);
        groupResponse.setFormOfEducationResponse(formOfEducationResponse);

        emptyGroupResponse = new GroupResponse();
        emptyGroupResponse.setId(0L);
        emptyGroupResponse.setName("");
        emptyGroupResponse.setDepartmentResponse(emptyDepartmentResponse);
        emptyGroupResponse.setFormOfEducationResponse(emptyFormOfEducationResponse);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsGroupEntity() {
        GroupResponse expected = groupResponse;
        GroupResponse actual = groupMapper.mapEntityToDto(groupWithEntities);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldMapEntityToDtoIfArgumentIsGroupEntityWhichNotFoundedInDB() {
        GroupResponse expected = emptyGroupResponse;
        GroupResponse actual = groupMapper.mapEntityToDto(emptyGroup);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapEntityToDtoShouldShouldReturnNullIfArgumentNull() {
        assertThat(groupMapper.mapEntityToDto(null)).isNull();
    }

    @Test
    void mapDtoToEntityShouldMapDtoToEntityIfArgumentIsGroupRequestDto() {
        Group expected = group;
        Group actual = groupMapper.mapDtoToEntity(groupRequest);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void mapDtoToEntityShouldReturnNullIfArgumentNull() {
        assertThat(groupMapper.mapDtoToEntity(null)).isNull();
    }

}
