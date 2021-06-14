package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.domain.Professor;

import java.util.List;

public interface ProfessorDao extends CrudPageableDao<Professor> {

    void changeScienceDegree(long professorId, int idNewScienceDegree);

    List<Professor> findByCourseId(long courseId);

}
