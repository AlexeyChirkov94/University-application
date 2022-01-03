package ua.com.foxminded.university.service.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Lesson;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LessonValidatorImplTest {

    @Mock
    CourseDao courseDao;

    @Mock
    LessonDao lessonDao;

    @InjectMocks
    LessonValidatorImpl lessonValidator;


    @Test
    void validateCompatibilityCourseAndProfessorShouldDoNothingIfCourseIdIsZero() {
                assertThatCode(() -> lessonValidator.validateCompatibilityCourseAndProfessor(0, 1))
                        .doesNotThrowAnyException();
    }

    @Test
    void validateCompatibilityCourseAndProfessorShouldDoNothingIfProfessorIdIsZero() {
        assertThatCode(() -> lessonValidator.validateCompatibilityCourseAndProfessor(1, 0))
                .doesNotThrowAnyException();
    }

    @Test
    void validateCompatibilityCourseAndProfessorShouldDoNothingIfCourseExistInProfessorCourseList() {
        Course course1 = Course.builder().withId(1L).build();
        Course course2 = Course.builder().withId(2L).build();
        List<Course> professorCourses = new ArrayList<>();
        professorCourses.add(course1);
        professorCourses.add(course2);

        long professorId = 1;
        long courseId = 2;

        when(courseDao.findByProfessorId(professorId)).thenReturn(professorCourses);

        assertThatCode(() -> lessonValidator.validateCompatibilityCourseAndProfessor(courseId, professorId))
                .doesNotThrowAnyException();

        verify(courseDao).findByProfessorId(professorId);
    }

    @Test
    void validateCompatibilityCourseAndProfessorShouldThrowExceptionIfCourseNotExistInProfessorCourseList() {
        Course course1 = Course.builder().withId(1L).build();
        Course course2 = Course.builder().withId(2L).build();
        List<Course> professorCourses = new ArrayList<>();
        professorCourses.add(course1);
        professorCourses.add(course2);

        long professorId = 1;
        long courseId = 3;

        when(courseDao.findByProfessorId(professorId)).thenReturn(professorCourses);

        assertThatThrownBy(() -> lessonValidator.validateCompatibilityCourseAndProfessor(courseId, professorId))
                .hasMessage("Selected professor can`t teach this course");

        verify(courseDao).findByProfessorId(professorId);
    }

    @Test
    void checkGroupTimeTableCrossingShouldDoNothingIfLessonHaveToTimeOfStart(){
        long groupId = 1;
        long lessonId = 2;

        Lesson addingLesson = Lesson.builder().withId(lessonId).build();

        Lesson lesson1 = Lesson.builder().withId(1L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();
        Lesson lesson2 = Lesson.builder().withId(2L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 12, 10, 00)).build();
        List<Lesson> groupLessons = new ArrayList<>();
        groupLessons.add(lesson1);
        groupLessons.add(lesson2);

        when(lessonDao.findByGroupId(groupId)).thenReturn(groupLessons);
        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(addingLesson));

        assertThatCode(() -> lessonValidator.checkGroupTimeTableCrossing(lessonId, groupId))
                .doesNotThrowAnyException();

        verify(lessonDao).findByGroupId(groupId);
        verify(lessonDao).findById(lessonId);
    }

    @Test
    void checkGroupTimeTableCrossingShouldDoNothingIfLessonTimeNotCrossingWithGroupTimeTable(){
        long groupId = 1;
        long lessonId = 2;

        Lesson addingLesson = Lesson.builder().withId(lessonId).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 14, 00)).build();

        Lesson lesson1 = Lesson.builder().withId(1L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();
        Lesson lesson2 = Lesson.builder().withId(2L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 12, 10, 00)).build();
        List<Lesson> groupLessons = new ArrayList<>();
        groupLessons.add(lesson1);
        groupLessons.add(lesson2);

        when(lessonDao.findByGroupId(groupId)).thenReturn(groupLessons);
        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(addingLesson));

        assertThatCode(() -> lessonValidator.checkGroupTimeTableCrossing(lessonId, groupId))
                .doesNotThrowAnyException();

        verify(lessonDao).findByGroupId(groupId);
        verify(lessonDao).findById(lessonId);
    }

    @Test
    void checkGroupTimeTableCrossingShouldThrowExceptionIfLessonTimeCrossingWithGroupTimeTable(){
        long groupId = 1;
        long lessonId = 2;

        Lesson addingLesson = Lesson.builder().withId(lessonId).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();

        Lesson lesson1 = Lesson.builder().withId(1L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();
        Lesson lesson2 = Lesson.builder().withId(2L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 12, 10, 00)).build();
        List<Lesson> groupLessons = new ArrayList<>();
        groupLessons.add(lesson1);
        groupLessons.add(lesson2);

        when(lessonDao.findByGroupId(groupId)).thenReturn(groupLessons);
        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(addingLesson));

        assertThatThrownBy(() -> lessonValidator.checkGroupTimeTableCrossing(lessonId, groupId))
                .hasMessage("Lesson can`t be appointed on this time, cause time of lesson cross timetable of group");

        verify(lessonDao).findByGroupId(groupId);
        verify(lessonDao).findById(lessonId);
    }

    @Test
    void checkProfessorTimeTableCrossingShouldDoNothingIfLessonHaveToTimeOfStart(){
        long professorId = 1;
        long lessonId = 2;

        Lesson addingLesson = Lesson.builder().withId(lessonId).build();

        Lesson lesson1 = Lesson.builder().withId(1L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();
        Lesson lesson2 = Lesson.builder().withId(2L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 12, 10, 00)).build();
        List<Lesson> professorLessons = new ArrayList<>();
        professorLessons.add(lesson1);
        professorLessons.add(lesson2);

        when(lessonDao.findByProfessorId(professorId)).thenReturn(professorLessons);
        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(addingLesson));

        assertThatCode(() -> lessonValidator.checkProfessorTimeTableCrossing(lessonId, professorId))
                .doesNotThrowAnyException();

        verify(lessonDao).findByProfessorId(professorId);
        verify(lessonDao).findById(lessonId);
    }

    @Test
    void checkProfessorTimeTableCrossingShouldDoNothingIfLessonTimeNotCrossingWithProfessorTimeTable(){
        long professorId = 1;
        long lessonId = 2;

        Lesson addingLesson = Lesson.builder().withId(lessonId).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 14, 00)).build();


        Lesson lesson1 = Lesson.builder().withId(1L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();
        Lesson lesson2 = Lesson.builder().withId(2L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 12, 10, 00)).build();
        List<Lesson> professorLessons = new ArrayList<>();
        professorLessons.add(lesson1);
        professorLessons.add(lesson2);

        when(lessonDao.findByProfessorId(professorId)).thenReturn(professorLessons);
        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(addingLesson));

        assertThatCode(() -> lessonValidator.checkProfessorTimeTableCrossing(lessonId, professorId))
                .doesNotThrowAnyException();

        verify(lessonDao).findByProfessorId(professorId);
        verify(lessonDao).findById(lessonId);
    }

    @Test
    void checkProfessorTimeTableCrossingShouldThrowExceptionIfLessonTimeCrossingWithProfessorTimeTable(){
        long professorId = 1;
        long lessonId = 2;

        Lesson addingLesson = Lesson.builder().withId(lessonId).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();


        Lesson lesson1 = Lesson.builder().withId(1L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 10, 10, 00)).build();
        Lesson lesson2 = Lesson.builder().withId(2L).withTimeOfStartLesson(LocalDateTime.of(2020, 01, 12, 10, 00)).build();
        List<Lesson> professorLessons = new ArrayList<>();
        professorLessons.add(lesson1);
        professorLessons.add(lesson2);

        when(lessonDao.findByProfessorId(professorId)).thenReturn(professorLessons);
        when(lessonDao.findById(lessonId)).thenReturn(Optional.of(addingLesson));

        assertThatThrownBy(() -> lessonValidator.checkProfessorTimeTableCrossing(lessonId, professorId))
                .hasMessage("Lesson can`t be appointed on this time, cause time of lesson cross timetable of professor");

        verify(lessonDao).findByProfessorId(professorId);
        verify(lessonDao).findById(lessonId);
    }
}
