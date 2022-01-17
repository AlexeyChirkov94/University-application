package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.Course;
import java.util.List;

public interface CourseDao extends CrudPageableDao<Course>{

    List<Course> findByName(String name);

    void addCourseToProfessorCourseList(long courseId, long professorId);

    void removeCourseFromProfessorCourseList(long courseId, long professorId);

    List<Course> findByProfessorId(long professorId);

    List<Course> findByDepartmentId(long departmentId);

    void changeDepartment(long courseId, long newDepartmentId);

    void removeDepartmentFromCourse(long courseId);

}
