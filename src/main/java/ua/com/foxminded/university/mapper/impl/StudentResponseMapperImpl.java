package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.StudentResponseMapper;

@Component
public class StudentResponseMapperImpl implements StudentResponseMapper {

    private final GroupResponseMapper groupResponseMapper;

    public StudentResponseMapperImpl(GroupResponseMapper groupResponseMapper) {
        this.groupResponseMapper = groupResponseMapper;
    }

    @Override
    public Student mapDtoToEntity(StudentResponse dto) {
        if (dto == null) {
            return null;
        } else {
            return Student.builder()
                    .withId(dto.getId())
                    .withFirstName(dto.getFirstName())
                    .withLastName(dto.getLastName())
                    .withEmail(dto.getEmail())
                    .withPassword(dto.getPassword())
                    .withGroup(groupResponseMapper.mapDtoToEntity(dto.getGroupResponse()))
                    .build();
        }
    }

    @Override
    public StudentResponse mapEntityToDto(Student entity) {
        if (entity == null) {
            return null;
        } else {
            StudentResponse studentResponse = new StudentResponse();
            studentResponse.setId(entity.getId());
            studentResponse.setFirstName(entity.getFirstName());
            studentResponse.setLastName(entity.getLastName());
            studentResponse.setEmail(entity.getEmail());
            studentResponse.setPassword(entity.getPassword());
            studentResponse.setGroupResponse(groupResponseMapper.mapEntityToDto(entity.getGroup()));

            return studentResponse;
        }
    }

}
