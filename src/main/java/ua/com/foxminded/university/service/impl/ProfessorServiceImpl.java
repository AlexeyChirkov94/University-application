package ua.com.foxminded.university.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ProfessorRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.ProfessorResponseMapper;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import ua.com.foxminded.university.service.validator.ScienceDegreeValidator;
import ua.com.foxminded.university.service.validator.UserValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfessorServiceImpl extends AbstractUserServiceImpl<ProfessorRequest, ProfessorResponse>  implements ProfessorService {

    private final ScienceDegreeValidator scienceDegreeValidator;
    private final ProfessorDao professorDao;
    private final LessonDao lessonDao;
    private final CourseDao courseDao;
    private final ProfessorRequestMapper professorRequestMapper;
    private final ProfessorResponseMapper professorResponseMapper;
    private final UserValidator userValidator;

    public ProfessorServiceImpl(ProfessorDao professorDao, LessonDao lessonDao, CourseDao courseDao, PasswordEncoder passwordEncoder,
                                UserValidator userValidator, ScienceDegreeValidator scienceDegreeValidator,
                                ProfessorRequestMapper professorRequestMapper, ProfessorResponseMapper professorResponseMapper) {
        super(passwordEncoder, professorDao, userValidator);
        this.professorDao = professorDao;
        this.lessonDao = lessonDao;
        this.courseDao = courseDao;
        this.scienceDegreeValidator = scienceDegreeValidator;
        this.professorRequestMapper = professorRequestMapper;
        this.professorResponseMapper = professorResponseMapper;
        this.userValidator = userValidator;
    }

    @Override
    public Optional<ProfessorResponse> findById(long id) {
        return professorDao.findById(id).map(professorResponseMapper::mapEntityToDto);
    }

    @Override
    public List<ProfessorResponse> findAll(String page) {
        final long itemsCount = professorDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return professorDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(professorResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProfessorResponse> findAll() {

        return professorDao.findAll().stream()
                .map(professorResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(ProfessorRequest user) {
        userValidator.validate(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        professorDao.update(professorRequestMapper.mapDtoToEntity(user));
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public boolean deleteById(long id) {
        if(professorDao.findById(id).isPresent()){

            List<Course> professorCourses = courseDao.findByProfessorId(id);
            for(Course course : professorCourses) {
                courseDao.removeCourseFromProfessorCourseList(course.getId(), id);
            }

            List<Lesson> professorsLessons = lessonDao.findByProfessorId(id);
            for(Lesson lesson : professorsLessons){
                lessonDao.removeTeacherFromLesson(lesson.getId());
            }

            return professorDao.deleteById(id);
        }
        return false;
    }

    @Override
    public Optional<ProfessorResponse> findByEmail(String email) {
        return professorDao.findByEmail(email).map(professorResponseMapper::mapEntityToDto);
    }

    @Override
    public void changeScienceDegree(long professorId, int idNewScienceDegree) {
        scienceDegreeValidator.validate(ScienceDegree.getById(idNewScienceDegree));
        professorDao.changeScienceDegree(professorId, idNewScienceDegree);
    }

    @Override
    public List<ProfessorResponse> findByCourseId(long courseId) {
        List<Professor> professors = professorDao.findByCourseId(courseId);
        List<ProfessorResponse> professorResponses = new ArrayList<>();
        for(Professor professor : professors){
            professorResponses.add(professorResponseMapper.mapEntityToDto(professor));
        }

        return professorResponses;
    }

    @Override
    protected ProfessorResponse registerCertainUser(ProfessorRequest userDto) {
        Professor professorBeforeSave = professorRequestMapper.mapDtoToEntity(userDto);
        Professor professorAfterSave = professorDao.save(professorBeforeSave);

        return professorResponseMapper.mapEntityToDto(professorAfterSave);
    }

}
