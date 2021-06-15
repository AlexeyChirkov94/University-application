package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;

@Component
public class DepartmentResponseMapperImpl implements DepartmentResponseMapper {

    @Override
    public Department mapDtoToEntity(DepartmentResponse dto) {
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
    public DepartmentResponse mapEntityToDto(Department entity) {
        if (entity == null) {
            return null;
        } else {
            DepartmentResponse departmentResponse = new DepartmentResponse();
            departmentResponse.setId(entity.getId());
            departmentResponse.setName(entity.getName());

            return departmentResponse;
        }
    }

}
