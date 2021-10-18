package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.interfaces.CourseRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.CourseResponseMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    CourseDao courseDao;

    @Mock
    ProfessorDao professorDao;

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

        assertThatThrownBy(() -> courseService.addCourseToProfessorCourseList(courseId, professorId)).hasMessage("There no professor or course with this ids");

        verify(courseDao).findById(courseId);
    }

    @Test
    void addCourseToProfessorCourseListShouldThrowExceptionIfProfessorDontExist() {
        long courseId = 1;
        long professorId = 2;

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.addCourseToProfessorCourseList(courseId, professorId)).hasMessage("There no professor or course with this ids");

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
    void registerShouldAddCourseToDBIfArgumentsIsCourseRequest() {
        String courseName= "Math";
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(courseName);

        when(courseDao.findByName(courseName)).thenReturn(Optional.empty());

        courseService.register(courseRequest);

        verify(courseDao).findByName(courseName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfCourseWithSameNameAlreadyExist() {
        String courseName= "Math";
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(courseName);

        when(courseDao.findByName(courseName)).thenReturn(Optional.of(Course.builder().withName(courseName).build()));

        assertThatThrownBy(() -> courseService.register(courseRequest)).hasMessage("Course with same name already exist");

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
    void editShouldEditDataOfCourseIfArgumentNewCourseRequest() {
        Course course = Course.builder().withId(1L).build();
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);

        when(courseRequestMapper.mapDtoToEntity(courseRequest)).thenReturn(course);
        doNothing().when(courseDao).update(course);

        courseService.edit(courseRequest);

        verify(courseRequestMapper).mapDtoToEntity(courseRequest);
        verify(courseDao).update(course);
    }

    @Test
    void deleteShouldDeleteDataOfCourseIfArgumentIsCourseId() {
        long courseId = 1;

        when(courseDao.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(courseDao.deleteById(courseId)).thenReturn(true);

        courseService.deleteById(courseId);

        verify(courseDao).findById(courseId);
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
