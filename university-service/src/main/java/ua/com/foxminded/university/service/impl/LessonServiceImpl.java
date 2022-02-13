package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.FormOfLessonDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.LessonMapper;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.exception.TimeTableStudentWithoutGroupException;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.validator.LessonValidator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class LessonServiceImpl extends AbstractPageableCrudService implements LessonService {

    private final LessonDao lessonDao;
    private final FormOfLessonDao formOfLessonDao;
    private final ProfessorDao professorDao;
    private final CourseDao courseDao;
    private final GroupDao groupDao;
    private final StudentDao studentDao;
    private final LessonValidator lessonValidator;
    private final LessonMapper lessonMapper;

    @Override
    public List<LessonResponse> formTimeTableForGroup(long groupId) {
        checkThatGroupExist(groupId);

        return lessonDao.findByGroupId(groupId).stream().map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponse> formTimeTableForStudent(long studentId) {
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new EntityDontExistException("There no student with id: " + studentId));

        long groupId = student.getGroup().getId();
        if(groupId == 0L){
            throw new TimeTableStudentWithoutGroupException("Student with id: " + studentId + " not a member of any group");
        }
        return lessonDao.findByGroupId(groupId).stream().map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponse> formTimeTableForProfessor(long professorId) {
        checkThatProfessorExist(professorId);

        return lessonDao.findByProfessorId(professorId).stream().map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponse create(LessonRequest lessonRequest) {

        Lesson lessonBeforeSave = lessonMapper.mapDtoToEntity(lessonRequest);
        Lesson lessonAfterSave = lessonDao.save(lessonBeforeSave);

        if(lessonRequest.getCourseId() != 0L){
            changeCourse(lessonAfterSave.getId(), lessonRequest.getCourseId());
        }
        if(lessonRequest.getGroupId() != 0L){
            changeGroup(lessonAfterSave.getId(), lessonRequest.getGroupId());
        }
        if(lessonRequest.getTeacherId() != 0L){
            changeTeacher(lessonAfterSave.getId(), lessonRequest.getTeacherId());
        }
        if(lessonRequest.getFormOfLessonId() != 0L){
            changeFormOfLesson(lessonAfterSave.getId(), lessonRequest.getFormOfLessonId());
        }

        return lessonMapper.mapEntityToDto(lessonAfterSave);
    }

    @Override
    public LessonResponse findById(long id) {
        Lesson lesson = lessonDao.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no lesson with id: " + id));

        return lessonMapper.mapEntityToDto(lesson);
    }

    @Override
    public List<LessonResponse> findAll(String page) {
        final long itemsCount = lessonDao.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonDao.findAll(pageNumber, ITEMS_PER_PAGE).stream()
                .map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponse> findAll() {

        return lessonDao.findAll().stream()
                .map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonRequest lessonRequest) {

        lessonDao.update(lessonMapper.mapDtoToEntity(lessonRequest));

        if(lessonRequest.getCourseId() != 0L){
            changeCourse(lessonRequest.getId(), lessonRequest.getCourseId());
        }
        if(lessonRequest.getGroupId() != 0L){
            changeGroup(lessonRequest.getId(), lessonRequest.getGroupId());
        }
        if(lessonRequest.getTeacherId() != 0L){
            changeTeacher(lessonRequest.getId(), lessonRequest.getTeacherId());
        }
        if(lessonRequest.getFormOfLessonId() != 0L){
            changeFormOfLesson(lessonRequest.getId(), lessonRequest.getFormOfLessonId());
        }

    }

    @Override
    public void deleteById(long id) {
        if(lessonDao.findById(id).isPresent()){
            lessonDao.deleteById(id);
        }
    }

    private void changeGroup(long lessonId, long newGroupId) {
        checkThatLessonExist(lessonId);
        checkThatGroupExist(newGroupId);

        lessonValidator.checkGroupTimeTableCrossing(lessonId, newGroupId);

        lessonDao.changeGroup(lessonId, newGroupId);

    }

    private void changeFormOfLesson(long lessonId, long newFormOfLessonId) {
        checkThatLessonExist(lessonId);
        checkThatFormOfLessonExist(newFormOfLessonId);

        lessonDao.changeFormOfLesson(lessonId, newFormOfLessonId);
    }

    private void changeTeacher(long lessonId, long newProfessorId) {
        Lesson lesson = checkThatLessonExist(lessonId);
        checkThatProfessorExist(newProfessorId);

        lessonDao.changeTeacher(lessonId, newProfessorId);

        if(lesson.getCourse() != null){
            lessonValidator.validateCompatibilityCourseAndProfessor(lesson.getCourse().getId(), newProfessorId);
        }
        lessonValidator.checkProfessorTimeTableCrossing(lessonId, newProfessorId);
    }

    private void changeCourse(long lessonId, long newCourseId) {
        Lesson lesson = checkThatLessonExist(lessonId);
        checkThatCourseExist(newCourseId);

        lessonDao.changeCourse(lessonId, newCourseId);

        if(lesson.getTeacher() != null){
            lessonValidator.validateCompatibilityCourseAndProfessor(newCourseId, lesson.getTeacher().getId());
        }
    }

    private Lesson checkThatLessonExist(long lessonId){
        return lessonDao.findById(lessonId)
                .orElseThrow(() -> new EntityDontExistException("There no lesson with id: " + lessonId));

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

}
