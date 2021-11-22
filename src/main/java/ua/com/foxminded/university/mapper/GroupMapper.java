package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;

@Mapper
public abstract class GroupMapper {

    @Autowired
    protected DepartmentMapper departmentMapper;

    @Autowired
    protected FormOfEducationMapper formOfEducationMapper;

    @Mapping(target = "id", expression = "java(group.getId() == null ? 0 : group.getId())")
    @Mapping(target = "name", expression = "java(group.getName() == null ? \"\" : group.getName())")
    @Mapping(target = "departmentResponse", expression = "java(departmentMapper.mapEntityToDto(group.getDepartment()))")
    @Mapping(target = "formOfEducationResponse", expression = "java(formOfEducationMapper.mapEntityToDto(group.getFormOfEducation()))")
    public abstract GroupResponse mapEntityToDto (Group group);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    public abstract Group mapDtoToEntity (GroupRequest groupRequest);

}
