package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import java.util.List;
import java.util.Optional;

public interface LessonService {

    LessonResponse register(LessonRequest requestDto);

    Optional<LessonResponse> findById(long id);

    List<LessonResponse> findAll(String page);

    List<LessonResponse> findAll();

    void edit(LessonRequest requestDto);

    boolean deleteById(long id);

    List<LessonResponse> formTimeTableForGroup(long groupId);

    List<LessonResponse> formTimeTableForProfessor(long professorId);

    void changeFormOfLesson(long lessonId, long newFormOfLessonId);

    void changeTeacher(long lessonId, long newProfessorId);

    void changeCourse(long lessonId, long newCourseId);

}
