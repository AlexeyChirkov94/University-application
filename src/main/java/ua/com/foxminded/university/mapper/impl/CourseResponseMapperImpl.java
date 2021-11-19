package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.mapper.interfaces.CourseResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;

@Component
public class CourseResponseMapperImpl implements CourseResponseMapper {

    private final DepartmentResponseMapper departmentResponseMapper;

    public CourseResponseMapperImpl(DepartmentResponseMapper departmentResponseMapper) {
        this.departmentResponseMapper = departmentResponseMapper;
    }

    @Override
    public CourseResponse mapEntityToDto(Course entity) {
        CourseResponse courseResponse = new CourseResponse();
        if (entity == null) {
            courseResponse = null;
        }  else if (entity.getId() == 0L) {
            courseResponse.setId(0L);
            courseResponse.setName("");
            courseResponse.setDepartmentResponse(departmentResponseMapper.mapEntityToDto(entity.getDepartment()));
        } else {
            courseResponse.setId(entity.getId());
            courseResponse.setName(entity.getName());
            courseResponse.setDepartmentResponse(departmentResponseMapper.mapEntityToDto(entity.getDepartment()));
        }

        return courseResponse;
    }

}
