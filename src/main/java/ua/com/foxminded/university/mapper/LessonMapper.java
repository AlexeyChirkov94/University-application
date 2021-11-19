package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Lesson;

@Mapper
public abstract class LessonMapper {

    @Autowired
    protected CourseMapper courseMapper;

    @Autowired
    protected GroupMapper groupMapper;

    @Autowired
    protected ProfessorMapper professorMapper;

    @Autowired
    protected FormOfLessonMapper formOfLessonMapper;

    @Mapping(target = "id", expression = "java(lesson.getId() == null ? 0 : lesson.getId())")
    @Mapping(target = "timeOfStartLesson", expression = "java(lesson.getTimeOfStartLesson())")
    @Mapping(target = "courseResponse", expression = "java(courseMapper.mapEntityToDto(lesson.getCourse()))")
    @Mapping(target = "groupResponse", expression = "java(groupMapper.mapEntityToDto(lesson.getGroup()))")
    @Mapping(target = "teacher", expression = "java(professorMapper.mapEntityToDto(lesson.getTeacher()))")
    @Mapping(target = "formOfLessonResponse", expression = "java(formOfLessonMapper.mapEntityToDto(lesson.getFormOfLesson()))")
    public abstract LessonResponse mapEntityToDto (Lesson lesson);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withTimeOfStartLesson", source = "timeOfStartLesson")
    public abstract Lesson mapDtoToEntity (LessonRequest lessonRequest);

}
