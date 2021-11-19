package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.ProfessorRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.ScienceDegreeRequestMapper;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.emptyList;

@Component
public class ProfessorRequestMapperImpl implements ProfessorRequestMapper {

    @Override
    public Professor mapDtoToEntity(ProfessorRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Professor.builder()
                    .withId(dto.getId())
                    .withFirstName(dto.getFirstName())
                    .withLastName(dto.getLastName())
                    .withEmail(dto.getEmail())
                    .withPassword(dto.getPassword())
                    .build();
        }
    }

}
