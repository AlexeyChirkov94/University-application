package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.FormOfLessonRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertLessons;

class LessonRepositoryImplTest {

    ApplicationContext context;
    LessonRepository lessonRepository;
    GroupRepository groupRepository;
    Group groupForTest;
    CourseRepository courseRepository;
    Course courseForTest;
    ProfessorRepository professorRepository;
    Professor professorForTest;
    FormOfLessonRepository formOfLessonRepository;
    FormOfLesson formOfLessonForTest;
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd_HH:mm:ss.SSS";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        lessonRepository = context.getBean(LessonRepository.class);

        groupRepository = context.getBean(GroupRepository.class);
        groupForTest = Group.builder().withId(0L).build();

        courseRepository = context.getBean(CourseRepository.class);
        courseForTest = Course.builder().withId(0L).build();

        professorRepository = context.getBean(ProfessorRepository.class);
        professorForTest = Professor.builder().withId(0L).build();

        formOfLessonRepository = context.getBean(FormOfLessonRepository.class);
        formOfLessonForTest = FormOfLesson.builder().withId(0L).build();
    }

    @Test
    void createAndReadShouldAddNewLessonToDatabaseIfArgumentIsLesson(){
        Lesson addingLesson = Lesson.builder()
                .withCourse(Course.builder().withId(1L).withName("Russia History").build())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(Group.builder().withId(1L).withName("History Group #1").build())
                .withTeacher(Professor.builder().withId(7L).withFirstName("Ivan").withLastName("Petrov").build())
                .withFormOfLesson(FormOfLesson.builder().withId(1L).withName("lecture").build())
                .build();
        lessonRepository.save(addingLesson);
        Lesson readingLesson = lessonRepository.findById(6L).get();

        assertLessons(readingLesson, addingLesson);
    }

    @Test
    void createAndReadShouldAddNewLessonToDatabaseIfArgumentIsLessonWithNotAppointedDateTime(){
        Lesson addingLesson = Lesson.builder()
                .withCourse(Course.builder().withId(1L).withName("Russia History").build())
                .withGroup(Group.builder().withId(1L).withName("History Group #1").build())
                .withTeacher(Professor.builder().withId(7L).withFirstName("Ivan").withLastName("Petrov").build())
                .withFormOfLesson(FormOfLesson.builder().withId(1L).withName("lecture").build())
                .build();
        lessonRepository.save(addingLesson);
        Lesson readingLesson = lessonRepository.findById(6L).get();

        assertLessons(readingLesson, addingLesson);
    }

    @Test
    void createAndReadShouldAddListOfNewLessonsToDatabaseIfArgumentIsListOfLessons(){
        List<Lesson> addingLessonEntities = Arrays.asList (Lesson.builder()
                .withCourse(Course.builder().withId(1L).withName("Russia History").build())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(Group.builder().withId(1L).withName("History Group #1").build())
                .withTeacher(Professor.builder().withId(7L).withFirstName("Ivan").withLastName("Petrov").build())
                .withFormOfLesson(FormOfLesson.builder().withId(1L).withName("lecture").build())
                .build());
        lessonRepository.saveAll(addingLessonEntities);
        List<Lesson> readingLessonEntities = Arrays.asList(lessonRepository.findById(6L).get());

        assertLessons(readingLessonEntities, addingLessonEntities);
    }

    @Test
    void updateShouldUpdateDataOfLessonIfArgumentIsLesson(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(professorRepository.findById(7L).get())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build();
        lessonRepository.save(expected);
        Lesson actual = lessonRepository.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfLessonIfArgumentIsListOfLesson(){
        List<Lesson> expected = Arrays.asList(Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(professorRepository.findById(7L).get())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build());
        lessonRepository.saveAll(expected);
        List<Lesson> actual = Arrays.asList(lessonRepository.findById(1L).get());

        assertLessons(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfLessonIfArgumentIsIdOfLesson(){
        Optional<Lesson> expected = Optional.empty();
        lessonRepository.deleteById(1L);
        Optional<Lesson> actual = lessonRepository.findById(1L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByGroupIdShouldReturnListOfLessonForGroupIfArgumentIsIdOfGroup(){
        List<Lesson> expected = Arrays.asList(lessonRepository.findById(1L).get(),
                lessonRepository.findById(2L).get(),
                lessonRepository.findById(3L).get());
        List<Lesson> actual = lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(1);

        assertLessons(actual, expected);
    }

    @Test
    void findByProfessorIdShouldReturnListOfLessonForProfessorIfArgumentIsIdOfProfessor(){
        List<Lesson> expected = Arrays.asList(lessonRepository.findById(1L).get(),
                lessonRepository.findById(2L).get());
        List<Lesson> actual = lessonRepository.findAllByTeacherIdOrderByTimeOfStartLesson(7);

        assertLessons(actual, expected);
    }

    @Test
    void findByCourseIdIdShouldReturnListOfLessonForProfessorIfArgumentIsIdOfProfessor(){
        List<Lesson> expected = Arrays.asList(lessonRepository.findById(1L).get(),
                lessonRepository.findById(2L).get());
        List<Lesson> actual = lessonRepository.findAllByCourseIdOrderByTimeOfStartLesson(1);

        assertLessons(actual, expected);
    }

    @Test
    void findByFormOfLessonIdIdShouldReturnListOfLessonForProfessorIfArgumentIsIdOfProfessor(){
        List<Lesson> expected = Arrays.asList(lessonRepository.findById(2L).get(),
                lessonRepository.findById(5L).get());
        List<Lesson> actual = lessonRepository.findAllByFormOfLessonIdOrderByTimeOfStartLesson(2);

        assertLessons(actual, expected);
    }

    @Test
    void changeFormOfLessonShouldChangeFormOfLessonIfArgumentIsIdOfLessonAndIdOfNewFormOfLesson(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(professorRepository.findById(7L).get())
                .withFormOfLesson(formOfLessonRepository.findById(2L).get())
                .build();

        lessonRepository.changeFormOfLesson(1, 2);

        Lesson actual = lessonRepository.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeFormOfLessonShouldChangeFormOfLessonIfArgumentIsIdOfLessonAndIdOfNewFormOfLesson(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(professorRepository.findById(7L).get())
                .build();

        lessonRepository.removeFormOfLessonFromLesson(1);

        Lesson actual = lessonRepository.findById(1L).get();


        assertThat(actual.getCourse().getId()).isEqualTo(expected.getCourse().getId());
        assertThat(actual.getCourse().getName()).isEqualTo(expected.getCourse().getName());
        assertThat(actual.getTimeOfStartLesson()).isEqualTo(expected.getTimeOfStartLesson());
        assertThat(actual.getGroup().getId()).isEqualTo(expected.getGroup().getId());
        assertThat(actual.getGroup().getName()).isEqualTo(expected.getGroup().getName());
        assertThat(actual.getTeacher().getId()).isEqualTo(expected.getTeacher().getId());
        assertThat(actual.getTeacher().getFirstName()).isEqualTo(expected.getTeacher().getFirstName());
        assertThat(actual.getTeacher().getLastName()).isEqualTo(expected.getTeacher().getLastName());
        assertThat(actual.getFormOfLesson()).isNull();
    }

    @Test
    void changeTeacherShouldChangeTeacherOfLessonIfArgumentIsIdOfLessonAndIdOfNewProfessor(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(professorRepository.findById(9L).get())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build();

        lessonRepository.changeTeacher(1, 9);

        Lesson actual = lessonRepository.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeTeacherShouldChangeTeacherOfLessonIfArgumentIsIdOfLessonAndIdOfNewProfessor(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(Professor.builder().withId(0L).build())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build();

        lessonRepository.removeTeacherFromLesson(1);

        Lesson actual = lessonRepository.findById(1L).get();

        assertThat(actual.getCourse().getId()).isEqualTo(expected.getCourse().getId());
        assertThat(actual.getCourse().getName()).isEqualTo(expected.getCourse().getName());
        assertThat(actual.getTimeOfStartLesson()).isEqualTo(expected.getTimeOfStartLesson());
        assertThat(actual.getGroup().getId()).isEqualTo(expected.getGroup().getId());
        assertThat(actual.getGroup().getName()).isEqualTo(expected.getGroup().getName());
        assertThat(actual.getTeacher()).isNull();
        assertThat(actual.getFormOfLesson().getId()).isEqualTo(expected.getFormOfLesson().getId());
        assertThat(actual.getFormOfLesson().getName()).isEqualTo(expected.getFormOfLesson().getName());
    }

    @Test
    void changeCourseShouldChangeCourseOfLessonIfArgumentIsIdOfLessonAndIdOfNewCourse(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(2L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(professorRepository.findById(7L).get())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build();

        lessonRepository.changeCourse(1, 2);

        Lesson actual = lessonRepository.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeCourseShouldChangeCourseOfLessonIfArgumentIsIdOfLessonAndIdOfNewCourse(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(Course.builder().withId(0L).build())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(1L).get())
                .withTeacher(professorRepository.findById(7L).get())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build();

        lessonRepository.removeCourseFromLesson(1);

        Lesson actual = lessonRepository.findById(1L).get();

        assertThat(actual.getCourse()).isNull();
        assertThat(actual.getTimeOfStartLesson()).isEqualTo(expected.getTimeOfStartLesson());
        assertThat(actual.getGroup().getId()).isEqualTo(expected.getGroup().getId());
        assertThat(actual.getGroup().getName()).isEqualTo(expected.getGroup().getName());
        assertThat(actual.getTeacher().getId()).isEqualTo(expected.getTeacher().getId());
        assertThat(actual.getTeacher().getFirstName()).isEqualTo(expected.getTeacher().getFirstName());
        assertThat(actual.getTeacher().getLastName()).isEqualTo(expected.getTeacher().getLastName());
        assertThat(actual.getFormOfLesson().getId()).isEqualTo(expected.getFormOfLesson().getId());
        assertThat(actual.getFormOfLesson().getName()).isEqualTo(expected.getFormOfLesson().getName());
    }

    @Test
    void changeGroupShouldChangeGroupOfLessonIfArgumentIsIdOfLessonAndIdOfNewGroup(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(groupRepository.findById(2L).get())
                .withTeacher(professorRepository.findById(7L).get())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build();

        lessonRepository.changeGroup(1, 2);

        Lesson actual = lessonRepository.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeGroupShouldChangeGroupOfLessonIfArgumentIsIdOfLessonAndIdOfNewGroup(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseRepository.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_10:00:00.000", FORMATTER))
                .withGroup(Group.builder().withId(0L).build())
                .withTeacher(professorRepository.findById(7L).get())
                .withFormOfLesson(formOfLessonRepository.findById(1L).get())
                .build();

        lessonRepository.removeGroupFromLesson(1);

        Lesson actual = lessonRepository.findById(1L).get();

        assertThat(actual.getCourse().getId()).isEqualTo(expected.getCourse().getId());
        assertThat(actual.getCourse().getName()).isEqualTo(expected.getCourse().getName());
        assertThat(actual.getTimeOfStartLesson()).isEqualTo(expected.getTimeOfStartLesson());
        assertThat(actual.getGroup()).isNull();
        assertThat(actual.getTeacher().getId()).isEqualTo(expected.getTeacher().getId());
        assertThat(actual.getTeacher().getFirstName()).isEqualTo(expected.getTeacher().getFirstName());
        assertThat(actual.getTeacher().getLastName()).isEqualTo(expected.getTeacher().getLastName());
        assertThat(actual.getFormOfLesson().getId()).isEqualTo(expected.getFormOfLesson().getId());
        assertThat(actual.getFormOfLesson().getName()).isEqualTo(expected.getFormOfLesson().getName());
    }
}
