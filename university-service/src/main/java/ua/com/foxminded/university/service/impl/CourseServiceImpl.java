package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.CourseMapper;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.CourseService;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(transactionManager = "hibernateTransactionManager")
public class CourseServiceImpl extends AbstractPageableCrudService implements CourseService {

    private final CourseDao courseDao;
    private final ProfessorDao professorDao;
    private final LessonDao lessonDao;
    private final DepartmentDao departmentDao;
    private final CourseMapper courseMapper;


    @Override
    public void addCourseToProfessorCourseList(long courseId, long professorId) {
        checkThatCourseExist(courseId);
        checkThatProfessorExist(professorId);

        if (!isCourseExistInProfessorCourseList(courseId, professorId)){
            courseDao.addCourseToProfessorCourseList(courseId, professorId);
        }
    }

    @Override
    public void removeCourseFromProfessorCourseList(long courseId, long professorId) {
        checkThatCourseExist(courseId);
        checkThatProfessorExist(professorId);

        if (isCourseExistInProfessorCourseList(courseId, professorId)){
            courseDao.removeCourseFromProfessorCourseList(courseId, professorId);
        }
    }

    @Override
    public void changeDepartment(long courseId, long departmentId) {
        checkThatCourseExist(courseId);
        checkThatDepartmentExist(departmentId);

        courseDao.changeDepartment(courseId, departmentId);
    }

    @Override
    public List<CourseResponse> findByProfessorId(long professorId) {
        if (professorDao.findById(professorId).isPresent()){
            return courseDao.findByProfessorId(professorId).stream().map(courseMapper::mapEntityToDto)
                    .collect(Collectors.toList());
        } else
            throw new EntityDontExistException("There no professor with id = " + professorId);
    }

    @Override
    public List<CourseResponse> findByDepartmentId(long departmentId) {
        checkThatDepartmentExist(departmentId);

        return courseDao.findByDepartmentId(departmentId).stream()
                .map(courseMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public CourseResponse create(CourseRequest courseRequest) {
        if (!courseDao.findByName(courseRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Course with same name already exist");
        } else {
            Course courseBeforeSave = courseMapper.mapDtoToEntity(courseRequest);
            Course courseAfterSave = courseDao.save(courseBeforeSave);

            if(courseRequest.getDepartmentId() != 0L){
                changeDepartment(courseAfterSave.getId(), courseRequest.getDepartmentId());
            }

            return courseMapper.mapEntityToDto(courseAfterSave);
        }
    }

    @Override
    public CourseResponse findById(long id) {
        Course course = courseDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no course with id: " + id));

        return courseMapper.mapEntityToDto(course);
    }

    @Override
    public List<CourseResponse> findAll(String page) {
        final long itemsCount = courseDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return courseDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(courseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> findAll() {
        return courseDao.findAll().stream()
                .map(courseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(CourseRequest courseRequest) {

        courseDao.update(courseMapper.mapDtoToEntity(courseRequest));

        if(courseRequest.getDepartmentId() != 0L){
            changeDepartment(courseRequest.getId(), courseRequest.getDepartmentId());
        }

    }

    @Override
    public void deleteById(long id) {
        if(courseDao.findById(id).isPresent()){

            List<Lesson> courseLessons = lessonDao.findByCourseId(id);
            for(Lesson lesson : courseLessons){
                lessonDao.removeCourseFromLesson(lesson.getId());
            }

            courseDao.deleteById(id);
        }
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
