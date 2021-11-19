package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;

@Component
public class GroupRequestMapperImpl implements GroupRequestMapper {

    private final DepartmentRequestMapper departmentRequestMapper;
    private final FormOfEducationRequestMapper formOfEducationRequestMapper;

    public GroupRequestMapperImpl(DepartmentRequestMapper departmentRequestMapper,
                                  FormOfEducationRequestMapper formOfEducationRequestMapper) {
        this.departmentRequestMapper = departmentRequestMapper;
        this.formOfEducationRequestMapper = formOfEducationRequestMapper;
    }

    @Override
    public Group mapDtoToEntity(GroupRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Group.builder()
                    .withId(dto.getId())
                    .withName(dto.getName())
                    .build();
        }
    }

}
