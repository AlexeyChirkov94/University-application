package ua.com.foxminded.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.entity.Course;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByName(String name);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO professor_course (professor_id, course_id) VALUES( ?2, ?1)", nativeQuery = true)
    void addCourseToProfessorCourseList(long courseId, long professorId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM professor_course WHERE professor_id = ?2 AND course_id = ?1", nativeQuery = true)
    void removeCourseFromProfessorCourseList(long courseId, long professorId);

    @Query(value = "SELECT c.id, c.name, c.department_id, d.name as department_name from courses c " +
            "left join departments d on c.department_id = d.id " +
            "left join professor_course pc on c.id = pc.course_id " +
            "where pc.professor_id = ?1 order by c.id", nativeQuery = true)
    List<Course> findByProfessorId(long professorId);

    @Query("from Course where department.id=:departmentId order by id")
    List<Course> findByDepartmentId(long departmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Course c SET c.department.id = ?2 WHERE id = ?1")
    void changeDepartment(long courseId, long newDepartmentId);

    @Transactional
    @Modifying
    @Query("UPDATE Course c SET c.department.id = null WHERE id = ?1")
    void removeDepartmentFromCourse(long courseId);

}
