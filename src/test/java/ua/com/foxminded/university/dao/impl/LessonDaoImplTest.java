package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
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

class LessonDaoImplTest {

    ApplicationContext context;
    LessonDao lessonDao;
    GroupDao groupDao;
    Group groupForTest;
    CourseDao courseDao;
    Course courseForTest;
    ProfessorDao professorDao;
    Professor professorForTest;
    FormOfLessonDao formOfLessonDao;
    FormOfLesson formOfLessonForTest;
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd_HH:mm:ss.SSS";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        lessonDao = context.getBean(LessonDaoImpl.class);

        groupDao = context.getBean(GroupDaoImpl.class);
        groupForTest = Group.builder().withId(0L).build();

        courseDao = context.getBean(CourseDaoImpl.class);
        courseForTest = Course.builder().withId(0L).build();

        professorDao = context.getBean(ProfessorDaoImpl.class);
        professorForTest = Professor.builder().withId(0L).build();

        formOfLessonDao = context.getBean(FormOfLessonDaoImpl.class);
        formOfLessonForTest = FormOfLesson.builder().withId(0L).build();
    }

    @Test
    void createAndReadShouldAddNewLessonToDatabaseIfArgumentIsLesson(){
        Lesson addingLesson = Lesson.builder()
                .withCourse(courseForTest)
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupForTest)
                .withTeacher(professorForTest)
                .withFormOfLesson(formOfLessonForTest)
                .build();
        lessonDao.save(addingLesson);
        Lesson readingLesson = lessonDao.findById(6L).get();

        assertLessons(readingLesson, addingLesson);
    }

    @Test
    void createAndReadShouldAddNewLessonToDatabaseIfArgumentIsLessonWithNotAppointedDateTime(){
        Lesson addingLesson = Lesson.builder()
                .withCourse(courseForTest)
                .withGroup(groupForTest)
                .withTeacher(professorForTest)
                .withFormOfLesson(formOfLessonForTest)
                .build();
        lessonDao.save(addingLesson);
        Lesson readingLesson = lessonDao.findById(6L).get();

        assertLessons(readingLesson, addingLesson);
    }

    @Test
    void createAndReadShouldAddListOfNewLessonsToDatabaseIfArgumentIsListOfLessons(){
        List<Lesson> addingLessonEntities = Arrays.asList (Lesson.builder()
                .withCourse(courseForTest)
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupForTest)
                .withTeacher(professorForTest)
                .withFormOfLesson(formOfLessonForTest)
                .build());
        lessonDao.saveAll(addingLessonEntities);
        List<Lesson> readingLessonEntities = Arrays.asList(lessonDao.findById(6L).get());

        assertLessons(readingLessonEntities, addingLessonEntities);
    }

    @Test
    void updateShouldUpdateDataOfLessonIfArgumentIsLesson(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build();
        lessonDao.update(expected);
        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfLessonIfArgumentIsListOfLesson(){
        List<Lesson> expected = Arrays.asList(Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build());
        lessonDao.updateAll(expected);
        List<Lesson> actual = Arrays.asList(lessonDao.findById(1L).get());

        assertLessons(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfLessonIfArgumentIsIdOfLesson(){
        Optional<Lesson> expected = Optional.empty();
        lessonDao.deleteById(1L);
        Optional<Lesson> actual = lessonDao.findById(1L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByGroupIdShouldReturnListOfLessonForGroupIfArgumentIsIdOfGroup(){
        List<Lesson> expected = Arrays.asList(lessonDao.findById(1L).get(),
                lessonDao.findById(2L).get(),
                lessonDao.findById(3L).get());
        List<Lesson> actual = lessonDao.findByGroupId(1);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByProfessorIdShouldReturnListOfLessonForProfessorIfArgumentIsIdOfProfessor(){
        List<Lesson> expected = Arrays.asList(lessonDao.findById(1L).get(),
                lessonDao.findById(2L).get());
        List<Lesson> actual = lessonDao.findByProfessorId(7);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByCourseIdIdShouldReturnListOfLessonForProfessorIfArgumentIsIdOfProfessor(){
        List<Lesson> expected = Arrays.asList(lessonDao.findById(1L).get(),
                lessonDao.findById(2L).get());
        List<Lesson> actual = lessonDao.findByCourseId(1);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByFormOfLessonIdIdShouldReturnListOfLessonForProfessorIfArgumentIsIdOfProfessor(){
        List<Lesson> expected = Arrays.asList(lessonDao.findById(2L).get(),
                lessonDao.findById(5L).get());
        List<Lesson> actual = lessonDao.findByFormOfLessonId(2);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeFormOfLessonShouldChangeFormOfLessonIfArgumentIsIdOfLessonAndIdOfNewFormOfLesson(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(formOfLessonDao.findById(2L).get())
                .build();

        lessonDao.changeFormOfLesson(1, 2);

        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeFormOfLessonShouldChangeFormOfLessonIfArgumentIsIdOfLessonAndIdOfNewFormOfLesson(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(FormOfLesson.builder().withId(0L).build())
                .build();

        lessonDao.removeFormOfLessonFromLesson(1);

        Lesson actual = lessonDao.findById(1L).get();


        assertLessons(actual, expected);
    }

    @Test
    void changeTeacherShouldChangeTeacherOfLessonIfArgumentIsIdOfLessonAndIdOfNewProfessor(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(professorDao.findById(9L).get())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build();

        lessonDao.changeTeacher(1, 9);

        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeTeacherShouldChangeTeacherOfLessonIfArgumentIsIdOfLessonAndIdOfNewProfessor(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(Professor.builder().withId(0L).build())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build();

        lessonDao.removeTeacherFromLesson(1);

        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void changeCourseShouldChangeCourseOfLessonIfArgumentIsIdOfLessonAndIdOfNewCourse(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(2L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build();

        lessonDao.changeCourse(1, 2);

        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeCourseShouldChangeCourseOfLessonIfArgumentIsIdOfLessonAndIdOfNewCourse(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(Course.builder().withId(0L).build())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(1L).get())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build();

        lessonDao.removeCourseFromLesson(1);

        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void changeGroupShouldChangeGroupOfLessonIfArgumentIsIdOfLessonAndIdOfNewGroup(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById(2L).get())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build();

        lessonDao.changeGroup(1, 2);

        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }

    @Test
    void removeGroupShouldChangeGroupOfLessonIfArgumentIsIdOfLessonAndIdOfNewGroup(){
        Lesson expected = Lesson.builder()
                .withId(1L)
                .withCourse(courseDao.findById(1L).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(Group.builder().withId(0L).build())
                .withTeacher(professorDao.findById(7L).get())
                .withFormOfLesson(formOfLessonDao.findById(1L).get())
                .build();

        lessonDao.removeGroupFromLesson(1);

        Lesson actual = lessonDao.findById(1L).get();

        assertLessons(actual, expected);
    }
}
