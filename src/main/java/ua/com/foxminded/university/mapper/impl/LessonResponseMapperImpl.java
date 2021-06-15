package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.interfaces.CourseResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.LessonResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.ProfessorResponseMapper;

@Component
public class LessonResponseMapperImpl implements LessonResponseMapper {

    private final CourseResponseMapper courseResponseMapper;
    private final GroupResponseMapper groupResponseMapper;
    private final ProfessorResponseMapper professorResponseMapper;
    private final FormOfLessonResponseMapper formOfLessonResponseMapper;

    public LessonResponseMapperImpl(CourseResponseMapper courseResponseMapper, GroupResponseMapper groupResponseMapper,
                                    ProfessorResponseMapper professorResponseMapper, FormOfLessonResponseMapper formOfLessonResponseMapper) {
        this.courseResponseMapper = courseResponseMapper;
        this.groupResponseMapper = groupResponseMapper;
        this.professorResponseMapper = professorResponseMapper;
        this.formOfLessonResponseMapper = formOfLessonResponseMapper;
    }

    @Override
    public Lesson mapDtoToEntity(LessonResponse dto) {
        if (dto == null) {
            return null;
        } else {
            return Lesson.builder()
                    .withId(dto.getId())
                    .withCourse(courseResponseMapper.mapDtoToEntity(dto.getCourseResponse()))
                    .withTimeOfStartLesson(dto.getTimeOfStartLesson())
                    .withGroup(groupResponseMapper.mapDtoToEntity(dto.getGroupResponse()))
                    .withTeacher(professorResponseMapper.mapDtoToEntity(dto.getTeacher()))
                    .withFormOfLesson(formOfLessonResponseMapper.mapDtoToEntity(dto.getFormOfLessonResponse()))
                    .build();
        }
    }

    @Override
    public LessonResponse mapEntityToDto(Lesson entity) {
        if (entity == null) {
            return null;
        } else {
            LessonResponse lessonResponse = new LessonResponse();
            lessonResponse.setId(entity.getId());
            lessonResponse.setCourseResponse(courseResponseMapper.mapEntityToDto(entity.getCourse()));
            lessonResponse.setTimeOfStartLesson(entity.getTimeOfStartLesson());
            lessonResponse.setGroupResponse(groupResponseMapper.mapEntityToDto(entity.getGroup()));
            lessonResponse.setTeacher(professorResponseMapper.mapEntityToDto(entity.getTeacher()));
            lessonResponse.setFormOfLessonResponse(formOfLessonResponseMapper.mapEntityToDto(entity.getFormOfLesson()));

            return lessonResponse;
        }
    }

}
