package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
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
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.DepartmentMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    ProfessorRepository professorRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    DepartmentMapper departmentMapper;

    @InjectMocks
    DepartmentServiceImpl departmentService;

    @Test
    void registerShouldAddDepartmentToDBIfArgumentsIsDepartmentRequest() {
        String departmentName= "Department of Math";
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName(departmentName);

        when(departmentRepository.findAllByName(departmentName)).thenReturn(Collections.emptyList());

        departmentService.create(departmentRequest);

        verify(departmentRepository).findAllByName(departmentName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfDepartmentWithSameNameAlreadyExist() {
        String departmentName= "Department of Math";
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName(departmentName);

        when(departmentRepository.findAllByName(departmentName)).thenReturn(Arrays.asList(Department.builder().withName(departmentName).build()));

        assertThatThrownBy(() -> departmentService.create(departmentRequest)).hasMessage("Department with same name already exist");

        verify(departmentRepository).findAllByName(departmentName);
    }

    @Test
    void findByIdShouldReturnDepartmentResponseIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(1L).build()));

        departmentService.findById(departmentId);

        verify(departmentRepository).findById(departmentId);
    }

    @Test
    void findByIdShouldThrowExceptionIfDepartmentNotExistIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> departmentService.findById(departmentId)).hasMessage("There no department with id: 1");

        verify(departmentRepository).findById(departmentId);
    }

    @Test
    void findAllIdShouldReturnListOfDepartmentResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(departmentRepository.count()).thenReturn(11L);
        when(departmentRepository.findAll(PageRequest.of(1, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Department.builder().withId(1L).build())));

        departmentService.findAll(pageNumber);

        verify(departmentRepository).count();
        verify(departmentRepository).findAll(PageRequest.of(1, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfDepartmentResponseNoArguments() {
        when(departmentRepository.findAll(Sort.by("id"))).thenReturn(Collections.singletonList(Department.builder().withId(1L).build()));

        departmentService.findAll();

        verify(departmentRepository).findAll(Sort.by("id"));
    }

    @Test
    void editShouldEditDataOfDepartmentIfArgumentNewDepartmentRequest() {
        Department department = Department.builder().withId(1L).build();
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(1L);

        when(departmentMapper.mapDtoToEntity(departmentRequest)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);

        departmentService.edit(departmentRequest);

        verify(departmentMapper).mapDtoToEntity(departmentRequest);
        verify(departmentRepository).save(department);
    }

    @Test
    void deleteShouldDeleteDataOfDepartmentIfArgumentIsDepartmentId() {
        long departmentId = 1;

        List<Professor> departmentsProfessors = Arrays.asList(Professor.builder().withId(1L).build(), Professor.builder().withId(2L).build());
        List<Course> departmentsCourses = Arrays.asList(Course.builder().withId(3L).build(), Course.builder().withId(4L).build());
        List<Group> departmentsGroup = Arrays.asList(Group.builder().withId(5L).build(), Group.builder().withId(6L).build());

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(departmentId).build()));
        when(professorRepository.findAllByDepartmentId(departmentId)).thenReturn(departmentsProfessors);
        when(courseRepository.findByDepartmentId(departmentId)).thenReturn(departmentsCourses);
        when(groupRepository.findAllByDepartmentIdOrderById(departmentId)).thenReturn(departmentsGroup);
        doNothing().when(professorRepository).removeDepartmentFromProfessor(1L);
        doNothing().when(professorRepository).removeDepartmentFromProfessor(2L);
        doNothing().when(courseRepository).removeDepartmentFromCourse(3L);
        doNothing().when(courseRepository).removeDepartmentFromCourse(4L);
        doNothing().when(groupRepository).removeDepartmentFromGroup(5L);
        doNothing().when(groupRepository).removeDepartmentFromGroup(6L);
        doNothing().when(departmentRepository).deleteById(departmentId);

        departmentService.deleteById(departmentId);

        verify(departmentRepository).findById(departmentId);
        verify(professorRepository).findAllByDepartmentId(departmentId);
        verify(courseRepository).findByDepartmentId(departmentId);
        verify(groupRepository).findAllByDepartmentIdOrderById(departmentId);
        verify(professorRepository).removeDepartmentFromProfessor(1L);
        verify(professorRepository).removeDepartmentFromProfessor(2L);
        verify(courseRepository).removeDepartmentFromCourse(3L);
        verify(courseRepository).removeDepartmentFromCourse(4L);
        verify(groupRepository).removeDepartmentFromGroup(5L);
        verify(groupRepository).removeDepartmentFromGroup(6L);
        verify(departmentRepository).deleteById(departmentId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentDepartmentDontExist() {
        long departmentId = 1;

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        departmentService.deleteById(departmentId);

        verify(departmentRepository).findById(departmentId);
    }

}
