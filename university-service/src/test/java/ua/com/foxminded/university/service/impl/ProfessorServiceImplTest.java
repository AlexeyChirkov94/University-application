package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.repository.RoleRepository;
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
    ProfessorRepository professorRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    LessonRepository lessonRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    RoleRepository roleRepository;

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
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        doNothing().when(professorRepository).changeScienceDegree(professorId, newScienceDegreeId);

        professorService.changeScienceDegree(professorId, newScienceDegreeId);

        verify(scienceDegreeValidator).validate(ScienceDegree.getById(newScienceDegreeId));
        verify(professorRepository).findById(professorId);
        verify(professorRepository).changeScienceDegree(professorId, newScienceDegreeId);
    }

    @Test
    void changeDepartmentShouldChangeDepartmentOfProfessorIfArgumentIsProfessorIdAndDepartmentId() {
        long professorId = 1;
        long newDepartmentId = 2;

        doNothing().when(professorRepository).changeDepartment(professorId, newDepartmentId);
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().build()));
        when(departmentRepository.findById(newDepartmentId)).thenReturn(Optional.of(Department.builder().build()));

        professorService.changeDepartment(professorId, newDepartmentId);

        verify(professorRepository).changeDepartment(professorId, newDepartmentId);
        verify(professorRepository).findById(professorId);
        verify(departmentRepository).findById(newDepartmentId);
    }

    @Test
    void removeDepartmentShouldRemoveDepartmentOfProfessorIfArgumentIsProfessorId() {
        long professorId = 1;

        doNothing().when(professorRepository).removeDepartmentFromProfessor(professorId);
        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().build()));

        professorService.removeDepartmentFromProfessor(professorId);

        verify(professorRepository).removeDepartmentFromProfessor(professorId);
        verify(professorRepository).findById(professorId);
    }

    @Test
    void removeDepartmentShouldThrowExceptionIfProfessorNotExist() {
        long professorId = 1;

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.removeDepartmentFromProfessor(professorId))
                .hasMessage("There no user with id: 1");

        verify(professorRepository).findById(professorId);
    }

    @Test
    void findByCourseIdShouldReturnListOfProfessorIfArgumentIsCourseId() {
        long courseId = 1;
        List<Professor> professors = Arrays.asList(Professor.builder().withId(1L).build());
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        List<ProfessorResponse> professorResponses = Arrays.asList(professorResponse);

        when(professorMapper.mapEntityToDto(professors.get(0))).thenReturn(professorResponse);
        when(professorRepository.findAllByCourseId(courseId)).thenReturn(professors);

        assertThat(professorService.findByCourseId(courseId)).isEqualTo(professorResponses);

        verify(professorMapper).mapEntityToDto(professors.get(0));
        verify(professorRepository).findAllByCourseId(courseId);
    }

    @Test
    void findByDepartmentIdShouldReturnCoursesResponseIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(1L).build()));
        when(professorRepository.findAllByDepartmentId(departmentId)).thenReturn(Arrays.asList(Professor.builder().withId(1L).build(),
                Professor.builder().withId(2L).build()));

        professorService.findByDepartmentId(departmentId);

        verify(departmentRepository).findById(departmentId);
        verify(professorRepository).findAllByDepartmentId(departmentId);
    }

    @Test
    void findByDepartmentIdShouldThrowExceptionIfDepartmentNotExist() {
        long departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.findByDepartmentId(departmentId)).hasMessage("There no department with id: 1");

        verify(departmentRepository).findById(departmentId);
    }

    @Test
    void findByEmailShouldReturnOptionalOfProfessorResponseIfArgumentIsEmail() {
        String email= "Alexey94@gamil.com";
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);

        when(professorRepository.findAllByEmail(email)).thenReturn(Collections.singletonList(Professor.builder().withId(1L).build()));
        when(professorMapper.mapEntityToDto(professor)).thenReturn(professorResponse);

        professorService.findByEmail(email);

        verify(professorRepository).findAllByEmail(email);
        verify(professorMapper).mapEntityToDto(professor);
    }

    @Test
    void findByEmailShouldReturnOptionalOfEmptyProfessorResponseIfEmailNotRegistered() {
        String email= "Alexey94@gamil.com";

        when(professorRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());

        professorService.findByEmail(email);

        verify(professorRepository).findAllByEmail(email);
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
        when(professorRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(roleRepository.findAllByUserId(1L)).thenReturn(professorRole);

        professorService.register(professorRequest);

        verify(userValidator).validate(professorRequest);
        verify(professorRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorRepository).save(professor);
        verify(roleRepository).findAllByUserId(1L);
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
        when(professorRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(roleRepository.findAllByUserId(1L)).thenReturn(professorRole);
        when(roleRepository.findAllByName("ROLE_PROFESSOR")).thenReturn(Arrays.asList(defaultRole));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(roleRepository.findById(3L)).thenReturn(Optional.of(defaultRole));

        professorService.register(professorRequest);

        verify(userValidator).validate(professorRequest);
        verify(professorRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorRepository).save(professor);
        verify(roleRepository).findAllByUserId(1L);
        verify(roleRepository).findAllByUserId(1L);
        verify(roleRepository).findAllByName("ROLE_PROFESSOR");
        verify(professorRepository).findById(1L);
        verify(roleRepository).findById(3L);
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
        when(professorRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(roleRepository.findAllByUserId(1L)).thenReturn(professorRole);
        when(roleRepository.findAllByName("ROLE_PROFESSOR")).thenReturn(Collections.emptyList());

        Assertions.assertThatThrownBy(() -> professorService.register(professorRequest)).hasMessage("ROLE_PROFESSOR not initialized");

        verify(userValidator).validate(professorRequest);
        verify(professorRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorRepository).save(professor);
        verify(roleRepository).findAllByUserId(1L);
        verify(roleRepository).findAllByUserId(1L);
        verify(roleRepository).findAllByName("ROLE_PROFESSOR");
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
        when(professorRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorRepository.save(professor)).thenReturn(professor);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(roleRepository.findAllByUserId(1L)).thenReturn(professorRole);
        doNothing().when(professorRepository).changeDepartment(1L, 1L);
        doNothing().when(scienceDegreeValidator).validate(scienceDegree);
        doNothing().when(professorRepository).changeScienceDegree(1L, 1);

        professorService.register(professorRequest);

        verify(userValidator).validate(professorRequest);
        verify(professorRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorRepository).save(professor);
        verify(departmentRepository).findById(1L);
        verify(professorRepository,times(2)).findById(1L);
        verify(roleRepository).findAllByUserId(1L);
        verify(professorRepository).changeDepartment(1L, 1L);
        verify(scienceDegreeValidator).validate(scienceDegree);
        verify(professorRepository).changeScienceDegree(1L, 1);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfProfessorWithSameNameAlreadyExist() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorRepository.findAllByEmail(email)).thenReturn(Collections.singletonList(Professor.builder().withEmail(email).build()));

        assertThatThrownBy(() -> professorService.register(professorRequest)).hasMessage("This email already registered");

        verify(userValidator).validate(professorRequest);
        verify(professorRepository).findAllByEmail(email);
    }

    @Test
    void findByIdShouldReturnProfessorResponseIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(1L).build()));

        professorService.findById(professorId);

        verify(professorRepository).findById(professorId);
    }

    @Test
    void findByIdShouldThrowExceptionIfProfessorNotExist() {
        long professorId = 1;

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> professorService.findById(professorId)).hasMessage("There no professor with id: 1");

        verify(professorRepository).findById(professorId);
    }

    @Test
    void findByIdShouldThrowExceptionIfProfessorNotExistIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> professorService.findById(professorId)).hasMessage("There no professor with id: 1");

        verify(professorRepository).findById(professorId);
    }

    @Test
    void findAllIdShouldReturnListOfProfessorResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(professorRepository.count()).thenReturn(11L);
        when(professorRepository.findAll(PageRequest.of(1, 5))).thenReturn(new PageImpl(Collections.singletonList(Professor.builder().withId(1L).build())));


        professorService.findAll(pageNumber);

        verify(professorRepository).count();
        verify(professorRepository).findAll(PageRequest.of(1, 5));
    }

    @Test
    void findAllIdShouldReturnListOfProfessorResponseNoArguments() {
        when(professorRepository.findAll()).thenReturn(Arrays.asList(Professor.builder().withId(1L).build()));

        professorService.findAll();

        verify(professorRepository).findAll();
    }

    @Test
    void editShouldEditDataOfProfessorIfArgumentNewProfessorRequestWithoutDepartmentAndScienceDegree() {
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setDepartmentId(0L);
        professorRequest.setScienceDegreeId(0);

        when(professorMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        when(professorRepository.save(professor)).thenReturn(professor);

        professorService.edit(professorRequest);

        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorRepository).save(professor);
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
        when(professorRepository.save(professor)).thenReturn(professor);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        doNothing().when(professorRepository).changeDepartment(1L, 1L);
        doNothing().when(scienceDegreeValidator).validate(scienceDegree);
        doNothing().when(professorRepository).changeScienceDegree(1L, 1);

        professorService.edit(professorRequest);

        verify(professorMapper).mapDtoToEntity(professorRequest);
        verify(professorRepository).save(professor);
        verify(departmentRepository).findById(1L);
        verify(professorRepository,times(2)).findById(1L);
        verify(professorRepository).changeDepartment(1L, 1L);
        verify(scienceDegreeValidator).validate(scienceDegree);
        verify(professorRepository).changeScienceDegree(1L, 1);
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

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(roleRepository.findAllByUserId(professorId)).thenReturn(professorRoles);
        doNothing().when(professorRepository).removeRoleFromUser(1L, 2L);
        doNothing().when(professorRepository).deleteById(professorId);
        when(courseRepository.findByProfessorId(1L)).thenReturn(professorCourses);
        doNothing().when(courseRepository).removeCourseFromProfessorCourseList(1L, 1L);
        doNothing().when(courseRepository).removeCourseFromProfessorCourseList(2L, 1L);
        when(lessonRepository.findAllByTeacherIdOrderByTimeOfStartLesson(1L)).thenReturn(professorLessons);
        doNothing().when(lessonRepository).removeTeacherFromLesson(1L);
        doNothing().when(lessonRepository).removeTeacherFromLesson(2L);

        professorService.deleteById(professorId);

        verify(professorRepository, times(2)).findById(professorId);
        verify(roleRepository).findAllByUserId(professorId);
        verify(professorRepository).removeRoleFromUser(1L, 2L);
        verify(professorRepository).deleteById(professorId);
        verify(courseRepository).findByProfessorId(1L);
        verify(courseRepository).removeCourseFromProfessorCourseList(1L, 1L);
        verify(courseRepository).removeCourseFromProfessorCourseList(2L, 1L);
        verify(lessonRepository).findAllByTeacherIdOrderByTimeOfStartLesson(1L);
        verify(lessonRepository).removeTeacherFromLesson(1L);
        verify(lessonRepository).removeTeacherFromLesson(2L);
    }

    @Test
    void deleteShouldDoNothingIfArgumentProfessorDontExist() {
        long professorId = 1;

        when(professorRepository.findById(professorId)).thenReturn(Optional.empty());

        professorService.deleteById(professorId);

        verify(professorRepository).findById(professorId);
    }

    @Test
    void removeRoleFromUserShouldRemoveRoleFromUserIfArgumentIsIdOfUserAndIdOfRole(){
        long professorId = 1L;
        long roleId = 2L;

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(Role.builder().withId(roleId).build()));

        professorService.removeRoleFromUser(professorId, roleId);

        verify(professorRepository).findById(professorId);
        verify(roleRepository).findById(roleId);
    }

    @Test
    void removeRoleFromUserShouldTrowExceptionIfRoleNotExist(){
        long professorId = 1L;
        long roleId = 2L;

        when(professorRepository.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> professorService.removeRoleFromUser(professorId, roleId)).hasMessage("There no role with id: 2");

        verify(professorRepository).findById(professorId);
        verify(roleRepository).findById(roleId);
    }

}
