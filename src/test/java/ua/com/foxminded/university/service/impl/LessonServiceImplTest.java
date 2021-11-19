package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.LessonMapper;
import ua.com.foxminded.university.service.validator.LessonValidator;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class LessonServiceImplTest {

    @Mock
    LessonDao lessonDao;

    @Mock
    FormOfLessonDao formOfLessonDao;

    @Mock
    ProfessorDao professorDao;

    @Mock
    CourseDao courseDao;

    @Mock
    GroupDao groupDao;

    @Mock
    StudentDao studentDao;

    @Mock
    LessonMapper lessonMapper;

    @Mock
    LessonValidator lessonValidator;

    @InjectMocks
    LessonServiceImpl lessonService;

    @Test
    void findByGroupIdShouldReturnListOfLessonResponsesIfArgumentsIsGroupId() {
        long groupId = 2;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(2L).build()));
        when(lessonDao.findByGroupId(groupId)).thenReturn(lessons);

        lessonService.formTimeTableForGroup(groupId);

        verify(groupDao).findById(groupId);
        verify(lessonDao).findByGroupId(groupId);
    }

    @Test
    void findByStudentIdShouldReturnListOfLessonResponsesIfArgumentsIsGroupId() {
        long studentId = 1;
        long groupId = 2;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);
        Student student = Student.builder().withId(studentId).withGroup(Group.builder().withId(groupId).build()).build();

        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonDao.findByGroupId(groupId)).thenReturn(lessons);

        lessonService.formTimeTableForStudent(studentId);

        verify(studentDao).findById(studentId);
        verify(lessonDao).findByGroupId(groupId);
    }

    @Test
    void findByProfessorIdShouldReturnListOfLessonResponsesIfArgumentsIsProfessorId() {
        long professorId = 2;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(2L).build()));
        when(lessonDao.findByProfessorId(professorId)).thenReturn(lessons);

        lessonService.formTimeTableForProfessor(professorId);

        verify(professorDao).findById(professorId);
        verify(lessonDao).findByProfessorId(professorId);
    }

    @Test
    void createShouldAddLessonToDBIfArgumentsIsLessonRequest() {
        Lesson lesson = Lesson.builder()
                .withTeacher(Professor.builder().withId(1L).build())
                .withGroup(Group.builder().withId(1L).build())
                .withTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 00, 00))
                .build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(0L);
        lessonRequest.setCourseId(0L);
        lessonRequest.setGroupId(0L);
        lessonRequest.setFormOfLessonId(0L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        when(lessonDao.save(lesson)).thenReturn(lesson);

        lessonService.create(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).save(lesson);
    }

    @Test
    void createShouldAddLessonToDBIfArgumentsIsLessonRequestWithManyAdditionalParams() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        Course course = Course.builder().withId(1L).build();
        Group group = Group.builder().withId(1L).build();
        Professor professor = Professor.builder().withId(1L).build();
        FormOfLesson formOfLesson = FormOfLesson.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(1L);
        lessonRequest.setFormOfLessonId(1L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        when(lessonDao.save(lesson)).thenReturn(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(groupDao.findById(1L)).thenReturn(Optional.of(group));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));
        when(formOfLessonDao.findById(1L)).thenReturn(Optional.of(formOfLesson));

        lessonService.create(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).save(lesson);
        verify(lessonDao, times(6)).findById(1L);
        verify(courseDao).findById(1L);
        verify(groupDao).findById(1L);
        verify(professorDao).findById(1L);
        verify(formOfLessonDao).findById(1L);
    }

    @Test
    void findByIdShouldReturnLessonIfArgumentIsLessonId() {
        long lessonId = 1;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId(1L).build()));

        lessonService.findById(lessonId);

        verify(lessonDao).findById(lessonId);
    }

    @Test
    void findByIdShouldThrowExceptionIfLessonNotExist() {
        long lessonId = 1;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.findById(lessonId)).hasMessage("There no lesson with id: 1");

        verify(lessonDao).findById(lessonId);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(lessonDao.count()).thenReturn(11L);
        when(lessonDao.findAll(2, 5)).thenReturn(Arrays.asList(Lesson.builder().withId(1L).build()));

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseNoArguments() {
        when(lessonDao.findAll()).thenReturn(Arrays.asList(Lesson.builder().withId(1L).build()));

        lessonService.findAll();

        verify(lessonDao).findAll();
    }

    @Test
    void editShouldEditDataOfLessonIfArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(0L);
        lessonRequest.setCourseId(0L);
        lessonRequest.setGroupId(0L);
        lessonRequest.setFormOfLessonId(0L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
    }

    @Test
    void editShouldEditDataOfLessonAndOverLessonParamsIfArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        Course course = Course.builder().withId(1L).build();
        Group group = Group.builder().withId(1L).build();
        Professor professor = Professor.builder().withId(1L).build();
        FormOfLesson formOfLesson = FormOfLesson.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(1L);
        lessonRequest.setFormOfLessonId(1L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(groupDao.findById(1L)).thenReturn(Optional.of(group));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));
        when(formOfLessonDao.findById(1L)).thenReturn(Optional.of(formOfLesson));

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao, times(6)).findById(1L);
        verify(courseDao).findById(1L);
        verify(groupDao).findById(1L);
        verify(professorDao).findById(1L);
        verify(formOfLessonDao).findById(1L);
    }

    @Test
    void editShouldThrowExceptionIfLessonWithAppointedIdNotExistArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(1L);
        lessonRequest.setFormOfLessonId(1L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no lesson with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao).findById(1L);
    }

    @Test
    void editShouldThrowExceptionIfCourseWithAppointedIdNotExistArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(1L);
        lessonRequest.setFormOfLessonId(1L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no course with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao).findById(1L);
        verify(courseDao).findById(1L);
    }

    @Test
    void editShouldThrowExceptionIfGroupWithAppointedIdNotExistArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        Course course = Course.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(1L);
        lessonRequest.setFormOfLessonId(1L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(groupDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no group with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao, times(3)).findById(1L);
        verify(courseDao).findById(1L);
        verify(groupDao).findById(1L);
    }

    @Test
    void editShouldThrowExceptionIfProfessorWithAppointedIdNotExistArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        Course course = Course.builder().withId(1L).build();
        Group group = Group.builder().withId(1L).build();
        Professor professor = Professor.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(1L);
        lessonRequest.setFormOfLessonId(1L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(groupDao.findById(1L)).thenReturn(Optional.of(group));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));
        when(formOfLessonDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no form of lesson with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao, times(6)).findById(1L);
        verify(courseDao).findById(1L);
        verify(groupDao).findById(1L);
        verify(professorDao).findById(1L);
        verify(formOfLessonDao).findById(1L);
    }

    @Test
    void editShouldThrowExceptionIfFormOfLessonWithAppointedIdNotExistArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId(1L).build();
        Course course = Course.builder().withId(1L).build();
        Group group = Group.builder().withId(1L).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(1L);
        lessonRequest.setFormOfLessonId(1L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(groupDao.findById(1L)).thenReturn(Optional.of(group));
        when(professorDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no professor with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao, times(4)).findById(1L);
        verify(courseDao).findById(1L);
        verify(groupDao).findById(1L);
        verify(professorDao).findById(1L);
    }

    @Test
    void editShouldValidateCompatibilityCourseAndProfessorWhenChangeTeacherIfArgumentNewLessonRequest() {
        Course course = Course.builder().withId(1L).build();
        Professor professor = Professor.builder().withId(1L).build();
        Lesson lesson = Lesson.builder().withId(1L).withCourse(course).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(0L);
        lessonRequest.setGroupId(0L);
        lessonRequest.setFormOfLessonId(0L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao, times(2)).findById(1L);
        verify(professorDao).findById(1L);
    }

    @Test
    void editShouldValidateCompatibilityCourseAndProfessorWhenChangeCourseIfArgumentNewLessonRequest() {
        Course course = Course.builder().withId(1L).build();
        Professor professor = Professor.builder().withId(1L).build();
        Lesson lesson = Lesson.builder().withId(1L).withTeacher(professor).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setTeacherId(1L);
        lessonRequest.setCourseId(1L);
        lessonRequest.setGroupId(0L);
        lessonRequest.setFormOfLessonId(0L);

        when(lessonMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);
        when(lessonDao.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
        verify(lessonDao, times(4)).findById(1L);
        verify(courseDao).findById(1L);
        verify(professorDao).findById(1L);
    }

    @Test
    void deleteShouldDeleteDataOfLessonIfArgumentIsLessonId() {
        long lessonId = 1;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId(lessonId).build()));
        when(lessonDao.deleteById(lessonId)).thenReturn(true);

        lessonService.deleteById(lessonId);

        verify(lessonDao).findById(lessonId);
        verify(lessonDao).deleteById(lessonId);
    }

    @Test
    void formTimeTableForGroupShouldThrowExceptionIfGroupDontExist() {
        long groupId = 200;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.formTimeTableForGroup(groupId)).hasMessage("There no group with id: 200");

        verify(groupDao).findById(groupId);
    }

    @Test
    void findByStudentIdShouldShouldThrowExceptionIfStudentDontExist() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.formTimeTableForStudent(studentId))
                .hasMessage("There no student with id: 1");

        verify(studentDao).findById(studentId);
    }

    @Test
    void findByStudentIdShouldShouldThrowExceptionIfStudentNotAMemberOfGroup() {
        long studentId = 1;
        Student student = Student.builder().withId(studentId).withGroup(Group.builder().withId(0L).build()).build();

        when(studentDao.findById(studentId)).thenReturn(Optional.of(student));

        assertThatThrownBy(() -> lessonService.formTimeTableForStudent(studentId))
                .hasMessage("Student with id: 1 not a member of any group");

        verify(studentDao).findById(studentId);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseIfArgumentIsPageNumberCountOfElementIsSharesEntirelyOnCountElementPerPage() {
        String pageNumber = "2";

        when(lessonDao.count()).thenReturn(10L);
        when(lessonDao.findAll(2, 5)).thenReturn(Arrays.asList(Lesson.builder().withId(1L).build()));

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentNotNumber() {
        String pageNumber = "a";

        when(lessonDao.count()).thenReturn(11L);

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(1, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentIsNull() {
        String pageNumber = null;

        when(lessonDao.count()).thenReturn(11L);

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(1, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentNegativeCount() {
        String pageNumber = "-1";

        when(lessonDao.count()).thenReturn(11L);

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(1, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromMaxPAgeIfArgumentIsMoreThatMaxPage() {
        String pageNumber = "100";

        when(lessonDao.count()).thenReturn(11L);

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(3, 5);
    }

    @Test
    void deleteShouldDoNothingIfArgumentLessonDontExist() {
        long lessonId = 1;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.empty());

        lessonService.deleteById(lessonId);

        verify(lessonDao).findById(lessonId);
    }

}
