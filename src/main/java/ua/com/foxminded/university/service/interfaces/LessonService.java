package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import java.util.List;

public interface LessonService {

    LessonResponse create(LessonRequest requestDto);

    LessonResponse findById(long id);

    List<LessonResponse> findAll(String page);

    List<LessonResponse> findAll();

    void edit(LessonRequest requestDto);

    boolean deleteById(long id);

    List<LessonResponse> formTimeTableForGroup(long groupId);

    List<LessonResponse> formTimeTableForStudent(long studentId);

    List<LessonResponse> formTimeTableForProfessor(long professorId);

}
