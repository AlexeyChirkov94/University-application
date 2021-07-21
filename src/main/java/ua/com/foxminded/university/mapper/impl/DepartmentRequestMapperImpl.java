package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;

@Component
public class DepartmentRequestMapperImpl implements DepartmentRequestMapper {

    @Override
    public Department mapDtoToEntity(DepartmentRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Department.builder()
                    .withId(dto.getId())
                    .withName(dto.getName())
                    .build();
        }
    }

    @Override
    public DepartmentRequest mapEntityToDto(Department entity) {
        if (entity == null) {
            return null;
        } else {
            DepartmentRequest departmentRequest = new DepartmentRequest();
            departmentRequest.setId(entity.getId());
            departmentRequest.setName(entity.getName());

            return departmentRequest;
        }
    }

}
