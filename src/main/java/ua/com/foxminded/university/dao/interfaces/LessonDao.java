package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.entity.Lesson;

import java.util.List;

public interface LessonDao extends CrudPageableDao<Lesson> {

    List<Lesson> findByGroupId(long groupId);

    List<Lesson> findByProfessorId(long professorId);

    List<Lesson> findByCourseId(long courseId);

    List<Lesson> findByFormOfLessonId(long formOfLessonId);

    void changeFormOfLesson(long lessonId, long newFormOfLessonId);

    void removeFormOfLessonFromLesson(long lessonId);

    void changeTeacher(long lessonId, long newProfessorId);

    void removeTeacherFromLesson(long lessonId);

    void changeCourse(long lessonId, long newCourseId);

    void removeCourseFromLesson(long lessonId);

    void changeGroup(long lessonId, long newGroupId);

    void removeGroupFromLesson(long lessonId);

}
