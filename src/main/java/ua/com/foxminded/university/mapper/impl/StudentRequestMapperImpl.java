package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.StudentRequestMapper;

@Component
public class StudentRequestMapperImpl implements StudentRequestMapper {

    @Override
    public Student mapDtoToEntity(StudentRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Student.builder()
                    .withId(dto.getId())
                    .withFirstName(dto.getFirstName())
                    .withLastName(dto.getLastName())
                    .withEmail(dto.getEmail())
                    .withPassword(dto.getPassword())
                    .build();
        }
    }

}
