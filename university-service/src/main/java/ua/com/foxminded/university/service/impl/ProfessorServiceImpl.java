package ua.com.foxminded.university.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.ProfessorMapper;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.ProfessorService;
import ua.com.foxminded.university.service.validator.ScienceDegreeValidator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProfessorServiceImpl extends AbstractUserServiceImpl<ProfessorRequest, ProfessorResponse>  implements ProfessorService {

    private final ScienceDegreeValidator scienceDegreeValidator;
    private final ProfessorRepository professorRepository;
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final ProfessorMapper professorMapper;

    public ProfessorServiceImpl(ProfessorRepository professorRepository, LessonRepository lessonRepository, CourseRepository courseRepository, DepartmentRepository departmentRepository,
                                PasswordEncoder passwordEncoder, ScienceDegreeValidator scienceDegreeValidator,
                                ProfessorMapper professorMapper, RoleRepository roleRepository) {
        super(passwordEncoder, professorRepository, roleRepository);
        this.professorRepository = professorRepository;
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.scienceDegreeValidator = scienceDegreeValidator;
        this.professorMapper = professorMapper;
    }

    @Override
    public ProfessorResponse findById(long id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no professor with id: " + id));

        return professorMapper.mapEntityToDto(professor);
    }

    @Override
    public List<ProfessorResponse> findAll(String page) {
        final long itemsCount = professorRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return professorRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE)).stream()
                .map(professorMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfessorResponse> findAll() {

        return professorRepository.findAll().stream()
                .map(professorMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(ProfessorRequest professorRequest) {

        professorRequest.setPassword(passwordEncoder.encode(professorRequest.getPassword()));

        professorRepository.save(professorMapper.mapDtoToEntity(professorRequest));

        if(professorRequest.getDepartmentId() != 0L){
            changeDepartment(professorRequest.getId(), professorRequest.getDepartmentId());
        }

        if(professorRequest.getScienceDegreeId() != 0L){
            changeScienceDegree(professorRequest.getId(), professorRequest.getScienceDegreeId());
        }

    }

    @Override
    public void deleteById(long id) {
        if(professorRepository.findById(id).isPresent()){
            removeAllRolesFromUser(id);

            List<Course> professorCourses = courseRepository.findByProfessorId(id);
            for(Course course : professorCourses) {
                courseRepository.removeCourseFromProfessorCourseList(course.getId(), id);
            }

            List<Lesson> professorsLessons = lessonRepository.findAllByTeacherIdOrderByTimeOfStartLesson(id);
            for(Lesson lesson : professorsLessons){
                lessonRepository.removeTeacherFromLesson(lesson.getId());
            }

            professorRepository.deleteById(id);
        }
    }

    @Override
    public List<ProfessorResponse> findByEmail(String email) {
        List<Professor> professors = professorRepository.findAllByEmail(email);

        if(!professors.isEmpty()){
            ProfessorResponse professorResponse = professorMapper.mapEntityToDto(professors.get(0));
            professorResponse.setRoles(roleRepository.findAllByUserId(professorResponse.getId()));
            return Arrays.asList(professorResponse);
        }

        return Collections.emptyList();
    }

    @Override
    public void changeScienceDegree(long professorId, int idNewScienceDegree) {
        checkThatUserExist(professorId);
        scienceDegreeValidator.validate(ScienceDegree.getById(idNewScienceDegree));
        professorRepository.changeScienceDegree(professorId, idNewScienceDegree);
    }

    @Override
    public List<ProfessorResponse> findByCourseId(long courseId) {
        List<Professor> professors = professorRepository.findAllByCourseId(courseId);
        List<ProfessorResponse> professorResponses = new ArrayList<>();
        for(Professor professor : professors){
            professorResponses.add(professorMapper.mapEntityToDto(professor));
        }

        return professorResponses;
    }

    @Override
    public List<ProfessorResponse> findByDepartmentId(long departmentId) {
        checkThatDepartmentExist(departmentId);

        return professorRepository.findAllByDepartmentId(departmentId)
                .stream().map(professorMapper ::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changeDepartment(long professorId, long departmentId) {
        checkThatDepartmentExist(departmentId);
        checkThatUserExist(professorId);

        professorRepository.changeDepartment(professorId, departmentId);
    }

    @Override
    public void removeDepartmentFromProfessor(long professorId) {
        checkThatUserExist(professorId);
        professorRepository.removeDepartmentFromProfessor(professorId);
    }

    @Override
    protected ProfessorResponse registerCertainUser(ProfessorRequest professorRequest) {
        Professor professorBeforeSave = professorMapper.mapDtoToEntity(professorRequest);
        Professor professorAfterSave = professorRepository.save(professorBeforeSave);
        long professorId = professorAfterSave.getId();

        if(professorRequest.getDepartmentId() != 0L){
            changeDepartment(professorId, professorRequest.getDepartmentId());
        }

        if(professorRequest.getScienceDegreeId() != 0L){
            changeScienceDegree(professorId, professorRequest.getScienceDegreeId());
        }

        if(roleRepository.findAllByUserId(professorId).isEmpty()) {

            List<Role> professorRoles = roleRepository.findAllByName("ROLE_PROFESSOR");

            if (professorRoles.isEmpty()){
                throw new EntityDontExistException("ROLE_PROFESSOR not initialized");
            }

            addRoleToUser(professorId, professorRoles.get(0).getId());
        }

        return professorMapper.mapEntityToDto(professorAfterSave);
    }

    private void checkThatDepartmentExist(long departmentId){
        if (!departmentRepository.findById(departmentId).isPresent()) {
            throw new EntityDontExistException("There no department with id: " + departmentId);
        }
    }

}
