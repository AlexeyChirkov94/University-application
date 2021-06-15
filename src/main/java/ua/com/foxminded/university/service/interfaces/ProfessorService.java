package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import java.util.List;

public interface ProfessorService extends UserService<ProfessorRequest, ProfessorResponse>{

    void changeScienceDegree(long professorId, int idNewScienceDegree);

    List<ProfessorResponse> findByCourseId(long courseId);

}
