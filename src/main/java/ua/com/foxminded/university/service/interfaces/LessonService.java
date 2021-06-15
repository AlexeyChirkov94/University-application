package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Lesson;

import java.util.List;

public interface LessonService extends CrudService<LessonRequest, LessonResponse> {

    List<LessonResponse> formTimeTableForGroup(long groupId);

    List<LessonResponse> formTimeTableForProfessor(long professorId);

    void changeFormOfLesson(long lessonId, long newFormOfLessonId);

    void changeTeacher(long lessonId, long newProfessorId);

    void changeCourse(long lessonId, long newCourseId);

}
