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
    public Course mapDtoToEntity(CourseResponse dto) {
        if (dto == null) {
            return null;
        } else {
            return Course.builder()
                    .withId(dto.getId())
                    .withName(dto.getName())
                    .withDepartment(departmentResponseMapper.mapDtoToEntity(dto.getDepartmentResponse()))
                    .build();
        }
    }

    @Override
    public CourseResponse mapEntityToDto(Course entity) {
        if (entity == null) {
            return null;
        } else {
            CourseResponse courseResponse = new CourseResponse();
            courseResponse.setId(entity.getId());
            courseResponse.setName(entity.getName());
            courseResponse.setDepartmentResponse(departmentResponseMapper.mapEntityToDto(entity.getDepartment()));

            return courseResponse;
        }
    }

}
