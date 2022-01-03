package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.entity.FormOfEducation;

@Mapper
public interface FormOfEducationMapper {

    @Mapping(target = "id", expression = "java(formOfEducation.getId() == null ? 0 : formOfEducation.getId())")
    @Mapping(target = "name", expression = "java(formOfEducation.getName() == null ? \"\" : formOfEducation.getName())")
    FormOfEducationResponse mapEntityToDto (FormOfEducation formOfEducation);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    FormOfEducation mapDtoToEntity (FormOfEducationRequest formOfEducationRequest);

}
