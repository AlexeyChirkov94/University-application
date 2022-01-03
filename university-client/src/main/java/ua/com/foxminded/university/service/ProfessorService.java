package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import java.util.List;

public interface ProfessorService extends UserService<ProfessorRequest, ProfessorResponse>{

    void changeScienceDegree(long professorId, int idNewScienceDegree);

    List<ProfessorResponse> findByCourseId(long courseId);

    List<ProfessorResponse> findByDepartmentId(long departmentId);

    void changeDepartment(long professorId, long departmentId);

    void removeDepartmentFromProfessor(long professorId);

}
