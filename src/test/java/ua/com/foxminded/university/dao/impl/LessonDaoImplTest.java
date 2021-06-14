package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.domain.Course;
import ua.com.foxminded.university.domain.FormOfLesson;
import ua.com.foxminded.university.domain.Group;
import ua.com.foxminded.university.domain.Lesson;
import ua.com.foxminded.university.domain.Professor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertLessons;

public class LessonDaoImplTest {

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
        groupForTest = Group.builder().withId((long)0).build();

        courseDao = context.getBean(CourseDaoImpl.class);
        courseForTest = Course.builder().withId((long)0).build();

        professorDao = context.getBean(ProfessorDaoImpl.class);
        professorForTest = Professor.builder().withId((long)0).build();

        formOfLessonDao = context.getBean(FormOfLessonDaoImpl.class);
        formOfLessonForTest = FormOfLesson.builder().withId((long)0).build();
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
        Lesson readingLesson = lessonDao.findById((long)6).get();


        assertLessons(readingLesson, addingLesson);
    }

    @Test
    void createAndReadShouldAddListOfNewLessonsToDatabaseIfArgumentIsListOfLessons(){
        List<Lesson> addingLessons = Arrays.asList (Lesson.builder()
                .withCourse(courseForTest)
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupForTest)
                .withTeacher(professorForTest)
                .withFormOfLesson(formOfLessonForTest)
                .build());
        lessonDao.saveAll(addingLessons);
        List<Lesson> readingLessons = Arrays.asList(lessonDao.findById((long)6).get());

        assertLessons(readingLessons, addingLessons);
    }

    @Test
    void updateShouldUpdateDataOfLessonIfArgumentIsLesson(){
        Lesson expected = Lesson.builder()
                .withId((long)1)
                .withCourse(courseDao.findById((long)1).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupDao.findById((long)1).get())
                .withTeacher(professorDao.findById((long)7).get())
                .withFormOfLesson(formOfLessonDao.findById((long)1).get())
                .build();
        lessonDao.update(expected);
        Lesson actual = lessonDao.findById((long)1).get();

        assertLessons(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfLessonIfArgumentIsListOfLesson(){
        List<Lesson> expected = Arrays.asList(Lesson.builder()
                .withId((long)1)
                .withCourse(courseDao.findById((long)1).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-15_12:00:00.000", FORMATTER))
                .withGroup(groupDao.findById((long)1).get())
                .withTeacher(professorDao.findById((long)7).get())
                .withFormOfLesson(formOfLessonDao.findById((long)1).get())
                .build());
        lessonDao.updateAll(expected);
        List<Lesson> actual = Arrays.asList(lessonDao.findById((long)1).get());

        assertLessons(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfLessonIfArgumentIsIdOfLesson(){
        Optional<Lesson> expected = Optional.empty();
        lessonDao.deleteById((long)1);
        Optional<Lesson> actual = lessonDao.findById((long)1);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void formTimeTableForGroupShouldReturnListOfLessonForGroupIfArgumentIsIdOfGroup(){
        List<Lesson> expected = Arrays.asList(lessonDao.findById((long)1).get(),
                lessonDao.findById((long)2).get(),
                lessonDao.findById((long)3).get());
        List<Lesson> actual = lessonDao.formTimeTableForGroup(1);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void formTimeTableForProfessorShouldReturnListOfLessonForProfessorIfArgumentIsIdOfProfessor(){
        List<Lesson> expected = Arrays.asList(lessonDao.findById((long)1).get(),
                lessonDao.findById((long)2).get());
        List<Lesson> actual = lessonDao.formTimeTableForProfessor(7);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeFormOfLessonShouldChangeFormOfLessonIfArgumentIsIdOfLessonAndIdOfNewFormOfLesson(){
        Lesson expected = Lesson.builder()
                .withId((long)1)
                .withCourse(courseDao.findById((long)1).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById((long)1).get())
                .withTeacher(professorDao.findById((long)7).get())
                .withFormOfLesson(formOfLessonDao.findById((long)2).get())
                .build();

        lessonDao.changeFormOfLesson(1, 2);

        Lesson actual = lessonDao.findById((long)1).get();

        assertLessons(actual, expected);
    }

    @Test
    void changeTeacherShouldChangeTeacherOfLessonIfArgumentIsIdOfLessonAndIdOfNewProfessor(){
        Lesson expected = Lesson.builder()
                .withId((long)1)
                .withCourse(courseDao.findById((long)1).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById((long)1).get())
                .withTeacher(professorDao.findById((long)9).get())
                .withFormOfLesson(formOfLessonDao.findById((long)1).get())
                .build();

        lessonDao.changeTeacher(1, 9);

        Lesson actual = lessonDao.findById((long)1).get();

        assertLessons(actual, expected);
    }

    @Test
    void changeCourseShouldChangeCourseOfLessonIfArgumentIsIdOfLessonAndIdOfNewCourse(){
        Lesson expected = Lesson.builder()
                .withId((long)1)
                .withCourse(courseDao.findById((long)2).get())
                .withTimeOfStartLesson(LocalDateTime.parse("2020-01-11_13:00:00.000", FORMATTER))
                .withGroup(groupDao.findById((long)1).get())
                .withTeacher(professorDao.findById((long)7).get())
                .withFormOfLesson(formOfLessonDao.findById((long)1).get())
                .build();

        lessonDao.changeCourse(1, 2);

        Lesson actual = lessonDao.findById((long)1).get();

        assertLessons(actual, expected);
    }
}
