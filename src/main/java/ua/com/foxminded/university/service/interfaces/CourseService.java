package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import java.util.List;

public interface CourseService {

    CourseResponse create(CourseRequest requestDto);

    CourseResponse findById(long id);

    List<CourseResponse> findByProfessorId(long professorId);

    List<CourseResponse> findByDepartmentId(long departmentId);

    List<CourseResponse> findAll(String page);

    List<CourseResponse> findAll();

    void edit(CourseRequest requestDto);

    boolean deleteById(long id);

    void addCourseToProfessorCourseList(long courseId, long professorId);

    void removeCourseFromProfessorCourseList(long courseId, long professorId);

    void changeDepartment(long courseId, long departmentId);

    void removeDepartmentFromCourse(long courseId);

}
