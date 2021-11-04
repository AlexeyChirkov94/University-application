package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.entity.Professor;
import java.util.List;

public interface ProfessorDao extends UserDao<Professor> {

    void changeScienceDegree(long professorId, int idNewScienceDegree);

    List<Professor> findByCourseId(long courseId);

    List<Professor> findByDepartmentId(long departmentId);

    void changeDepartment(long professorId, long departmentId);

    void removeDepartmentFromProfessor(long professorId);

}
