package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
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
@Transactional
public class CourseServiceImpl extends AbstractPageableCrudService implements CourseService {

    private final CourseRepository courseRepository;
    private final ProfessorRepository professorRepository;
    private final LessonRepository lessonRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseMapper courseMapper;


    @Override
    public void addCourseToProfessorCourseList(long courseId, long professorId) {
        checkThatCourseExist(courseId);
        checkThatProfessorExist(professorId);

        if (!isCourseExistInProfessorCourseList(courseId, professorId)){
            courseRepository.addCourseToProfessorCourseList(courseId, professorId);
        }
    }

    @Override
    public void removeCourseFromProfessorCourseList(long courseId, long professorId) {
        checkThatCourseExist(courseId);
        checkThatProfessorExist(professorId);

        if (isCourseExistInProfessorCourseList(courseId, professorId)){
            courseRepository.removeCourseFromProfessorCourseList(courseId, professorId);
        }
    }

    @Override
    public void changeDepartment(long courseId, long departmentId) {
        checkThatCourseExist(courseId);
        checkThatDepartmentExist(departmentId);

        courseRepository.changeDepartment(courseId, departmentId);
    }

    @Override
    public List<CourseResponse> findByProfessorId(long professorId) {
        if (professorRepository.findById(professorId).isPresent()){
            return courseRepository.findByProfessorId(professorId).stream().map(courseMapper::mapEntityToDto)
                    .collect(Collectors.toList());
        } else
            throw new EntityDontExistException("There no professor with id = " + professorId);
    }

    @Override
    public List<CourseResponse> findByDepartmentId(long departmentId) {
        checkThatDepartmentExist(departmentId);

        return courseRepository.findByDepartmentId(departmentId).stream()
                .map(courseMapper::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public CourseResponse create(CourseRequest courseRequest) {
        if (!courseRepository.findAllByName(courseRequest.getName()).isEmpty()){
            throw new EntityAlreadyExistException("Course with same name already exist");
        } else {
            Course courseBeforeSave = courseMapper.mapDtoToEntity(courseRequest);
            Course courseAfterSave = courseRepository.save(courseBeforeSave);

            if(courseRequest.getDepartmentId() != 0L){
                changeDepartment(courseAfterSave.getId(), courseRequest.getDepartmentId());
            }

            return courseMapper.mapEntityToDto(courseAfterSave);
        }
    }

    @Override
    public CourseResponse findById(long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no course with id: " + id));

        return courseMapper.mapEntityToDto(course);
    }

    @Override
    public List<CourseResponse> findAll(String page) {
        final long itemsCount = courseRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return courseRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE, Sort.by("id")))
                .stream().map(courseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> findAll() {
        return courseRepository.findAll(Sort.by("id")).stream()
                .map(courseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(CourseRequest courseRequest) {

        courseRepository.save(courseMapper.mapDtoToEntity(courseRequest));

        if(courseRequest.getDepartmentId() != 0L){
            changeDepartment(courseRequest.getId(), courseRequest.getDepartmentId());
        }

    }

    @Override
    public void deleteById(long id) {
        if(courseRepository.findById(id).isPresent()){

            List<Lesson> courseLessons = lessonRepository.findAllByCourseIdOrderByTimeOfStartLesson(id);
            for(Lesson lesson : courseLessons){
                lessonRepository.removeCourseFromLesson(lesson.getId());
            }

            courseRepository.deleteById(id);
        }
    }

    @Override
    public void removeDepartmentFromCourse(long courseId){
        checkThatCourseExist(courseId);
        courseRepository.removeDepartmentFromCourse(courseId);
    }

    private void checkThatProfessorExist(long professorId){
        if (!professorRepository.findById(professorId).isPresent()) {
            throw new EntityDontExistException("There no professor with id: " + professorId);
        }
    }

    private void checkThatCourseExist(long courseId){
        if (!courseRepository.findById(courseId).isPresent()) {
            throw new EntityDontExistException("There no course with id: " + courseId);
        }
    }

    private void checkThatDepartmentExist(long departmentId){
        if (!departmentRepository.findById(departmentId).isPresent()) {
            throw new EntityDontExistException("There no department with id: " + departmentId);
        }
    }

    private boolean isCourseExistInProfessorCourseList (long courseId, long professorId){
            List<Course> coursesOfProfessor = courseRepository.findByProfessorId(professorId);
    return coursesOfProfessor.stream().map(Course::getId).collect(Collectors.toList()).contains(courseId);
    }

}
