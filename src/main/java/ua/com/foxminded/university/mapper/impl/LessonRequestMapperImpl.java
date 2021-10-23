package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.LessonRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.ProfessorRequestMapper;

@Component
public class LessonRequestMapperImpl implements LessonRequestMapper {

    private final CourseRequestMapper courseRequestMapper;
    private final GroupRequestMapper groupRequestMapper;
    private final ProfessorRequestMapper professorRequestMapper;
    private final FormOfLessonRequestMapper formOfLessonRequestMapper;

    public LessonRequestMapperImpl(CourseRequestMapper courseRequestMapper, GroupRequestMapper groupRequestMapper,
                                   ProfessorRequestMapper professorRequestMapper, FormOfLessonRequestMapper formOfLessonRequestMapper) {
        this.courseRequestMapper = courseRequestMapper;
        this.groupRequestMapper = groupRequestMapper;
        this.professorRequestMapper = professorRequestMapper;
        this.formOfLessonRequestMapper = formOfLessonRequestMapper;
    }

    @Override
    public Lesson mapDtoToEntity(LessonRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Lesson.builder()
                    .withId(dto.getId())
                    .withTimeOfStartLesson(dto.getTimeOfStartLesson())
                    .build();
        }
    }

    @Override
    public LessonRequest mapEntityToDto(Lesson entity) {
        if (entity == null) {
            return null;
        } else {
            LessonRequest lessonRequest = new LessonRequest();
            lessonRequest.setId(entity.getId());
            lessonRequest.setCourseId(courseRequestMapper.mapEntityToDto(entity.getCourse()).getId());
            lessonRequest.setTimeOfStartLesson(entity.getTimeOfStartLesson());
            lessonRequest.setGroupId(groupRequestMapper.mapEntityToDto(entity.getGroup()).getId());
            lessonRequest.setTeacherId(professorRequestMapper.mapEntityToDto(entity.getTeacher()).getId());
            lessonRequest.setFormOfLessonId(formOfLessonRequestMapper.mapEntityToDto(entity.getFormOfLesson()).getId());

            return lessonRequest;
        }

    }
}
