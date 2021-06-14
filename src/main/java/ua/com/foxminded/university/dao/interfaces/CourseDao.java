package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.domain.Course;
import java.util.List;

public interface CourseDao extends CrudPageableDao<Course>{

    void addCourseToProfessorCourseList(long courseId, long professorId);

    void removeCourseFromProfessorCourseList(long courseId, long professorId);

    List<Course> findByProfessorId(long professorId);

}
