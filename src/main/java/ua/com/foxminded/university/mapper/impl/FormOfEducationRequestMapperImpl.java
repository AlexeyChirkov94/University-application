package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationRequestMapper;

@Component
public class FormOfEducationRequestMapperImpl implements FormOfEducationRequestMapper {

    @Override
    public FormOfEducation mapDtoToEntity(FormOfEducationRequest dto) {
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
    public FormOfEducationRequest mapEntityToDto(FormOfEducation entity) {
        if (entity == null) {
            return null;
        } else {
            FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
            formOfEducationRequest.setId(entity.getId());
            formOfEducationRequest.setName(entity.getName());

            return formOfEducationRequest;
        }
    }

}
