package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.entity.FormOfLesson;

@Mapper
public interface FormOfLessonMapper {

    @Mapping(target = "id", expression = "java(formOfLesson.getId() == null ? 0 : formOfLesson.getId())")
    @Mapping(target = "name", expression = "java(formOfLesson.getName() == null ? \"\" : formOfLesson.getName())")
    @Mapping(target = "duration", expression = "java(formOfLesson.getDuration() == null ? 0 : formOfLesson.getDuration())")
    FormOfLessonResponse mapEntityToDto (FormOfLesson formOfLesson);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withName", source = "name")
    @Mapping(target = "withDuration", source = "duration")
    FormOfLesson mapDtoToEntity (FormOfLessonRequest formOfLessonRequest);

}
