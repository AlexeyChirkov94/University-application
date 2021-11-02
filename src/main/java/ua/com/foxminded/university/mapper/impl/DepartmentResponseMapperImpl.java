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
        DepartmentResponse departmentResponse = new DepartmentResponse();
        if (entity == null) {
            return null;
        } else if (entity.getId() == 0L){
            departmentResponse.setId(0L);
            departmentResponse.setName("not chosen");
        } else {
            departmentResponse.setId(entity.getId());
            departmentResponse.setName(entity.getName());
        }

        return departmentResponse;
    }

}
