package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Department;


@Mapper
public interface DepartmentMapper {

    @Mapping(target = "id", expression = "java(department.getId() == null ? 0 : department.getId())")
    @Mapping(target = "name", expression = "java(department.getName() == null ? \"\" : department.getName())")
    DepartmentResponse mapEntityToDto (Department department);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    Department mapDtoToEntity (DepartmentRequest departmentRequest);

}
