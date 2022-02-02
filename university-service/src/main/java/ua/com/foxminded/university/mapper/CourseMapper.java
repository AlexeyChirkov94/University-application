package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Course;

@Mapper
public abstract class CourseMapper {

    @Autowired
    protected DepartmentMapper departmentMapper;

    DepartmentResponse departmentResponse;

    {
        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(0L);
        departmentResponse.setName("");
    }

    @Mapping(target = "id", expression = "java(course.getId() == null ? 0 : course.getId())")
    @Mapping(target = "name", expression = "java(course.getName() == null ? \"\" : course.getName())")
    @Mapping(target = "departmentResponse", expression = "java(course.getDepartment() == null ? " +
            "departmentResponse : departmentMapper.mapEntityToDto(course.getDepartment()))")
    public abstract CourseResponse mapEntityToDto (Course course);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    public abstract Course mapDtoToEntity (CourseRequest courseRequest);

}
