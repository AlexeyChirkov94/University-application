package ua.com.foxminded.university.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.dao.RoleDao;
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
import ua.com.foxminded.university.service.validator.UserValidator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "hibernateTransactionManager")
public class ProfessorServiceImpl extends AbstractUserServiceImpl<ProfessorRequest, ProfessorResponse>  implements ProfessorService {

    private final ScienceDegreeValidator scienceDegreeValidator;
    private final ProfessorDao professorDao;
    private final LessonDao lessonDao;
    private final CourseDao courseDao;
    private final DepartmentDao departmentDao;
    private final ProfessorMapper professorMapper;

    public ProfessorServiceImpl(ProfessorDao professorDao, LessonDao lessonDao, CourseDao courseDao, DepartmentDao departmentDao,
                                PasswordEncoder passwordEncoder, UserValidator userValidator, ScienceDegreeValidator scienceDegreeValidator,
                                ProfessorMapper professorMapper, RoleDao roleDao) {
        super(passwordEncoder, professorDao, roleDao, userValidator);
        this.professorDao = professorDao;
        this.lessonDao = lessonDao;
        this.courseDao = courseDao;
        this.departmentDao = departmentDao;
        this.scienceDegreeValidator = scienceDegreeValidator;
        this.professorMapper = professorMapper;
    }

    @Override
    public ProfessorResponse findById(long id) {
        Professor professor = professorDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no professor with id: " + id));

        return professorMapper.mapEntityToDto(professor);
    }

    @Override
    public List<ProfessorResponse> findAll(String page) {
        final long itemsCount = professorDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return professorDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(professorMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfessorResponse> findAll() {

        return professorDao.findAll().stream()
                .map(professorMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(ProfessorRequest professorRequest) {

        userValidator.validate(professorRequest);
        professorRequest.setPassword(passwordEncoder.encode(professorRequest.getPassword()));

        professorDao.update(professorMapper.mapDtoToEntity(professorRequest));

        if(professorRequest.getDepartmentId() != 0L){
            changeDepartment(professorRequest.getId(), professorRequest.getDepartmentId());
        }

        if(professorRequest.getScienceDegreeId() != 0L){
            changeScienceDegree(professorRequest.getId(), professorRequest.getScienceDegreeId());
        }

    }

    @Override
    public void deleteById(long id) {
        if(professorDao.findById(id).isPresent()){
            removeAllRolesFromUser(id);

            List<Course> professorCourses = courseDao.findByProfessorId(id);
            for(Course course : professorCourses) {
                courseDao.removeCourseFromProfessorCourseList(course.getId(), id);
            }

            List<Lesson> professorsLessons = lessonDao.findByProfessorId(id);
            for(Lesson lesson : professorsLessons){
                lessonDao.removeTeacherFromLesson(lesson.getId());
            }

            professorDao.deleteById(id);
        }
    }

    @Override
    public List<ProfessorResponse> findByEmail(String email) {
        List<Professor> professors = professorDao.findByEmail(email);

        if(!professors.isEmpty()){
            ProfessorResponse professorResponse = professorMapper.mapEntityToDto(professors.get(0));
            professorResponse.setRoles(roleDao.findByUserId(professorResponse.getId()));
            return Arrays.asList(professorResponse);
        }

        return Collections.emptyList();
    }

    @Override
    public void changeScienceDegree(long professorId, int idNewScienceDegree) {
        checkThatUserExist(professorId);
        scienceDegreeValidator.validate(ScienceDegree.getById(idNewScienceDegree));
        professorDao.changeScienceDegree(professorId, idNewScienceDegree);
    }

    @Override
    public List<ProfessorResponse> findByCourseId(long courseId) {
        List<Professor> professors = professorDao.findByCourseId(courseId);
        List<ProfessorResponse> professorResponses = new ArrayList<>();
        for(Professor professor : professors){
            professorResponses.add(professorMapper.mapEntityToDto(professor));
        }

        return professorResponses;
    }

    @Override
    public List<ProfessorResponse> findByDepartmentId(long departmentId) {
        checkThatDepartmentExist(departmentId);

        return professorDao.findByDepartmentId(departmentId)
                .stream().map(professorMapper ::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changeDepartment(long professorId, long departmentId) {
        checkThatDepartmentExist(departmentId);
        checkThatUserExist(professorId);

        professorDao.changeDepartment(professorId, departmentId);
    }

    @Override
    public void removeDepartmentFromProfessor(long professorId) {
        checkThatUserExist(professorId);
        professorDao.removeDepartmentFromProfessor(professorId);
    }

    @Override
    protected ProfessorResponse registerCertainUser(ProfessorRequest professorRequest) {
        Professor professorBeforeSave = professorMapper.mapDtoToEntity(professorRequest);
        Professor professorAfterSave = professorDao.save(professorBeforeSave);
        long professorId = professorAfterSave.getId();

        if(professorRequest.getDepartmentId() != 0L){
            changeDepartment(professorId, professorRequest.getDepartmentId());
        }

        if(professorRequest.getScienceDegreeId() != 0L){
            changeScienceDegree(professorId, professorRequest.getScienceDegreeId());
        }

        if(roleDao.findByUserId(professorId).isEmpty()) {

            List<Role> professorRoles = roleDao.findByName("ROLE_PROFESSOR");

            if (professorRoles.isEmpty()){
                throw new EntityDontExistException("ROLE_PROFESSOR not initialized");
            }

            addRoleToUser(professorId, professorRoles.get(0).getId());
        }

        return professorMapper.mapEntityToDto(professorAfterSave);
    }

    private void checkThatDepartmentExist(long departmentId){
        if (!departmentDao.findById(departmentId).isPresent()) {
            throw new EntityDontExistException("There no department with id: " + departmentId);
        }
    }

}
