package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.CourseResponseMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    CourseDao courseDao;

    @Mock
    ProfessorDao professorDao;

    @Mock
    DepartmentDao departmentDao;

    @Mock
    LessonDao lessonDao;

    @Mock
    CourseRequestMapper courseRequestMapper;

    @Mock
    CourseResponseMapper courseResponseMapper;

    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void addCourseToProfessorCourseListShouldAddCourseToProfessorCourseListIfArgumentsIsCourseIdAndProfessorId() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(3L).build());

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseDao.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);
        doNothing().when(courseDao).addCourseToProfessorCourseList(courseId, professorId);

        courseService.addCourseToProfessorCourseList(courseId, professorId);

        verify(courseDao).findById(courseId);
        verify(professorDao).findById(professorId);
        verify(courseDao).findByProfessorId(professorId);
        verify(courseDao).addCourseToProfessorCourseList(courseId, professorId);
    }

    @Test
    void addCourseToProfessorCourseListShouldDoNothingIfCourseAlreadyExistInProfessorCourseList() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(1L).build());

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseDao.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);

        courseService.addCourseToProfessorCourseList(courseId, professorId);

        verify(courseDao).findById(courseId);
        verify(professorDao).findById(professorId);
        verify(courseDao).findByProfessorId(professorId);
    }

    @Test
    void addCourseToProfessorCourseListShouldThrowExceptionIfCourseDontExist() {
        long courseId = 1;
        long professorId = 2;

        when(courseDao.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.addCourseToProfessorCourseList(courseId, professorId)).hasMessage("There no course with id: 1");

        verify(courseDao).findById(courseId);
    }

    @Test
    void addCourseToProfessorCourseListShouldThrowExceptionIfProfessorDontExist() {
        long courseId = 1;
        long professorId = 2;

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.addCourseToProfessorCourseList(courseId, professorId)).hasMessage("There no professor with id: 2");

        verify(courseDao).findById(courseId);
        verify(professorDao).findById(professorId);
    }

    @Test
    void removeCourseToProfessorCourseListShouldRemoveCourseToProfessorCourseListIfArgumentsIsCourseIdAndProfessorId() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(1L).build());

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseDao.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);
        doNothing().when(courseDao).removeCourseFromProfessorCourseList(courseId, professorId);

        courseService.removeCourseFromProfessorCourseList(courseId, professorId);

        verify(courseDao).findById(courseId);
        verify(professorDao).findById(professorId);
        verify(courseDao).findByProfessorId(professorId);
        verify(courseDao).removeCourseFromProfessorCourseList(courseId, professorId);
    }

    @Test
    void removeCourseToProfessorCourseListShouldDoNothingIfThisCourseDontExistInProfessorCourseList() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(3L).build());

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseDao.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);

        courseService.removeCourseFromProfessorCourseList(courseId, professorId);

        verify(courseDao).findById(courseId);
        verify(professorDao).findById(professorId);
        verify(courseDao).findByProfessorId(professorId);
    }

    @Test
    void findByProfessorIdShouldReturnListOfCourseResponsesIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseDao.findByProfessorId(professorId)).thenReturn(Arrays.asList(Course.builder().withId(1L).build()));

        courseService.findByProfessorId(professorId);

        verify(professorDao).findById(professorId);
        verify(courseDao).findByProfessorId(professorId);
    }

    @Test
    void findByProfessorIdShouldThrowExceptionIfProfessorDontExist() {
        long professorId = 100;

        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findByProfessorId(professorId)).hasMessage("There no professor with id = 100");

        verify(professorDao).findById(professorId);
    }

    @Test
    void createShouldAddCourseToDBIfArgumentsIsCourseRequestAndDepartmentNotChosen() {
        String courseName= "Math";
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(courseName);
        courseRequest.setDepartmentId(0L);

        when(courseDao.findByName(courseName)).thenReturn(Optional.empty());

        courseService.create(courseRequest);

        verify(courseDao).findByName(courseName);
    }

    @Test
    void createShouldAddCourseToDBIfArgumentsIsCourseRequestAndDepartmentChosen() {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setName("Math");
        courseRequest.setDepartmentId(1L);
        Department department = Department.builder().withId(1L).withName("Department of Math").build();
        Course courseBeforeSave = Course.builder().withName("Math").build();
        Course courseAfterSave = Course.builder().withId(1L).withName("Math").build();

        when(courseRequestMapper.mapDtoToEntity(courseRequest)).thenReturn(courseBeforeSave);
        when(courseDao.save(courseBeforeSave)).thenReturn(courseAfterSave);
        when(courseDao.findByName("Math")).thenReturn(Optional.empty());
        when(courseDao.findById(1L)).thenReturn(Optional.of(courseAfterSave));
        when(departmentDao.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(courseDao).changeDepartment(1L, 1L);

        courseService.create(courseRequest);

        verify(courseRequestMapper).mapDtoToEntity(courseRequest);
        verify(courseDao).save(courseBeforeSave);
        verify(courseDao).findByName("Math");
        verify(courseDao).findById(1L);
        verify(departmentDao).findById(1L);
        verify(courseDao).changeDepartment(1L, 1L);
        verifyNoMoreInteractions(courseRequestMapper);
        verifyNoMoreInteractions(courseDao);
        verifyNoMoreInteractions(departmentDao);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfCourseWithSameNameAlreadyExist() {
        String courseName= "Math";
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(courseName);

        when(courseDao.findByName(courseName)).thenReturn(Optional.of(Course.builder().withName(courseName).build()));

        assertThatThrownBy(() -> courseService.create(courseRequest)).hasMessage("Course with same name already exist");

        verify(courseDao).findByName(courseName);
    }

    @Test
    void findByIdShouldReturnOptionalOfCourseResponseIfArgumentIsCourseId() {
        long courseId = 1;

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(1L).build()));

        courseService.findById(courseId);

        verify(courseDao).findById(courseId);
    }

    @Test
    void findAllIdShouldReturnListOfCourseResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(courseDao.count()).thenReturn(11L);
        when(courseDao.findAll(2, 5)).thenReturn(Arrays.asList(Course.builder().withId(1L).build()));

        courseService.findAll(pageNumber);

        verify(courseDao).count();
        verify(courseDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfCourseResponseNoArguments() {
        when(courseDao.findAll()).thenReturn(Arrays.asList(Course.builder().withId(1L).build()));

        courseService.findAll();

        verify(courseDao).findAll();
    }

    @Test
    void editShouldEditDataOfCourseIfArgumentNewCourseRequestAndDepartmentNotChosen() {
        Course course = Course.builder().withId(1L).build();
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setDepartmentId(0L);

        when(courseRequestMapper.mapDtoToEntity(courseRequest)).thenReturn(course);
        doNothing().when(courseDao).update(course);

        courseService.edit(courseRequest);

        verify(courseRequestMapper).mapDtoToEntity(courseRequest);
        verify(courseDao).update(course);
    }

    @Test
    void editShouldEditDataOfCourseIfArgumentNewCourseRequestAndDepartmentChosen() {
        Course course = Course.builder().withId(1L).build();
        Department department = Department.builder().withId(1L).build();
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setDepartmentId(1L);

        when(courseRequestMapper.mapDtoToEntity(courseRequest)).thenReturn(course);
        doNothing().when(courseDao).update(course);
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(departmentDao.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(courseDao).changeDepartment(1L, 1L);

        courseService.edit(courseRequest);

        verify(courseRequestMapper).mapDtoToEntity(courseRequest);
        verify(courseDao).update(course);
        verify(courseDao).findById(1L);
        verify(departmentDao).findById(1L);
        verify(courseDao).changeDepartment(1L, 1L);
        verifyNoMoreInteractions(courseRequestMapper);
        verifyNoMoreInteractions(courseDao);
        verifyNoMoreInteractions(departmentDao);
    }

    @Test
    void editShouldThrowExceptionIfArgumentNewCourseRequestAndDepartmentChosenAndDepartmentNotExist() {
        Course course = Course.builder().withId(1L).build();
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setDepartmentId(1L);

        when(courseRequestMapper.mapDtoToEntity(courseRequest)).thenReturn(course);
        doNothing().when(courseDao).update(course);
        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        when(departmentDao.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.edit(courseRequest)).hasMessage("There no department with id: 1");

        verify(courseRequestMapper).mapDtoToEntity(courseRequest);
        verify(courseDao).update(course);
        verify(courseDao).findById(1L);
        verify(departmentDao).findById(1L);
        verifyNoMoreInteractions(courseRequestMapper);
        verifyNoMoreInteractions(courseDao);
        verifyNoMoreInteractions(departmentDao);
    }

    @Test
    void removeDepartmentFromCourseShouldRemoveDepartmentFromCourseIfArgumentCourseId() {
        Course course = Course.builder().withId(1L).build();

        when(courseDao.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseDao).removeDepartmentFromCourse(1L);

        courseService.removeDepartmentFromCourse(1L);

        verify(courseDao).findById(1L);
        verify(courseDao).removeDepartmentFromCourse(1L);
        verifyNoMoreInteractions(courseDao);
    }

    @Test
    void deleteShouldDeleteDataOfCourseIfArgumentIsCourseId() {
        long courseId = 1;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> courseLessons = Arrays.asList(lesson1, lesson2);

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(lessonDao.findByCourseId(courseId)).thenReturn(courseLessons);
        doNothing().when(lessonDao).removeCourseFromLesson(1L);
        doNothing().when(lessonDao).removeCourseFromLesson(2L);
        when(courseDao.deleteById(courseId)).thenReturn(true);

        courseService.deleteById(courseId);

        verify(courseDao).findById(courseId);
        verify(lessonDao).findByCourseId(courseId);
        verify(lessonDao).removeCourseFromLesson(1L);
        verify(lessonDao).removeCourseFromLesson(2L);
        verify(courseDao).deleteById(courseId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentCourseDontExist() {
        long courseId = 1;

        when(courseDao.findById(courseId)).thenReturn(Optional.empty());

        courseService.deleteById(courseId);

        verify(courseDao).findById(courseId);
    }

}
