package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;

@Component
public class CourseRequestMapperImpl implements CourseRequestMapper {

    private final DepartmentRequestMapper departmentRequestMapper;

    public CourseRequestMapperImpl(DepartmentRequestMapper departmentRequestMapper) {
        this.departmentRequestMapper = departmentRequestMapper;
    }

    @Override
    public Course mapDtoToEntity(CourseRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Course.builder()
                    .withId(dto.getId())
                    .withName(dto.getName())
                    .build();
        }
    }

    @Override
    public CourseRequest mapEntityToDto(Course entity) {
        if (entity == null) {
            return null;
        } else {
            CourseRequest courseRequest = new CourseRequest();
            courseRequest.setId(entity.getId());
            courseRequest.setName(entity.getName());
            courseRequest.setDepartmentId(departmentRequestMapper.mapEntityToDto(entity.getDepartment()).getId());

            return courseRequest;
        }
    }

}
