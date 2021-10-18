package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import java.util.List;
import java.util.Optional;

public interface CourseService {

    CourseResponse register(CourseRequest requestDto);

    Optional<CourseResponse> findById(long id);

    List<CourseResponse> findAll(String page);

    List<CourseResponse> findAll();

    void edit(CourseRequest requestDto);

    boolean deleteById(long id);

    void addCourseToProfessorCourseList(long courseId, long professorId);

    void removeCourseFromProfessorCourseList(long courseId, long professorId);

    List<CourseResponse> findByProfessorId(long professorId);

}
