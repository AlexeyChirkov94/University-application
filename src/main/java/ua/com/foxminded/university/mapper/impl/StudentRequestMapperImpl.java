package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.StudentRequestMapper;

@Component
public class StudentRequestMapperImpl implements StudentRequestMapper {

    private final GroupRequestMapper groupRequestMapper;

    public StudentRequestMapperImpl(GroupRequestMapper groupRequestMapper) {
        this.groupRequestMapper = groupRequestMapper;
    }

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
                    .withGroup(groupRequestMapper.mapDtoToEntity(dto.getGroupRequest()))
                    .build();
        }
    }

    @Override
    public StudentRequest mapEntityToDto(Student entity) {
        if (entity == null) {
            return null;
        } else {
            StudentRequest studentRequest = new StudentRequest();
            studentRequest.setId(entity.getId());
            studentRequest.setFirstName(entity.getFirstName());
            studentRequest.setLastName(entity.getLastName());
            studentRequest.setEmail(entity.getEmail());
            studentRequest.setPassword(entity.getPassword());
            studentRequest.setGroupRequest(groupRequestMapper.mapEntityToDto(entity.getGroup()));

            return studentRequest;
        }
    }

}
