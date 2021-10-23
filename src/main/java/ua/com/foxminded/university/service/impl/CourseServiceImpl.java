package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.CourseResponseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.interfaces.CourseService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CourseServiceImpl extends AbstractPageableCrudService implements CourseService {

    private final CourseDao courseDao;
    private final ProfessorDao professorDao;
    private final LessonDao lessonDao;
    private final DepartmentDao departmentDao;
    private final CourseRequestMapper courseRequestMapper;
    private final CourseResponseMapper courseResponseMapper;

    @Override
    @Transactional(transactionManager = "txManager")
    public void addCourseToProfessorCourseList(long courseId, long professorId) {
        checkThatCourseExist(courseId);
        checkThatProfessorExist(professorId);

        if (!isCourseExistInProfessorCourseList(courseId, professorId)){
            courseDao.addCourseToProfessorCourseList(courseId, professorId);
        }
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void removeCourseFromProfessorCourseList(long courseId, long professorId) {
        checkThatCourseExist(courseId);
        checkThatProfessorExist(professorId);

        if (isCourseExistInProfessorCourseList(courseId, professorId)){
            courseDao.removeCourseFromProfessorCourseList(courseId, professorId);
        }
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public List<CourseResponse> findByProfessorId(long professorId) {
        if (professorDao.findById(professorId).isPresent()){
            return courseDao.findByProfessorId(professorId).stream().map(courseResponseMapper::mapEntityToDto)
                    .collect(Collectors.toList());
        } else
            throw new EntityDontExistException("There no professor with id = " + professorId);
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public CourseResponse create(CourseRequest courseRequest) {
        if (courseDao.findByName(courseRequest.getName()).isPresent()){
            throw new EntityAlreadyExistException("Course with same name already exist");
        } else {
            Course courseBeforeSave = courseRequestMapper.mapDtoToEntity(courseRequest);
            Course courseAfterSave = courseDao.save(courseBeforeSave);

            if(courseRequest.getDepartmentId() != 0L){
                checkThatCourseExist(courseAfterSave.getId());
                checkThatDepartmentExist(courseRequest.getDepartmentId());

                courseDao.changeDepartment(courseAfterSave.getId(), courseRequest.getDepartmentId());
            }

            return courseResponseMapper.mapEntityToDto(courseAfterSave);
        }
    }

    @Override
    public Optional<CourseResponse> findById(long id) {
        return courseDao.findById(id).map(courseResponseMapper::mapEntityToDto);
    }

    @Override
    public List<CourseResponse> findAll(String page) {
        final long itemsCount = courseDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return courseDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(courseResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> findAll() {
        return courseDao.findAll().stream()
                .map(courseResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(CourseRequest courseRequest) {

        courseDao.update(courseRequestMapper.mapDtoToEntity(courseRequest));

        if(courseRequest.getDepartmentId() != 0L){
            checkThatCourseExist(courseRequest.getId());
            checkThatDepartmentExist(courseRequest.getDepartmentId());

            courseDao.changeDepartment(courseRequest.getId(), courseRequest.getDepartmentId());
        }

    }

    @Override
    public boolean deleteById(long id) {
        if(courseDao.findById(id).isPresent()){

            List<Lesson> courseLessons = lessonDao.findByCourseId(id);
            for(Lesson lesson : courseLessons){
                lessonDao.removeCourseFromLesson(lesson.getId());
            }

            return courseDao.deleteById(id);
        }
        return false;
    }

    @Override
    public void removeDepartmentFromCourse(long courseId){
        checkThatCourseExist(courseId);
        courseDao.removeDepartmentFromCourse(courseId);
    }

    private void checkThatProfessorExist(long professorId){
        if (!professorDao.findById(professorId).isPresent()) {
            throw new EntityDontExistException("There no professor with id: " + professorId);
        }
    }

    private void checkThatCourseExist(long courseId){
        if (!courseDao.findById(courseId).isPresent()) {
            throw new EntityDontExistException("There no course with id: " + courseId);
        }
    }

    private void checkThatDepartmentExist(long departmentId){
        if (!departmentDao.findById(departmentId).isPresent()) {
            throw new EntityDontExistException("There no department with id: " + departmentId);
        }
    }

    private boolean isCourseExistInProfessorCourseList (long courseId, long professorId){
            List<Course> coursesOfProfessor = courseDao.findByProfessorId(professorId);
    return coursesOfProfessor.stream().map(Course::getId).collect(Collectors.toList()).contains(courseId);
    }

}
