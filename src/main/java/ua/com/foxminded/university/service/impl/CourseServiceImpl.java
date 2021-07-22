package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.entity.Course;
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
public class CourseServiceImpl extends AbstractPageableCrudService<CourseRequest, CourseResponse>
        implements CourseService {

    private final CourseDao courseDao;
    private final ProfessorDao professorDao;
    private final CourseRequestMapper courseRequestMapper;
    private final CourseResponseMapper courseResponseMapper;

    @Override
    @Transactional(transactionManager = "txManager")
    public void addCourseToProfessorCourseList(long courseId, long professorId) {
        checkThatCourseAndProfessorExist(courseId, professorId);

        if (!isCourseExistInProfessorCourseList(courseId, professorId)){
            courseDao.addCourseToProfessorCourseList(courseId, professorId);
        }
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void removeCourseFromProfessorCourseList(long courseId, long professorId) {
        checkThatCourseAndProfessorExist(courseId, professorId);

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
    public CourseResponse register(CourseRequest courseRequest) {
        if (courseDao.findByName(courseRequest.getName()).isPresent()){
            throw new EntityAlreadyExistException("Course with same name already exist");
        } else {
            Course courseBeforeSave = courseRequestMapper.mapDtoToEntity(courseRequest);
            Course courseAfterSave = courseDao.save(courseBeforeSave);

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
    public void edit(CourseRequest courseRequest) {
        courseDao.update(courseRequestMapper.mapDtoToEntity(courseRequest));
    }

    @Override
    public boolean deleteById(long id) {
        if(courseDao.findById(id).isPresent()){
            return courseDao.deleteById(id);
        }
        return false;
    }

    private void checkThatCourseAndProfessorExist(long courseId, long professorId){
        if (!courseDao.findById(courseId).isPresent() || !professorDao.findById(professorId).isPresent()) {
            throw new EntityDontExistException("There no professor or course with this ids");
        }
    }

    private boolean isCourseExistInProfessorCourseList (long courseId, long professorId){
            List<Course> coursesOfProfessor = courseDao.findByProfessorId(professorId);
    return coursesOfProfessor.stream().map(Course::getId).collect(Collectors.toList()).contains(courseId);
    }

}
