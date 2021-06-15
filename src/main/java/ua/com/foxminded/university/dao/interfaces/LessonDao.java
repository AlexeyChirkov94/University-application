package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.entity.Lesson;

import java.util.List;

public interface LessonDao extends CrudPageableDao<Lesson> {

    List<Lesson> formTimeTableForGroup(long groupId);

    List<Lesson> formTimeTableForProfessor(long professorId);

    void changeFormOfLesson(long lessonId, long newFormOfLessonId);

    void changeTeacher(long lessonId, long newProfessorId);

    void changeCourse(long lessonId, long newCourseId);

}
