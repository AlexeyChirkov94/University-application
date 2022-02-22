package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.CourseMapper;
import java.util.Arrays;
import java.util.Collections;
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
    CourseRepository courseRepository;

    @Mock
    ProfessorRepository professorRepository;

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    LessonRepository lessonRepository;

    @Mock
    CourseMapper courseMapper;


    @InjectMocks
    CourseServiceImpl courseService;

    @Test
    void addCourseToProfessorCourseListShouldAddCourseToProfessorCourseListIfArgumentsIsCourseIdAndProfessorId() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(3L).build());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseRepository.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);
        doNothing().when(courseRepository).addCourseToProfessorCourseList(courseId, professorId);

        courseService.addCourseToProfessorCourseList(courseId, professorId);

        verify(courseRepository).findById(courseId);
        verify(professorRepository).findById(professorId);
        verify(courseRepository).findByProfessorId(professorId);
        verify(courseRepository).addCourseToProfessorCourseList(courseId, professorId);
    }

    @Test
    void addCourseToProfessorCourseListShouldDoNothingIfCourseAlreadyExistInProfessorCourseList() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(1L).build());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseRepository.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);

        courseService.addCourseToProfessorCourseList(courseId, professorId);

        verify(courseRepository).findById(courseId);
        verify(professorRepository).findById(professorId);
        verify(courseRepository).findByProfessorId(professorId);
    }

    @Test
    void addCourseToProfessorCourseListShouldThrowExceptionIfCourseDontExist() {
        long courseId = 1;
        long professorId = 2;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.addCourseToProfessorCourseList(courseId, professorId)).hasMessage("There no course with id: 1");

        verify(courseRepository).findById(courseId);
    }

    @Test
    void addCourseToProfessorCourseListShouldThrowExceptionIfProfessorDontExist() {
        long courseId = 1;
        long professorId = 2;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.addCourseToProfessorCourseList(courseId, professorId)).hasMessage("There no professor with id: 2");

        verify(courseRepository).findById(courseId);
        verify(professorRepository).findById(professorId);
    }

    @Test
    void removeCourseToProfessorCourseListShouldRemoveCourseToProfessorCourseListIfArgumentsIsCourseIdAndProfessorId() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(1L).build());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseRepository.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);
        doNothing().when(courseRepository).removeCourseFromProfessorCourseList(courseId, professorId);

        courseService.removeCourseFromProfessorCourseList(courseId, professorId);

        verify(courseRepository).findById(courseId);
        verify(professorRepository).findById(professorId);
        verify(courseRepository).findByProfessorId(professorId);
        verify(courseRepository).removeCourseFromProfessorCourseList(courseId, professorId);
    }

    @Test
    void removeCourseToProfessorCourseListShouldDoNothingIfThisCourseDontExistInProfessorCourseList() {
        long courseId = 1;
        long professorId = 2;
        List<Course> coursesOfProfessor = Arrays.asList(Course.builder().withId(3L).build());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseRepository.findByProfessorId(professorId)).thenReturn(coursesOfProfessor);

        courseService.removeCourseFromProfessorCourseList(courseId, professorId);

        verify(courseRepository).findById(courseId);
        verify(professorRepository).findById(professorId);
        verify(courseRepository).findByProfessorId(professorId);
    }

    @Test
    void findByProfessorIdShouldReturnListOfCourseResponsesIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(courseRepository.findByProfessorId(professorId)).thenReturn(Arrays.asList(Course.builder().withId(1L).build()));

        courseService.findByProfessorId(professorId);

        verify(professorRepository).findById(professorId);
        verify(courseRepository).findByProfessorId(professorId);
    }

    @Test
    void findByProfessorIdShouldThrowExceptionIfProfessorDontExist() {
        long professorId = 100;

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.findByProfessorId(professorId)).hasMessage("There no professor with id = 100");

        verify(professorRepository).findById(professorId);
    }

    @Test
    void createShouldAddCourseToDBIfArgumentsIsCourseRequestAndDepartmentNotChosen() {
        String courseName= "Math";
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(courseName);
        courseRequest.setDepartmentId(0L);

        when(courseRepository.findAllByName(courseName)).thenReturn(Collections.emptyList());

        courseService.create(courseRequest);

        verify(courseRepository).findAllByName(courseName);
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

        when(courseMapper.mapDtoToEntity(courseRequest)).thenReturn(courseBeforeSave);
        when(courseRepository.save(courseBeforeSave)).thenReturn(courseAfterSave);
        when(courseRepository.findAllByName("Math")).thenReturn(Collections.emptyList());
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseAfterSave));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(courseRepository).changeDepartment(1L, 1L);

        courseService.create(courseRequest);

        verify(courseMapper).mapDtoToEntity(courseRequest);
        verify(courseMapper).mapEntityToDto(courseAfterSave);
        verify(courseRepository).save(courseBeforeSave);
        verify(courseRepository).findAllByName("Math");
        verify(courseRepository).findById(1L);
        verify(departmentRepository).findById(1L);
        verify(courseRepository).changeDepartment(1L, 1L);
        verifyNoMoreInteractions(courseMapper);
        verifyNoMoreInteractions(courseRepository);
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfCourseWithSameNameAlreadyExist() {
        String courseName= "Math";
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(courseName);

        when(courseRepository.findAllByName(courseName)).thenReturn(Arrays.asList(Course.builder().withName(courseName).build()));

        assertThatThrownBy(() -> courseService.create(courseRequest)).hasMessage("Course with same name already exist");

        verify(courseRepository).findAllByName(courseName);
    }

    @Test
    void findByIdShouldReturnCourseResponseIfArgumentIsCourseId() {
        long courseId = 1;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(1L).build()));

        courseService.findById(courseId);

        verify(courseRepository).findById(courseId);
    }

    @Test
    void findByIdShouldThrowExceptionIfCourseNotExist() {
        long courseId = 1;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> courseService.findById(courseId)).hasMessage("There no course with id: 1");

        verify(courseRepository).findById(courseId);
    }

    @Test
    void findByDepartmentIdShouldReturnCoursesResponseIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(1L).build()));
        when(courseRepository.findByDepartmentId(departmentId)).thenReturn(Arrays.asList(Course.builder().withId(1L).build(),
                Course.builder().withId(2L).build()));

        courseService.findByDepartmentId(departmentId);

        verify(departmentRepository).findById(departmentId);
        verify(courseRepository).findByDepartmentId(departmentId);
    }

    @Test
    void findAllIdShouldReturnListOfCourseResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(courseRepository.count()).thenReturn(11L);
        when(courseRepository.findAll(PageRequest.of(1, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Course.builder().withId(1L).build())));

        courseService.findAll(pageNumber);

        verify(courseRepository).count();
        verify(courseRepository).findAll(PageRequest.of(1, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfCourseResponseNoArguments() {
        when(courseRepository.findAll(Sort.by("id"))).thenReturn(Arrays.asList(Course.builder().withId(1L).build()));

        courseService.findAll();

        verify(courseRepository).findAll(Sort.by("id"));
    }

    @Test
    void editShouldEditDataOfCourseIfArgumentNewCourseRequestAndDepartmentNotChosen() {
        Course course = Course.builder().withId(1L).build();
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setDepartmentId(0L);

        when(courseMapper.mapDtoToEntity(courseRequest)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);

        courseService.edit(courseRequest);

        verify(courseMapper).mapDtoToEntity(courseRequest);
        verify(courseRepository).save(course);
    }

    @Test
    void editShouldEditDataOfCourseIfArgumentNewCourseRequestAndDepartmentChosen() {
        Course course = Course.builder().withId(1L).build();
        Department department = Department.builder().withId(1L).build();
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setDepartmentId(1L);

        when(courseMapper.mapDtoToEntity(courseRequest)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(courseRepository).changeDepartment(1L, 1L);

        courseService.edit(courseRequest);

        verify(courseMapper).mapDtoToEntity(courseRequest);
        verify(courseRepository).save(course);
        verify(courseRepository).findById(1L);
        verify(departmentRepository).findById(1L);
        verify(courseRepository).changeDepartment(1L, 1L);
        verifyNoMoreInteractions(courseMapper);
        verifyNoMoreInteractions(courseRepository);
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void editShouldThrowExceptionIfArgumentNewCourseRequestAndDepartmentChosenAndDepartmentNotExist() {
        Course course = Course.builder().withId(1L).build();
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setDepartmentId(1L);

        when(courseMapper.mapDtoToEntity(courseRequest)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.edit(courseRequest)).hasMessage("There no department with id: 1");

        verify(courseMapper).mapDtoToEntity(courseRequest);
        verify(courseRepository).save(course);
        verify(courseRepository).findById(1L);
        verify(departmentRepository).findById(1L);
        verifyNoMoreInteractions(courseMapper);
        verifyNoMoreInteractions(courseRepository);
        verifyNoMoreInteractions(departmentRepository);
    }

    @Test
    void removeDepartmentFromCourseShouldRemoveDepartmentFromCourseIfArgumentCourseId() {
        Course course = Course.builder().withId(1L).build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).removeDepartmentFromCourse(1L);

        courseService.removeDepartmentFromCourse(1L);

        verify(courseRepository).findById(1L);
        verify(courseRepository).removeDepartmentFromCourse(1L);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void deleteShouldDeleteDataOfCourseIfArgumentIsCourseId() {
        long courseId = 1;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> courseLessons = Arrays.asList(lesson1, lesson2);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(Course.builder().withId(courseId).build()));
        when(lessonRepository.findAllByCourseIdOrderByTimeOfStartLesson(courseId)).thenReturn(courseLessons);
        doNothing().when(lessonRepository).removeCourseFromLesson(1L);
        doNothing().when(lessonRepository).removeCourseFromLesson(2L);
        doNothing().when(courseRepository).deleteById(courseId);

        courseService.deleteById(courseId);

        verify(courseRepository).findById(courseId);
        verify(lessonRepository).findAllByCourseIdOrderByTimeOfStartLesson(courseId);
        verify(lessonRepository).removeCourseFromLesson(1L);
        verify(lessonRepository).removeCourseFromLesson(2L);
        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentCourseDontExist() {
        long courseId = 1;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        courseService.deleteById(courseId);

        verify(courseRepository).findById(courseId);
    }

}
