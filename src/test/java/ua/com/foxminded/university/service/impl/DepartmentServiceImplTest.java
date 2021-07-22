package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.mapper.interfaces.DepartmentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;
import java.util.Arrays;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentDao departmentDao;

    @Mock
    private DepartmentRequestMapper departmentRequestMapper;

    @Mock
    private DepartmentResponseMapper departmentResponseMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @Test
    void registerShouldAddDepartmentToDBIfArgumentsIsDepartmentRequest() {
        String departmentName= "Department of Math";
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName(departmentName);

        when(departmentDao.findByName(departmentName)).thenReturn(Optional.empty());

        departmentService.register(departmentRequest);

        verify(departmentDao).findByName(departmentName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfDepartmentWithSameNameAlreadyExist() {
        String departmentName= "Department of Math";
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName(departmentName);

        when(departmentDao.findByName(departmentName)).thenReturn(Optional.of(Department.builder().withName(departmentName).build()));

        assertThatThrownBy(() -> departmentService.register(departmentRequest)).hasMessage("Department with same name already exist");

        verify(departmentDao).findByName(departmentName);
    }

    @Test
    void findByIdShouldReturnOptionalOfDepartmentResponseIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(1L).build()));

        departmentService.findById(departmentId);

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
    void editShouldEditDataOfDepartmentIfArgumentNewDepartmentRequest() {
        Department department = Department.builder().withId(1L).build();
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(1L);

        when(departmentRequestMapper.mapDtoToEntity(departmentRequest)).thenReturn(department);
        doNothing().when(departmentDao).update(department);

        departmentService.edit(departmentRequest);

        verify(departmentRequestMapper).mapDtoToEntity(departmentRequest);
        verify(departmentDao).update(department);
    }

    @Test
    void deleteShouldDeleteDataOfDepartmentIfArgumentIsDepartmentId() {
        long departmentId = 1;

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(departmentId).build()));
        when(departmentDao.deleteById(departmentId)).thenReturn(true);

        departmentService.deleteById(departmentId);

        verify(departmentDao).findById(departmentId);
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
