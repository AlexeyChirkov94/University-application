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
    public StudentResponse mapEntityToDto(Student entity) {
        StudentResponse studentResponse = new StudentResponse();
        if (entity == null) {
            return null;
        } else if (entity.getId() == 0L){
            studentResponse.setId(0L);
            studentResponse.setFirstName("");
            studentResponse.setLastName("");
            studentResponse.setEmail("");
            studentResponse.setPassword("");
            studentResponse.setGroupResponse(groupResponseMapper.mapEntityToDto(entity.getGroup()));
        } else {
            studentResponse.setId(entity.getId());
            studentResponse.setFirstName(entity.getFirstName());
            studentResponse.setLastName(entity.getLastName());
            studentResponse.setEmail(entity.getEmail());
            studentResponse.setPassword(entity.getPassword());
            studentResponse.setGroupResponse(groupResponseMapper.mapEntityToDto(entity.getGroup()));
        }

        return studentResponse;
    }

}
