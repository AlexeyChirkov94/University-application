package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.mapper.DepartmentMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    DepartmentDao departmentDao;

    @Mock
    CourseDao courseDao;

    @Mock
    ProfessorDao professorDao;

    @Mock
    GroupDao groupDao;

    @Mock
    DepartmentMapper departmentMapper;

    @InjectMocks
    DepartmentServiceImpl departmentService;

    @Test
    void registerShouldAddDepartmentToDBIfArgumentsIsDepartmentRequest() {
        String departmentName= "Department of Math";
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName(departmentName);

        when(departmentDao.findByName(departmentName)).thenReturn(Optional.empty());

        departmentService.create(departmentRequest);

        verify(departmentDao).findByName(departmentName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfDepartmentWithSameNameAlreadyExist() {
        String departmentName= "Department of Math";
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName(departmentName);

        when(departmentDao.findByName(departmentName)).thenReturn(Optional.of(Department.builder().withName(departmentName).build()));

        assertThatThrownBy(() -> departmentService.create(departmentRequest)).hasMessage("Department with same name already exist");

        verify(departmentDao).findByName(departmentName);
    }

    @Test
    void findByIdShouldReturnDepartmentResponseIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(1L).build()));

        departmentService.findById(departmentId);

        verify(departmentDao).findById(departmentId);
    }

    @Test
    void findByIdShouldThrowExceptionIfDepartmentNotExistIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> departmentService.findById(departmentId)).hasMessage("There no department with id: 1");

        verify(departmentDao).findById(departmentId);
    }

    @Test
    void findAllIdShouldReturnListOfDepartmentResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(departmentDao.count()).thenReturn(11L);
        when(departmentDao.findAll(2, 5)).thenReturn(Arrays.asList(Department.builder().withId(1L).build()));

        departmentService.findAll(pageNumber);

        verify(departmentDao).count();
        verify(departmentDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfDepartmentResponseNoArguments() {
        when(departmentDao.findAll()).thenReturn(Arrays.asList(Department.builder().withId(1L).build()));

        departmentService.findAll();

        verify(departmentDao).findAll();
    }

    @Test
    void editShouldEditDataOfDepartmentIfArgumentNewDepartmentRequest() {
        Department department = Department.builder().withId(1L).build();
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(1L);

        when(departmentMapper.mapDtoToEntity(departmentRequest)).thenReturn(department);
        doNothing().when(departmentDao).update(department);

        departmentService.edit(departmentRequest);

        verify(departmentMapper).mapDtoToEntity(departmentRequest);
        verify(departmentDao).update(department);
    }

    @Test
    void deleteShouldDeleteDataOfDepartmentIfArgumentIsDepartmentId() {
        long departmentId = 1;

        List<Professor> departmentsProfessors = Arrays.asList(Professor.builder().withId(1L).build(), Professor.builder().withId(2L).build());
        List<Course> departmentsCourses = Arrays.asList(Course.builder().withId(3L).build(), Course.builder().withId(4L).build());
        List<Group> departmentsGroup = Arrays.asList(Group.builder().withId(5L).build(), Group.builder().withId(6L).build());

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(departmentId).build()));
        when(professorDao.findByDepartmentId(departmentId)).thenReturn(departmentsProfessors);
        when(courseDao.findByDepartmentId(departmentId)).thenReturn(departmentsCourses);
        when(groupDao.findByDepartmentId(departmentId)).thenReturn(departmentsGroup);
        doNothing().when(professorDao).removeDepartmentFromProfessor(1L);
        doNothing().when(professorDao).removeDepartmentFromProfessor(2L);
        doNothing().when(courseDao).removeDepartmentFromCourse(3L);
        doNothing().when(courseDao).removeDepartmentFromCourse(4L);
        doNothing().when(groupDao).removeDepartmentFromGroup(5L);
        doNothing().when(groupDao).removeDepartmentFromGroup(6L);
        when(departmentDao.deleteById(departmentId)).thenReturn(true);

        departmentService.deleteById(departmentId);

        verify(departmentDao).findById(departmentId);
        verify(professorDao).findByDepartmentId(departmentId);
        verify(courseDao).findByDepartmentId(departmentId);
        verify(groupDao).findByDepartmentId(departmentId);
        verify(professorDao).removeDepartmentFromProfessor(1L);
        verify(professorDao).removeDepartmentFromProfessor(2L);
        verify(courseDao).removeDepartmentFromCourse(3L);
        verify(courseDao).removeDepartmentFromCourse(4L);
        verify(groupDao).removeDepartmentFromGroup(5L);
        verify(groupDao).removeDepartmentFromGroup(6L);
        verify(departmentDao).deleteById(departmentId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentDepartmentDontExist() {
        long departmentId = 1;

        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        departmentService.deleteById(departmentId);

        verify(departmentDao).findById(departmentId);
    }

}
