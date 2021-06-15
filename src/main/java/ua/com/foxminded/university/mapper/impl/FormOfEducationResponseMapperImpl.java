package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationResponseMapper;

@Component
public class FormOfEducationResponseMapperImpl implements FormOfEducationResponseMapper {

    @Override
    public FormOfEducation mapDtoToEntity(FormOfEducationResponse dto) {
        if (dto == null) {
            return null;
        } else {
            return FormOfEducation.builder()
                    .withId(dto.getId())
                    .withName(dto.getName())
                    .build();
        }
    }

    @Override
    public FormOfEducationResponse mapEntityToDto(FormOfEducation entity) {
        if (entity == null) {
            return null;
        } else {
            FormOfEducationResponse formOfEducationResponse = new FormOfEducationResponse();
            formOfEducationResponse.setId(entity.getId());
            formOfEducationResponse.setName(entity.getName());

            return formOfEducationResponse;
        }
    }

}
