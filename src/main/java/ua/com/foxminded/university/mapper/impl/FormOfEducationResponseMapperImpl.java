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
        FormOfEducationResponse formOfEducationResponse = new FormOfEducationResponse();
        if (entity == null) {
            return null;
        } else if (entity.getId() == 0L){
            formOfEducationResponse.setId(0L);
            formOfEducationResponse.setName("not chosen");
        } else {
            formOfEducationResponse.setId(entity.getId());
            formOfEducationResponse.setName(entity.getName());
        }

        return formOfEducationResponse;
    }

}
