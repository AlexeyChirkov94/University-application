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
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.LessonRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.LessonResponseMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
    private LessonDao lessonDao;

    @Mock
    private FormOfLessonDao formOfLessonDao;

    @Mock
    private ProfessorDao professorDao;

    @Mock
    private CourseDao courseDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private LessonRequestMapper lessonRequestMapper;

    @Mock
    private LessonResponseMapper lessonResponseMapper;


    @InjectMocks
    private LessonServiceImpl lessonService;

    @Test
    void formTimeTableForGroupShouldReturnListOfLessonResponsesIfArgumentsIsGroupId() {
        long groupId = 2;
        Lesson lesson1 = Lesson.builder().withId((long)1).build();
        Lesson lesson2 = Lesson.builder().withId((long)2).build();
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId((long)2).build()));
        when(lessonDao.formTimeTableForGroup(groupId)).thenReturn(lessons);

        lessonService.formTimeTableForGroup(groupId);

        verify(groupDao).findById(groupId);
        verify(lessonDao).formTimeTableForGroup(groupId);
    }

    @Test
    void formTimeTableForProfessorShouldReturnListOfLessonResponsesIfArgumentsIsProfessorId() {
        long professorId = 2;
        Lesson lesson1 = Lesson.builder().withId((long)1).build();
        Lesson lesson2 = Lesson.builder().withId((long)2).build();
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId((long)2).build()));
        when(lessonDao.formTimeTableForProfessor(professorId)).thenReturn(lessons);

        lessonService.formTimeTableForProfessor(professorId);

        verify(professorDao).findById(professorId);
        verify(lessonDao).formTimeTableForProfessor(professorId);
    }

    @Test
    void changeFormOfLessonShouldChangeFormOfLessonIfArgumentsIsLessonIdAndFormOfLessonId() {
        long lessonId = 1;
        long formOfLessonId = 2;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId((long)1).build()));
        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.of(FormOfLesson.builder().withId((long)2).build()));
        doNothing().when(lessonDao).changeFormOfLesson(lessonId, formOfLessonId);

        lessonService.changeFormOfLesson(lessonId, formOfLessonId);

        verify(lessonDao).findById(lessonId);
        verify(formOfLessonDao).findById(formOfLessonId);
        verify(lessonDao).changeFormOfLesson(lessonId, formOfLessonId);
    }

    @Test
    void changeTeacherShouldChangeTeacherIfArgumentsIsLessonIdAndProfessorId() {
        long lessonId = 1;
        long professorId = 2;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId((long)1).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId((long)2).build()));
        doNothing().when(lessonDao).changeTeacher(lessonId, professorId);

        lessonService.changeTeacher(lessonId, professorId);

        verify(lessonDao).findById(lessonId);
        verify(professorDao).findById(professorId);
        verify(lessonDao).changeTeacher(lessonId, professorId);
    }

    @Test
    void changeCourseShouldChangeCourseIfArgumentsIsLessonIdAndCourseId() {
        long lessonId = 1;
        long courseId = 2;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId((long)1).build()));
        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId((long)2).build()));
        doNothing().when(lessonDao).changeCourse(lessonId, courseId);

        lessonService.changeCourse(lessonId, courseId);

        verify(lessonDao).findById(lessonId);
        verify(courseDao).findById(courseId);
        verify(lessonDao).changeCourse(lessonId, courseId);
    }

    @Test
    void registerShouldAddLessonToDBIfArgumentsIsLessonRequest() {
        Lesson lesson = Lesson.builder()
                .withTeacher(Professor.builder().withId((long)1).build())
                .withGroup(Group.builder().withId((long)1).build())
                .withTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 00, 00))
                .build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId((long)1);
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId((long)1);
        List<Lesson> lessonsOfGroup = Collections.emptyList();
        List<Lesson> lessonsOfProfessor = Collections.emptyList();

        when(lessonDao.formTimeTableForGroup(lesson.getGroup().getId())).thenReturn(lessonsOfGroup);
        when(lessonDao.formTimeTableForProfessor(lesson.getTeacher().getId())).thenReturn(lessonsOfProfessor);
        when(lessonRequestMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        when(lessonDao.save(lesson)).thenReturn(lesson);
        when(lessonResponseMapper.mapEntityToDto(lesson)).thenReturn(lessonResponse);

        lessonService.register(lessonRequest);

        verify(lessonDao).formTimeTableForGroup(lesson.getGroup().getId());
        verify(lessonDao).formTimeTableForProfessor(lesson.getTeacher().getId());
        verify(lessonRequestMapper, times(2)).mapDtoToEntity(lessonRequest);
        verify(lessonDao).save(lesson);
        verify(lessonResponseMapper).mapEntityToDto(lesson);
    }

    @Test
    void findByIdShouldReturnOptionalOfLessonIfArgumentIsLessonId() {
        long lessonId = 1;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId((long)1).build()));

        lessonService.findById(lessonId);

        verify(lessonDao).findById(lessonId);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(lessonDao.count()).thenReturn((long)11);
        when(lessonDao.findAll(2, 5)).thenReturn(Arrays.asList(Lesson.builder().withId((long)1).build()));

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(2, 5);
    }

    @Test
    void editShouldEditDataOfLessonIfArgumentNewLessonRequest() {
        Lesson lesson = Lesson.builder().withId((long)1).build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId((long)1);

        when(lessonRequestMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);
        doNothing().when(lessonDao).update(lesson);

        lessonService.edit(lessonRequest);

        verify(lessonRequestMapper).mapDtoToEntity(lessonRequest);
        verify(lessonDao).update(lesson);
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
    void changeFormOfLessonShouldThrowExceptionIfLessonDontExist() {
        long lessonId = 100;
        long formOfLessonId = 2;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.changeFormOfLesson(lessonId, formOfLessonId)).hasMessage("There no lesson with id: 100");

        verify(lessonDao).findById(lessonId);
    }

    @Test
    void changeFormOfLessonShouldThrowExceptionIfFormOfLessonDontExist() {
        long lessonId = 1;
        long formOfLessonId = 200;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId((long)1).build()));
        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.changeFormOfLesson(lessonId, formOfLessonId)).hasMessage("There no form of lesson with id: 200");

        verify(lessonDao).findById(lessonId);
        verify(formOfLessonDao).findById(formOfLessonId);
    }

    @Test
    void changeTeacherShouldThrowExceptionIfProfessorDontExist() {
        long lessonId = 1;
        long professorId = 100;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId((long)1).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.changeTeacher(lessonId, professorId)).hasMessage("There no professor with id: 100");

        verify(lessonDao).findById(lessonId);
        verify(professorDao).findById(professorId);
    }

    @Test
    void changeCourseShouldThrowExceptionIfCourseDontExist() {
        long lessonId = 1;
        long courseId = 200;

        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId((long)1).build()));
        when(courseDao.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.changeCourse(lessonId, courseId)).hasMessage("There no course with id: 200");

        verify(lessonDao).findById(lessonId);
        verify(courseDao).findById(courseId);
    }

    @Test
    void formTimeTableForGroupShouldThrowExceptionIfGroupDontExist() {
        long groupId = 200;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.formTimeTableForGroup(groupId)).hasMessage("There no group with id: 200");

        verify(groupDao).findById(groupId);
    }

    @Test
    void registerShouldThrowExceptionIfLessonCantBeCreatedBecauseGroupAlreadyHaveLessonOnThisTime() {
        Lesson lesson = Lesson.builder()
                .withTeacher(Professor.builder().withId((long)1).build())
                .withGroup(Group.builder().withId((long)1).build())
                .withTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 00, 00))
                .build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId((long)1);
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId((long)1);
        List<Lesson> lessonsOfGroup = Arrays.asList(Lesson.builder().withTimeOfStartLesson(lesson.getTimeOfStartLesson()).build());
        List<Lesson> lessonsOfProfessor = Collections.emptyList();

        when(lessonDao.formTimeTableForGroup(lesson.getGroup().getId())).thenReturn(lessonsOfGroup);
        when(lessonDao.formTimeTableForProfessor(lesson.getTeacher().getId())).thenReturn(lessonsOfProfessor);
        when(lessonRequestMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);

        assertThatThrownBy(() -> lessonService.register(lessonRequest)).hasMessage("Lesson can`t be appointed on this time");

        verify(lessonDao).formTimeTableForGroup(lesson.getGroup().getId());
        verify(lessonDao).formTimeTableForProfessor(lesson.getTeacher().getId());
        verify(lessonRequestMapper).mapDtoToEntity(lessonRequest);
    }

    @Test
    void registerShouldThrowExceptionIfLessonCantBeCreatedBecauseProfessorAlreadyHaveLessonOnThisTime() {
        Lesson lesson = Lesson.builder()
                .withTeacher(Professor.builder().withId((long)1).build())
                .withGroup(Group.builder().withId((long)1).build())
                .withTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 00, 00))
                .build();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId((long)1);
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId((long)1);
        List<Lesson> lessonsOfGroup = Collections.emptyList();
        List<Lesson> lessonsOfProfessor = Arrays.asList(Lesson.builder().withTimeOfStartLesson(lesson.getTimeOfStartLesson()).build());

        when(lessonDao.formTimeTableForGroup(lesson.getGroup().getId())).thenReturn(lessonsOfGroup);
        when(lessonDao.formTimeTableForProfessor(lesson.getTeacher().getId())).thenReturn(lessonsOfProfessor);
        when(lessonRequestMapper.mapDtoToEntity(lessonRequest)).thenReturn(lesson);

        assertThatThrownBy(() -> lessonService.register(lessonRequest)).hasMessage("Lesson can`t be appointed on this time");

        verify(lessonDao).formTimeTableForGroup(lesson.getGroup().getId());
        verify(lessonDao).formTimeTableForProfessor(lesson.getTeacher().getId());
        verify(lessonRequestMapper).mapDtoToEntity(lessonRequest);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseIfArgumentIsPageNumberCountOfElementIsSharesEntirelyOnCountElementPerPage() {
        String pageNumber = "2";

        when(lessonDao.count()).thenReturn((long)10);
        when(lessonDao.findAll(2, 5)).thenReturn(Arrays.asList(Lesson.builder().withId((long)1).build()));

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentNotNumber() {
        String pageNumber = "a";

        when(lessonDao.count()).thenReturn((long)11);

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(1, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentIsNull() {
        String pageNumber = null;

        when(lessonDao.count()).thenReturn((long)11);

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(1, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentNegativeCount() {
        String pageNumber = "-1";

        when(lessonDao.count()).thenReturn((long)11);

        lessonService.findAll(pageNumber);

        verify(lessonDao).count();
        verify(lessonDao).findAll(1, 5);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromMaxPAgeIfArgumentIsMoreThatMaxPage() {
        String pageNumber = "100";

        when(lessonDao.count()).thenReturn((long)11);

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
