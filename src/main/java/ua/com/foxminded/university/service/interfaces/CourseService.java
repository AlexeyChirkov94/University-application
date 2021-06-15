package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;

import java.util.List;

public interface CourseService extends CrudService<CourseRequest, CourseResponse> {

    void addCourseToProfessorCourseList(long courseId, long professorId);

    void removeCourseFromProfessorCourseList(long courseId, long professorId);

    List<CourseResponse> findByProfessorId(long professorId);

}
