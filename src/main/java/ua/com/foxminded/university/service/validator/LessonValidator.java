package ua.com.foxminded.university.service.validator;

public interface LessonValidator {

    void validateCompatibilityCourseAndProfessor(long courseId, long professorId);

    void checkGroupTimeTableCrossing(long lessonId, long groupId);

    void checkProfessorTimeTableCrossing(long lessonId, long professorId);

}
