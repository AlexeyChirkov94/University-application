package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.LessonRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.LessonResponseMapper;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.LessonService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonServiceImpl extends AbstractPageableCrudService implements LessonService {

    private final LessonDao lessonDao;
    private final FormOfLessonDao formOfLessonDao;
    private final ProfessorDao professorDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final LessonRequestMapper lessonRequestMapper;
    private final LessonResponseMapper lessonResponseMapper;

    @Override
    @Transactional(transactionManager = "txManager")
    public List<LessonResponse> formTimeTableForGroup(long groupId) {
        checkThatGroupExist(groupId);

        return lessonDao.formTimeTableForGroup(groupId).stream().map(lessonResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public List<LessonResponse> formTimeTableForProfessor(long professorId) {
        checkThatProfessorExist(professorId);

        return lessonDao.formTimeTableForProfessor(professorId).stream().map(lessonResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void changeFormOfLesson(long lessonId, long newFormOfLessonId) {
        checkThatLessonExist(lessonId);
        checkThatFormOfLessonExist(newFormOfLessonId);

        lessonDao.changeFormOfLesson(lessonId, newFormOfLessonId);
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void changeTeacher(long lessonId, long newProfessorId) {
        checkThatLessonExist(lessonId);
        checkThatProfessorExist(newProfessorId);

        lessonDao.changeTeacher(lessonId, newProfessorId);
    }

    @Override
    @Transactional(transactionManager = "txManager")
    public void changeCourse(long lessonId, long newCourseId) {
        checkThatLessonExist(lessonId);
        checkThatCourseExist(newCourseId);

        lessonDao.changeCourse(lessonId, newCourseId);
    }

    @Override
    public LessonResponse register(LessonRequest lessonRequest) {
        validate(lessonRequest);

        Lesson lessonBeforeSave = lessonRequestMapper.mapDtoToEntity(lessonRequest);
        Lesson lessonAfterSave = lessonDao.save(lessonBeforeSave);

        return lessonResponseMapper.mapEntityToDto(lessonAfterSave);
    }

    @Override
    public Optional<LessonResponse> findById(long id) {
        return lessonDao.findById(id).map(lessonResponseMapper::mapEntityToDto);
    }

    @Override
    public List<LessonResponse> findAll(String page) {
        final long itemsCount = lessonDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(lessonResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponse> findAll() {

        return lessonDao.findAll().stream()
                .map(lessonResponseMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonRequest lessonRequest) {
        lessonDao.update(lessonRequestMapper.mapDtoToEntity(lessonRequest));
    }

    @Override
    public boolean deleteById(long id) {
        if(lessonDao.findById(id).isPresent()){
            return lessonDao.deleteById(id);
        }
        return false;
    }

    private void checkThatLessonExist(long lessonId){
        if (!lessonDao.findById(lessonId).isPresent()) {
            throw new EntityDontExistException("There no lesson with id: " + lessonId);
        }
    }

    private void checkThatFormOfLessonExist(long formOfLessonId){
        if (!formOfLessonDao.findById(formOfLessonId).isPresent()) {
            throw new EntityDontExistException("There no form of lesson with id: " + formOfLessonId);
        }
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

    private void checkThatGroupExist(long groupId){
        if (!groupDao.findById(groupId).isPresent()) {
            throw new EntityDontExistException("There no group with id: " + groupId);
        }
    }

    private void validate(LessonRequest lessonRequest) {
        Lesson lesson = lessonRequestMapper.mapDtoToEntity(lessonRequest);
        Professor professor = lesson.getTeacher();
        Group group = lesson.getGroup();
        LocalDateTime timeOfStartLesson = lesson.getTimeOfStartLesson();

        List<Lesson> existingLessonsOfGroup = lessonDao.formTimeTableForGroup(group.getId());
        List<Lesson> existingLessonOfTeacher = lessonDao.formTimeTableForProfessor(professor.getId());

        boolean isNewLessonMatchesWithAnotherLessonsInGroup = existingLessonsOfGroup.stream()
                .map(Lesson::getTimeOfStartLesson)
                .collect(Collectors.toList()).contains(timeOfStartLesson);
        boolean isNewLessonMatchesWithAnotherLessonsOfProfessor = existingLessonOfTeacher.stream()
                .map(Lesson::getTimeOfStartLesson)
                .collect(Collectors.toList()).contains(timeOfStartLesson);

        if (isNewLessonMatchesWithAnotherLessonsInGroup || isNewLessonMatchesWithAnotherLessonsOfProfessor){
            throw new ValidateException("Lesson can`t be appointed on this time");
        }
    }

}
