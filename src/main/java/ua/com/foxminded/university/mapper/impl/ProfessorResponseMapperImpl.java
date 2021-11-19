package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.CourseResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.ProfessorResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.ScienceDegreeResponseMapper;
import java.util.ArrayList;
import java.util.List;
import static java.util.Collections.emptyList;

@Component
public class ProfessorResponseMapperImpl implements ProfessorResponseMapper {

    private final DepartmentResponseMapper departmentResponseMapper;
    private final ScienceDegreeResponseMapper scienceDegreeResponseMapper;
    private final CourseResponseMapper courseResponseMapper;

    public ProfessorResponseMapperImpl(DepartmentResponseMapper departmentResponseMapper,
                                       ScienceDegreeResponseMapper scienceDegreeResponseMapper, CourseResponseMapper courseResponseMapper) {
        this.departmentResponseMapper = departmentResponseMapper;
        this.scienceDegreeResponseMapper = scienceDegreeResponseMapper;
        this.courseResponseMapper = courseResponseMapper;
    }

    @Override
    public ProfessorResponse mapEntityToDto(Professor entity) {
        ProfessorResponse professorResponse = new ProfessorResponse();
        if (entity == null) {
            return null;
        } else if (entity.getId() == 0L) {
            professorResponse.setId(0L);
            professorResponse.setFirstName("");
            professorResponse.setLastName("");
            professorResponse.setEmail("");
            professorResponse.setPassword("");
            professorResponse.setDepartmentResponse(departmentResponseMapper.mapEntityToDto(entity.getDepartment()));
            professorResponse.setScienceDegreeResponse(scienceDegreeResponseMapper.mapEntityToDto(entity.getScienceDegree()));
            professorResponse.setCoursesResponse(emptyList());
        } else {
            professorResponse.setId(entity.getId());
            professorResponse.setFirstName(entity.getFirstName());
            professorResponse.setLastName(entity.getLastName());
            professorResponse.setEmail(entity.getEmail());
            professorResponse.setPassword(entity.getPassword());
            professorResponse.setDepartmentResponse(departmentResponseMapper.mapEntityToDto(entity.getDepartment()));
            professorResponse.setScienceDegreeResponse(scienceDegreeResponseMapper.mapEntityToDto(entity.getScienceDegree()));
            professorResponse.setCoursesResponse(getCoursesResponse(entity));
        }

        return professorResponse;
    }

    private List<CourseResponse> getCoursesResponse(Professor entity){
        List<CourseResponse> coursesResponse = new ArrayList<>();
        if (entity.getCourses() == null) {
            return emptyList();
        } else {
                List<Course> courses = entity.getCourses();
                for(Course course : courses){
                    coursesResponse.add(courseResponseMapper.mapEntityToDto(course));
                }
            return coursesResponse;
        }
    }

}
