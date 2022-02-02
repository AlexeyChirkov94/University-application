package ua.com.foxminded.university.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
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

    CourseResponse courseResponse;
    GroupResponse groupResponse;
    ProfessorResponse professorResponse;
    FormOfLessonResponse formOfLessonResponse;

    {
        courseResponse = new CourseResponse();
        courseResponse.setId(0L);
        courseResponse.setName("");
        groupResponse = new GroupResponse();
        groupResponse.setId(0L);
        groupResponse.setName("");
        professorResponse = new ProfessorResponse();
        professorResponse.setId(0L);
        formOfLessonResponse = new FormOfLessonResponse();
        formOfLessonResponse.setId(0L);
        formOfLessonResponse.setName("");
    }

    @Mapping(target = "id", expression = "java(lesson.getId() == null ? 0 : lesson.getId())")
    @Mapping(target = "timeOfStartLesson", expression = "java(lesson.getTimeOfStartLesson())")
    @Mapping(target = "courseResponse", expression = "java(lesson.getCourse() == null ? " +
            "courseResponse : courseMapper.mapEntityToDto(lesson.getCourse()))")
    @Mapping(target = "groupResponse", expression = "java(lesson.getGroup() == null ? " +
            "groupResponse : groupMapper.mapEntityToDto(lesson.getGroup()))")
    @Mapping(target = "teacher", expression = "java(lesson.getTeacher() == null ? " +
            "professorResponse : professorMapper.mapEntityToDto(lesson.getTeacher()))")
    @Mapping(target = "formOfLessonResponse", expression = "java(lesson.getFormOfLesson() == null ? " +
            "formOfLessonResponse : formOfLessonMapper.mapEntityToDto(lesson.getFormOfLesson()))")
    public abstract LessonResponse mapEntityToDto (Lesson lesson);

    @Mapping(target = "withId", source = "id")
    @Mapping(target = "withTimeOfStartLesson", source = "timeOfStartLesson")
    public abstract Lesson mapDtoToEntity (LessonRequest lessonRequest);

}
