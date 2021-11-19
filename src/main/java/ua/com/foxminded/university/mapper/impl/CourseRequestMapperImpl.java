package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;

@Component
public class CourseRequestMapperImpl implements CourseRequestMapper {

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

}
