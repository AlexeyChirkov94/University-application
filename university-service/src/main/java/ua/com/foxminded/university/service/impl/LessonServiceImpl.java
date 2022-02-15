package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.FormOfLessonRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.repository.StudentRepository;
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

    private final LessonRepository lessonRepository;
    private final FormOfLessonRepository formOfLessonRepository;
    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final LessonValidator lessonValidator;
    private final LessonMapper lessonMapper;

    @Override
    public List<LessonResponse> formTimeTableForGroup(long groupId) {
        checkThatGroupExist(groupId);

        return lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(groupId).stream().map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponse> formTimeTableForStudent(long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityDontExistException("There no student with id: " + studentId));

        if(student.getGroup() == null){
            throw new TimeTableStudentWithoutGroupException("Student with id: " + studentId + " not a member of any group");
        }

        long groupId = student.getGroup().getId();
        return lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(groupId).stream().map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponse> formTimeTableForProfessor(long professorId) {
        checkThatProfessorExist(professorId);

        return lessonRepository.findAllByTeacherIdOrderByTimeOfStartLesson(professorId).stream().map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponse create(LessonRequest lessonRequest) {

        Lesson lessonBeforeSave = lessonMapper.mapDtoToEntity(lessonRequest);
        Lesson lessonAfterSave = lessonRepository.save(lessonBeforeSave);

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
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityDontExistException("There no lesson with id: " + id));

        return lessonMapper.mapEntityToDto(lesson);
    }

    @Override
    public List<LessonResponse> findAll(String page) {
        final long itemsCount = lessonRepository.count();
        int pageNumber = parsePageNumber(page, itemsCount, 1);

        return lessonRepository.findAll(PageRequest.of(pageNumber - 1, ITEMS_PER_PAGE, Sort.by("id")))
                .stream().map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LessonResponse> findAll() {

        return lessonRepository.findAll(Sort.by("id")).stream()
                .map(lessonMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(LessonRequest lessonRequest) {

        lessonRepository.save(lessonMapper.mapDtoToEntity(lessonRequest));

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
        if(lessonRepository.findById(id).isPresent()){
            lessonRepository.deleteById(id);
        }
    }

    private void changeGroup(long lessonId, long newGroupId) {
        checkThatLessonExist(lessonId);
        checkThatGroupExist(newGroupId);

        lessonValidator.checkGroupTimeTableCrossing(lessonId, newGroupId);

        lessonRepository.changeGroup(lessonId, newGroupId);

    }

    private void changeFormOfLesson(long lessonId, long newFormOfLessonId) {
        checkThatLessonExist(lessonId);
        checkThatFormOfLessonExist(newFormOfLessonId);

        lessonRepository.changeFormOfLesson(lessonId, newFormOfLessonId);
    }

    private void changeTeacher(long lessonId, long newProfessorId) {
        Lesson lesson = checkThatLessonExist(lessonId);
        checkThatProfessorExist(newProfessorId);

        lessonRepository.changeTeacher(lessonId, newProfessorId);

        if(lesson.getCourse() != null){
            lessonValidator.validateCompatibilityCourseAndProfessor(lesson.getCourse().getId(), newProfessorId);
        }
        lessonValidator.checkProfessorTimeTableCrossing(lessonId, newProfessorId);
    }

    private void changeCourse(long lessonId, long newCourseId) {
        Lesson lesson = checkThatLessonExist(lessonId);
        checkThatCourseExist(newCourseId);

        lessonRepository.changeCourse(lessonId, newCourseId);

        if(lesson.getTeacher() != null){
            lessonValidator.validateCompatibilityCourseAndProfessor(newCourseId, lesson.getTeacher().getId());
        }
    }

    private Lesson checkThatLessonExist(long lessonId){
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityDontExistException("There no lesson with id: " + lessonId));

    }

    private void checkThatFormOfLessonExist(long formOfLessonId){
        if (!formOfLessonRepository.findById(formOfLessonId).isPresent()) {
            throw new EntityDontExistException("There no form of lesson with id: " + formOfLessonId);
        }
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

    private void checkThatGroupExist(long groupId){
        if (!groupRepository.findById(groupId).isPresent()) {
            throw new EntityDontExistException("There no group with id: " + groupId);
        }
    }

}
