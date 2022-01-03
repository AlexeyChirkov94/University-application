package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.dao.RoleDao;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.ProfessorMapper;
import ua.com.foxminded.university.service.validator.ScienceDegreeValidator;
import ua.com.foxminded.university.service.validator.UserValidator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class ProfessorServiceImplTest {

    @Mock
    ProfessorDao professorDao;

    @Mock
    CourseDao courseDao;

    @Mock
    LessonDao lessonDao;

    @Mock
    GroupDao groupDao;

    @Mock
    DepartmentDao departmentDao;

    @Mock
    RoleDao roleDao;

    @Mock
    UserValidator userValidator;

    @Mock
    ScienceDegreeValidator scienceDegreeValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ProfessorMapper professorMapper;

    @InjectMocks
    ProfessorServiceImpl professorService;

    @Test
    void changeScienceDegreeShouldChangeScienceDegreeOfProfessorIfArgumentIsProfessorIdAndScienceDegreeId() {
        long professorId = 1;
        int newScienceDegreeId = 2;

        doNothing().when(scienceDegreeValidator).validate(ScienceDegree.getById(newScienceDegreeId));
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        doNothing().when(professorDao).changeScienceDegree(professorId, newScienceDegreeId);

        professorService.changeScienceDegree(professorId, newScienceDegreeId);

        verify(scienceDegreeValidator).validate(ScienceDegree.getById(newScienceDegreeId));
        verify(professorDao).findById(professorId);
        verify(professorDao).changeScienceDegree(professorId, newScienceDegreeId);
    }

    @Test
    void changeDepartmentShouldChangeDepartmentOfProfessorIfArgumentIsProfessorIdAndDepartmentId() {
        long professorId = 1;
        long newDepartmentId = 2;

        doNothing().when(professorDao).changeDepartment(professorId, newDepartmentId);
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().build()));
        when(departmentDao.findById(newDepartmentId)).thenReturn(Optional.of(Department.builder().build()));

        professorService.changeDepartment(professorId, newDepartmentId);

        verify(professorDao).changeDepartment(professorId, newDepartmentId);
        verify(professorDao).findById(professorId);
        verify(departmentDao).findById(newDepartmentId);
    }

    @Test
    void removeDepartmentShouldRemoveDepartmentOfProfessorIfArgumentIsProfessorId() {
        long professorId = 1;

        doNothing().when(professorDao).removeDepartmentFromProfessor(professorId);
        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().build()));

        professorService.removeDepartmentFromProfessor(professorId);

        verify(professorDao).removeDepartmentFromProfessor(professorId);
        verify(professorDao).findById(professorId);
    }

    @Test
    void removeDepartmentShouldThrowExceptionIfProfessorNotExist() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.removeDepartmentFromProfessor(professorId))
                .hasMessage("There no user with id: 1");

        verify(professorDao).findById(professorId);
    }

    @Test
    void findByCourseIdShouldReturnListOfProfessorIfArgumentIsCourseId() {
        long courseId = 1;
        List<Professor> professors = Arrays.asList(Professor.builder().withId(1L).build());
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        List<ProfessorResponse> professorResponses = Arrays.asList(professorResponse);

        when(professorMapper.mapEntityToDto(professors.get(0))).thenReturn(professorResponse);
        when(professorDao.findByCourseId(courseId)).thenReturn(professors);

        assertThat(professorService.findByCourseId(courseId)).isEqualTo(professorResponses);

        verify(professorMapper).mapEntityToDto(professors.get(0));
        verify(professorDao).findByCourseId(courseId);
    }

    @Test
    void findByDepartmentIdShouldReturnCoursesResponseIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(1L).build()));
        when(professorDao.findByDepartmentId(departmentId)).thenReturn(Arrays.asList(Professor.builder().withId(1L).build(),
                Professor.builder().withId(2L).build()));

        professorService.findByDepartmentId(departmentId);

        verify(departmentDao).findById(departmentId);
        verify(professorDao).findByDepartmentId(departmentId);
    }

    @Test
    void findByDepartmentIdShouldThrowExceptionIfDepartmentNotExist() {
        long departmentId = 1;

        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.findByDepartmentId(departmentId)).hasMessage("There no department with id: 1");

        verify(departmentDao).findById(departmentId);
    }

    @Test
    void findByEmailShouldReturnOptionalOfProfessorResponseIfArgumentIsEmail() {
        String email= "Alexey94@gamil.com";
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);

        when(professorDao.findByEmail(email)).thenReturn(Optional.of(Professor.builder().withId(1L).build()));
        when(professorMapper.mapEntityToDto(professor)).thenReturn(professorResponse);

        professorService.findByEmail(email);

        verify(professorDao).findByEmail(email);
        verify(professorMapper).mapEntityToDto(professor);
    }

    @Test
    void findByEmailShouldReturnOptionalOfEmptyProfessorResponseIfEmailNotRegistered() {
        String email= "Alexey94@gamil.com";

        when(professorDao.findByEmail(email)).thenReturn(Optional.empty());

        professorService.findByEmail(email);

        verify(professorDao).findByEmail(email);
    }

    @Test
    void registerShouldAddProfessorToDBIfArgumentsIsProfessorRequestWithoutDepartmentAndScienceDegree() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        List<Role> professorRole = Collections.singletonList(Role.builder().withName("ROLE_PROFESSOR").build());
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);
        professorRequest.setDepartmentId(0L);
        professorRequest.setScienceDegreeId(0);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorDao.save(professor)).thenReturn(professor);
        when(roleDao.findByUserId(1L)).thenReturn(professorRole);

        professorService.register(professorRequest);

        verify(userValidator).validate(professorRequest);
        verify(professorDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorDao).save(professor);
        verify(roleDao).findByUserId(1L);
    }

    @Test
    void registerWithAddingDefaultRoleShouldAddProfessorToDBAndAddRoleToProfessorIfArgumentsIsProfessorRequestWithoutDepartmentAndScienceDegree() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        List<Role> professorRole = Collections.emptyList();
        Role defaultRole = Role.builder().withId(3L).withName("ROLE_PROFESSOR").build();
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);
        professorRequest.setDepartmentId(0L);
        professorRequest.setScienceDegreeId(0);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorDao.save(professor)).thenReturn(professor);
        when(roleDao.findByUserId(1L)).thenReturn(professorRole);
        when(roleDao.findByName("ROLE_PROFESSOR")).thenReturn(Optional.of(defaultRole));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));
        when(roleDao.findById(3L)).thenReturn(Optional.of(defaultRole));

        professorService.register(professorRequest);

        verify(userValidator).validate(professorRequest);
        verify(professorDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorDao).save(professor);
        verify(roleDao).findByUserId(1L);
        verify(roleDao).findByUserId(1L);
        verify(roleDao).findByName("ROLE_PROFESSOR");
        verify(professorDao).findById(1L);
        verify(roleDao).findById(3L);
    }

    @Test
    void registerWithAddingDefaultRoleShouldThrowExceptionIfDefaultRoleNotExist() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        List<Role> professorRole = Collections.emptyList();
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);
        professorRequest.setDepartmentId(0L);
        professorRequest.setScienceDegreeId(0);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorDao.save(professor)).thenReturn(professor);
        when(roleDao.findByUserId(1L)).thenReturn(professorRole);
        when(roleDao.findByName("ROLE_PROFESSOR")).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> professorService.register(professorRequest)).hasMessage("ROLE_PROFESSOR not initialized");

        verify(userValidator).validate(professorRequest);
        verify(professorDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorDao).save(professor);
        verify(roleDao).findByUserId(1L);
        verify(roleDao).findByUserId(1L);
        verify(roleDao).findByName("ROLE_PROFESSOR");
    }

    @Test
    void registerShouldAddProfessorToDBIfArgumentsIsProfessorRequestWithDepartmentAndScienceDegree() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        Department department = Department.builder().withId(1L).build();
        ScienceDegree scienceDegree = ScienceDegree.getById(1);
        List<Role> professorRole = Collections.singletonList(Role.builder().withName("ROLE_PROFESSOR").build());
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);
        professorRequest.setDepartmentId(1L);
        professorRequest.setScienceDegreeId(1);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorDao.save(professor)).thenReturn(professor);
        when(departmentDao.findById(1L)).thenReturn(Optional.of(department));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));
        when(roleDao.findByUserId(1L)).thenReturn(professorRole);
        doNothing().when(professorDao).changeDepartment(1L, 1L);
        doNothing().when(scienceDegreeValidator).validate(scienceDegree);
        doNothing().when(professorDao).changeScienceDegree(1L, 1);

        professorService.register(professorRequest);

        verify(userValidator).validate(professorRequest);
        verify(professorDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorDao).save(professor);
        verify(departmentDao).findById(1L);
        verify(professorDao ,times(2)).findById(1L);
        verify(roleDao).findByUserId(1L);
        verify(professorDao).changeDepartment(1L, 1L);
        verify(scienceDegreeValidator).validate(scienceDegree);
        verify(professorDao).changeScienceDegree(1L, 1);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfProfessorWithSameNameAlreadyExist() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorDao.findByEmail(email)).thenReturn(Optional.of(Professor.builder().withEmail(email).build()));

        assertThatThrownBy(() -> professorService.register(professorRequest)).hasMessage("This email already registered");

        verify(userValidator).validate(professorRequest);
        verify(professorDao).findByEmail(email);
    }

    @Test
    void findByIdShouldReturnProfessorResponseIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(1L).build()));

        professorService.findById(professorId);

        verify(professorDao).findById(professorId);
    }

    @Test
    void findByIdShouldThrowExceptionIfProfessorNotExist() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> professorService.findById(professorId)).hasMessage("There no professor with id: 1");

        verify(professorDao).findById(professorId);
    }

    @Test
    void findByIdShouldThrowExceptionIfProfessorNotExistIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.findById(professorId)).hasMessage("There no professor with id: 1");

        verify(professorDao).findById(professorId);
    }

    @Test
    void findAllIdShouldReturnListOfProfessorResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(professorDao.count()).thenReturn(11L);
        when(professorDao.findAll(2, 5)).thenReturn(Arrays.asList(Professor.builder().withId(1L).build()));

        professorService.findAll(pageNumber);

        verify(professorDao).count();
        verify(professorDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfProfessorResponseNoArguments() {
        when(professorDao.findAll()).thenReturn(Arrays.asList(Professor.builder().withId(1L).build()));

        professorService.findAll();

        verify(professorDao).findAll();
    }

    @Test
    void editShouldEditDataOfProfessorIfArgumentNewProfessorRequestWithoutDepartmentAndScienceDegree() {
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setDepartmentId(0L);
        professorRequest.setScienceDegreeId(0);

        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        doNothing().when(professorDao).update(professor);

        professorService.edit(professorRequest);

        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorDao).update(professor);
    }

    @Test
    void editShouldEditDataOfProfessorIfArgumentNewProfessorRequestWithDepartmentAndScienceDegree() {
        Department department = Department.builder().withId(1L).build();
        ScienceDegree scienceDegree = ScienceDegree.getById(1);

        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setDepartmentId(1L);
        professorRequest.setScienceDegreeId(1);

        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        doNothing().when(professorDao).update(professor);
        when(departmentDao.findById(1L)).thenReturn(Optional.of(department));
        when(professorDao.findById(1L)).thenReturn(Optional.of(professor));
        doNothing().when(professorDao).changeDepartment(1L, 1L);
        doNothing().when(scienceDegreeValidator).validate(scienceDegree);
        doNothing().when(professorDao).changeScienceDegree(1L, 1);

        professorService.edit(professorRequest);

        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorDao).update(professor);
        verify(departmentDao).findById(1L);
        verify(professorDao ,times(2)).findById(1L);
        verify(professorDao).changeDepartment(1L, 1L);
        verify(scienceDegreeValidator).validate(scienceDegree);
        verify(professorDao).changeScienceDegree(1L, 1);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsProfessorId() {
        long professorId = 1;
        Course course1 = Course.builder().withId(1L).build();
        Course course2 = Course.builder().withId(2L).build();
        List<Course> professorCourses = Arrays.asList(course1, course2);
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> professorLessons = Arrays.asList(lesson1, lesson2);
        List<Role> professorRoles = Collections.singletonList(Role.builder().withId(2L).withName("ROLE_PROFESSOR").build());

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(roleDao.findByUserId(professorId)).thenReturn(professorRoles);
        doNothing().when(professorDao).removeRoleFromUser(1L, 2L);
        when(professorDao.deleteById(professorId)).thenReturn(true);
        when(courseDao.findByProfessorId(1L)).thenReturn(professorCourses);
        doNothing().when(courseDao).removeCourseFromProfessorCourseList(1L, 1L);
        doNothing().when(courseDao).removeCourseFromProfessorCourseList(2L, 1L);
        when(lessonDao.findByProfessorId(1L)).thenReturn(professorLessons);
        doNothing().when(lessonDao).removeTeacherFromLesson(1L);
        doNothing().when(lessonDao).removeTeacherFromLesson(2L);

        professorService.deleteById(professorId);

        verify(professorDao, times(2)).findById(professorId);
        verify(roleDao).findByUserId(professorId);
        verify(professorDao).removeRoleFromUser(1L, 2L);
        verify(professorDao).deleteById(professorId);
        verify(courseDao).findByProfessorId(1L);
        verify(courseDao).removeCourseFromProfessorCourseList(1L, 1L);
        verify(courseDao).removeCourseFromProfessorCourseList(2L, 1L);
        verify(lessonDao).findByProfessorId(1L);
        verify(lessonDao).removeTeacherFromLesson(1L);
        verify(lessonDao).removeTeacherFromLesson(2L);
    }

    @Test
    void deleteShouldDoNothingIfArgumentProfessorDontExist() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        professorService.deleteById(professorId);

        verify(professorDao).findById(professorId);
    }

    @Test
    void removeRoleFromUserShouldRemoveRoleFromUserIfArgumentIsIdOfUserAndIdOfRole(){
        long professorId = 1L;
        long roleId = 2L;

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(roleDao.findById(roleId)).thenReturn(Optional.of(Role.builder().withId(roleId).build()));

        professorService.removeRoleFromUser(professorId, roleId);

        verify(professorDao).findById(professorId);
        verify(roleDao).findById(roleId);
    }

    @Test
    void removeRoleFromUserShouldTrowExceptionIfRoleNotExist(){
        long professorId = 1L;
        long roleId = 2L;

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(roleDao.findById(roleId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> professorService.removeRoleFromUser(professorId, roleId)).hasMessage("There no role with id: 2");

        verify(professorDao).findById(professorId);
        verify(roleDao).findById(roleId);
    }

}
