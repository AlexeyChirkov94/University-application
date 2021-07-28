package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.ProfessorRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.ScienceDegreeRequestMapper;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

@Component
public class ProfessorRequestMapperImpl implements ProfessorRequestMapper {

    private final DepartmentRequestMapper departmentRequestMapper;
    private final ScienceDegreeRequestMapper scienceDegreeRequestMapper;
    private final CourseRequestMapper courseRequestMapper;

    public ProfessorRequestMapperImpl(DepartmentRequestMapper departmentRequestMapper,
                                      ScienceDegreeRequestMapper scienceDegreeRequestMapper, CourseRequestMapper courseRequestMapper) {
        this.departmentRequestMapper = departmentRequestMapper;
        this.scienceDegreeRequestMapper = scienceDegreeRequestMapper;
        this.courseRequestMapper = courseRequestMapper;
    }

    @Override
    public Professor mapDtoToEntity(ProfessorRequest dto) {
        if (dto == null) {
            return null;
        } else {
            return Professor.builder()
                    .withId(dto.getId())
                    .withFirstName(dto.getFirstName())
                    .withLastName(dto.getLastName())
                    .withEmail(dto.getEmail())
                    .withPassword(dto.getPassword())
                    .withDepartment(departmentRequestMapper.mapDtoToEntity(dto.getDepartmentRequest()))
                    .withScienceDegree(scienceDegreeRequestMapper.mapDtoToEntity(dto.getScienceDegreeRequest()))
                    .withCourses(getCourses(dto))
                    .build();
        }
    }

    @Override
    public ProfessorRequest mapEntityToDto(Professor entity) {
        if (entity == null) {
            return null;
        } else {
            ProfessorRequest professorRequest = new ProfessorRequest();
            professorRequest.setId(entity.getId());
            professorRequest.setFirstName(entity.getFirstName());
            professorRequest.setLastName(entity.getLastName());
            professorRequest.setEmail(entity.getEmail());
            professorRequest.setPassword(entity.getPassword());
            professorRequest.setDepartmentRequest(departmentRequestMapper.mapEntityToDto(entity.getDepartment()));
            professorRequest.setScienceDegreeRequest(scienceDegreeRequestMapper.mapEntityToDto(entity.getScienceDegree()));
            professorRequest.setCoursesRequest(getCoursesRequest(entity));

            return professorRequest;
        }
    }

    private List<Course> getCourses(ProfessorRequest dto){
        List<Course> courses = new ArrayList<>();
        if (dto.getCoursesRequest() == null) {
            return emptyList();
        } else{
            List<CourseRequest> courseRequests = dto.getCoursesRequest();
            for (CourseRequest courseRequest : courseRequests) {
                courses.add(courseRequestMapper.mapDtoToEntity(courseRequest));
            }
            return courses;
        }
    }

    private List<CourseRequest> getCoursesRequest(Professor entity){
        List<CourseRequest> coursesRequest = new ArrayList<>();
        if (entity.getCourses() == null) {
            return emptyList();
        } else {
            List<Course> courses = entity.getCourses();
            for(Course course : courses){
                coursesRequest.add(courseRequestMapper.mapEntityToDto(course));
            }
            return coursesRequest;
        }
    }

}
