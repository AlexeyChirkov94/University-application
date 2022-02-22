package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.FormOfLessonRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.repository.StudentRepository;
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
    LessonRepository lessonRepository;

    @Mock
    FormOfLessonRepository formOfLessonRepository;

    @Mock
    ProfessorRepository professorRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    StudentRepository studentRepository;

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

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(2L).build()));
        when(lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(groupId)).thenReturn(lessons);

        lessonService.formTimeTableForGroup(groupId);

        verify(groupRepository).findById(groupId);
        verify(lessonRepository).findAllByGroupIdOrderByTimeOfStartLesson(groupId);
    }

    @Test
    void findByStudentIdShouldReturnListOfLessonResponsesIfArgumentsIsGroupId() {
        long studentId = 1;
        long groupId = 2;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);
        Student student = Student.builder().withId(studentId).withGroup(Group.builder().withId(groupId).build()).build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(groupId)).thenReturn(lessons);

        lessonService.formTimeTableForStudent(studentId);

        verify(studentRepository).findById(studentId);
        verify(lessonRepository).findAllByGroupIdOrderByTimeOfStartLesson(groupId);
    }

    @Test
    void findByProfessorIdShouldReturnListOfLessonResponsesIfArgumentsIsProfessorId() {
        long professorId = 2;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> lessons = Arrays.asList(lesson1, lesson2);

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(2L).build()));
        when(lessonRepository.findAllByTeacherIdOrderByTimeOfStartLesson(professorId)).thenReturn(lessons);

        lessonService.formTimeTableForProfessor(professorId);

        verify(professorRepository).findById(professorId);
        verify(lessonRepository).findAllByTeacherIdOrderByTimeOfStartLesson(professorId);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        lessonService.create(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(formOfLessonRepository.findById(1L)).thenReturn(Optional.of(formOfLesson));

        lessonService.create(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository, times(4)).findById(1L);
        verify(courseRepository).findById(1L);
        verify(groupRepository).findById(1L);
        verify(professorRepository).findById(1L);
        verify(formOfLessonRepository).findById(1L);
    }

    @Test
    void findByIdShouldReturnLessonIfArgumentIsLessonId() {
        long lessonId = 1;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId(1L).build()));

        lessonService.findById(lessonId);

        verify(lessonRepository).findById(lessonId);
    }

    @Test
    void findByIdShouldThrowExceptionIfLessonNotExist() {
        long lessonId = 1;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.findById(lessonId)).hasMessage("There no lesson with id: 1");

        verify(lessonRepository).findById(lessonId);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(lessonRepository.count()).thenReturn(11L);
        when(lessonRepository.findAll(PageRequest.of(1, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Lesson.builder().withId(1L).build())));

        lessonService.findAll(pageNumber);

        verify(lessonRepository).count();
        verify(lessonRepository).findAll(PageRequest.of(1, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseNoArguments() {
        when(lessonRepository.findAll(Sort.by("id"))).thenReturn(Arrays.asList(Lesson.builder().withId(1L).build()));

        lessonService.findAll();

        verify(lessonRepository).findAll(Sort.by("id"));
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(formOfLessonRepository.findById(1L)).thenReturn(Optional.of(formOfLesson));

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository, times(4)).findById(1L);
        verify(courseRepository).findById(1L);
        verify(groupRepository).findById(1L);
        verify(professorRepository).findById(1L);
        verify(formOfLessonRepository).findById(1L);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no lesson with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository).findById(1L);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no course with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository).findById(1L);
        verify(courseRepository).findById(1L);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no group with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository, times(2)).findById(1L);
        verify(courseRepository).findById(1L);
        verify(groupRepository).findById(1L);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(formOfLessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no form of lesson with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository, times(4)).findById(1L);
        verify(courseRepository).findById(1L);
        verify(groupRepository).findById(1L);
        verify(professorRepository).findById(1L);
        verify(formOfLessonRepository).findById(1L);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(professorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.edit(lessonRequest)).hasMessage("There no professor with id: 1");

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository, times(3)).findById(1L);
        verify(courseRepository).findById(1L);
        verify(groupRepository).findById(1L);
        verify(professorRepository).findById(1L);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository).findById(1L);
        verify(professorRepository).findById(1L);
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
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(lesson));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));

        lessonService.edit(lessonRequest);

        verify(lessonMapper).mapDtoToEntity(lessonRequest);
        verify(lessonRepository).save(lesson);
        verify(lessonRepository, times(2)).findById(1L);
        verify(courseRepository).findById(1L);
        verify(professorRepository).findById(1L);
    }

    @Test
    void deleteShouldDeleteDataOfLessonIfArgumentIsLessonId() {
        long lessonId = 1;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(Lesson.builder().withId(lessonId).build()));
        doNothing().when(lessonRepository).deleteById(lessonId);

        lessonService.deleteById(lessonId);

        verify(lessonRepository).findById(lessonId);
        verify(lessonRepository).deleteById(lessonId);
    }

    @Test
    void formTimeTableForGroupShouldThrowExceptionIfGroupDontExist() {
        long groupId = 200;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.formTimeTableForGroup(groupId)).hasMessage("There no group with id: 200");

        verify(groupRepository).findById(groupId);
    }

    @Test
    void findByStudentIdShouldShouldThrowExceptionIfStudentDontExist() {
        long studentId = 1;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.formTimeTableForStudent(studentId))
                .hasMessage("There no student with id: 1");

        verify(studentRepository).findById(studentId);
    }

    @Test
    void findByStudentIdShouldShouldThrowExceptionIfStudentNotAMemberOfGroup() {
        long studentId = 1;
        Student student = Student.builder().withId(studentId).build();

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        assertThatThrownBy(() -> lessonService.formTimeTableForStudent(studentId))
                .hasMessage("Student with id: 1 not a member of any group");

        verify(studentRepository).findById(studentId);
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseIfArgumentIsPageNumberCountOfElementIsSharesEntirelyOnCountElementPerPage() {
        String pageNumber = "2";

        when(lessonRepository.count()).thenReturn(10L);
        when(lessonRepository.findAll(PageRequest.of(1, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Lesson.builder().withId(1L).build())));

        lessonService.findAll(pageNumber);

        verify(lessonRepository).count();
        verify(lessonRepository).findAll(PageRequest.of(1, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentNotNumber() {
        String pageNumber = "a";

        when(lessonRepository.count()).thenReturn(11L);
        when(lessonRepository.findAll(PageRequest.of(0, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Lesson.builder().withId(1L).build())));

        lessonService.findAll(pageNumber);

        verify(lessonRepository).count();
        verify(lessonRepository).findAll(PageRequest.of(0, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentIsNull() {
        String pageNumber = null;

        when(lessonRepository.count()).thenReturn(11L);
        when(lessonRepository.findAll(PageRequest.of(0, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Lesson.builder().withId(1L).build())));

        lessonService.findAll(pageNumber);

        verify(lessonRepository).count();
        verify(lessonRepository).findAll(PageRequest.of(0, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromDefaultPageIfArgumentNegativeCount() {
        String pageNumber = "-1";

        when(lessonRepository.count()).thenReturn(11L);
        when(lessonRepository.findAll(PageRequest.of(0, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Lesson.builder().withId(1L).build())));

        lessonService.findAll(pageNumber);

        verify(lessonRepository).count();
        verify(lessonRepository).findAll(PageRequest.of(0, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfLessonResponseFromMaxPAgeIfArgumentIsMoreThatMaxPage() {
        String pageNumber = "100";

        when(lessonRepository.count()).thenReturn(11L);
        when(lessonRepository.findAll(PageRequest.of(2, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Lesson.builder().withId(1L).build())));

        lessonService.findAll(pageNumber);

        verify(lessonRepository).count();
        verify(lessonRepository).findAll(PageRequest.of(2, 5, Sort.by("id")));
    }

    @Test
    void deleteShouldDoNothingIfArgumentLessonDontExist() {
        long lessonId = 1;

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        lessonService.deleteById(lessonId);

        verify(lessonRepository).findById(lessonId);
    }

}
