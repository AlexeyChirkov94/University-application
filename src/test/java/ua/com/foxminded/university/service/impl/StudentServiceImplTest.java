package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.RoleDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.StudentMapper;
import ua.com.foxminded.university.service.validator.UserValidator;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    StudentDao studentDao;

    @Mock
    GroupDao groupDao;

    @Mock
    RoleDao roleDao;

    @Mock
    UserValidator userValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    StudentMapper studentMapper;

    @InjectMocks
    StudentServiceImpl studentService;

    @Test
    void findByEmailShouldReturnOptionalOfStudentResponseIfArgumentIsEmail() {
        String email= "Alexey94@gamil.com";
        Student student = Student.builder().withId(1L).build();
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(1L);

        when(studentDao.findByEmail(email)).thenReturn(Optional.of(Student.builder().withId(1L).build()));
        when(studentMapper.mapEntityToDto(student)).thenReturn(studentResponse);

        studentService.findByEmail(email);

        verify(studentDao).findByEmail(email);
        verify(studentMapper).mapEntityToDto(student);
    }

    @Test
    void findByEmailShouldReturnOptionalOfEmptyStudentResponseIfEmailNotRegistered() {
        String email= "Alexey94@gamil.com";

        when(studentDao.findByEmail(email)).thenReturn(Optional.empty());

        studentService.findByEmail(email);

        verify(studentDao).findByEmail(email);
    }

    @Test
    void leaveGroupShouldDeleteStudentFromGroupIfArgumentsIsStudentId() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));
        doNothing().when(studentDao).leaveGroup(studentId);

        studentService.leaveGroup(studentId);

        verify(studentDao).findById(studentId);
        verify(studentDao).leaveGroup(studentId);
    }

    @Test
    void leaveGroupShouldDoNothingIfStudentDontExist() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        studentService.leaveGroup(studentId);

        verify(studentDao).findById(studentId);
    }

    @Test
    void enterGroupShouldAddStudentToGroupIfArgumentsAreStudentIdAndGroupId() {
        long studentId = 1;
        long groupId = 2;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));
        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(2L).build()));
        doNothing().when(studentDao).enterGroup(studentId, groupId);

        studentService.changeGroup(studentId, groupId);

        verify(studentDao).findById(studentId);
        verify(groupDao).findById(groupId);
        verify(studentDao).enterGroup(studentId, groupId);
    }

    @Test
    void enterGroupShouldDoNothingIfStudentDontExist() {
        long studentId = 1;
        long groupId = 2;

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        studentService.changeGroup(studentId, groupId);

        verify(studentDao).findById(studentId);
    }

    @Test
    void enterGroupShouldDoNothingIfGroupDontExist() {
        long studentId = 1;
        long groupId = 2;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));
        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        studentService.changeGroup(studentId, groupId);

        verify(studentDao).findById(studentId);
        verify(groupDao).findById(groupId);
    }

    @Test
    void registerShouldAddStudentToDBIfArgumentsIsStudentRequestWithGroup() {
        String email= "Alexey94@gamil.com";
        String password = "12345";
        List<Role> studentRole = Collections.singletonList(Role.builder().withName("ROLE_STUDENT").build());
        Student student = Student.builder().withId(1L).withFirstName("Alex").withEmail(email).build();
        Group group = Group.builder().withId(1L).withName("Group").build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);
        studentRequest.setGroupId(1L);

        doNothing().when(userValidator).validate(studentRequest);
        when(studentDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentDao.save(student)).thenReturn(student);
        when(studentDao.findById(1L)).thenReturn(Optional.of(student));
        when(groupDao.findById(1L)).thenReturn(Optional.of(group));
        when(roleDao.findByUserId(1L)).thenReturn(studentRole);
        doNothing().when(studentDao).enterGroup(1L, 1L);

        studentService.register(studentRequest);

        verify(userValidator).validate(studentRequest);
        verify(studentDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentDao).save(student);
        verify(studentDao).findById(1L);
        verify(groupDao).findById(1L);
        verify(roleDao).findByUserId(1L);
        verify(studentDao).enterGroup(1L, 1L);
    }

    @Test
    void registerShouldAddStudentToDBIfArgumentsIsStudentRequestWithoutGroup() {
        String email= "Alexey94@gamil.com";
        String password = "12345";
        List<Role> studentRole = Collections.singletonList(Role.builder().withName("ROLE_STUDENT").build());
        Student student = Student.builder().withId(1L).withFirstName("Alex").withEmail(email).build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);
        studentRequest.setGroupId(0L);

        doNothing().when(userValidator).validate(studentRequest);
        when(studentDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentDao.save(student)).thenReturn(student);
        when(roleDao.findByUserId(1L)).thenReturn(studentRole);

        studentService.register(studentRequest);

        verify(userValidator).validate(studentRequest);
        verify(studentDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentDao).save(student);
        verify(roleDao).findByUserId(1L);
    }

    @Test
    void registerWithAddingDefaultRoleShouldAddStudentToDBAndAddRoleToStudentIfArgumentsIsStudentRequestWithoutGroup() {
        String email= "Alexey94@gamil.com";
        String password = "12345";
        List<Role> studentRole = Collections.emptyList();
        Role defaultRole = Role.builder().withId(3L).withName("ROLE_STUDENT").build();
        Student student = Student.builder().withId(1L).withFirstName("Alex").withEmail(email).build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);
        studentRequest.setGroupId(0L);

        doNothing().when(userValidator).validate(studentRequest);
        when(studentDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentDao.save(student)).thenReturn(student);
        when(roleDao.findByUserId(1L)).thenReturn(studentRole);
        when(roleDao.findByName("ROLE_STUDENT")).thenReturn(Optional.of(defaultRole));
        when(studentDao.findById(1L)).thenReturn(Optional.of(student));
        when(roleDao.findById(3L)).thenReturn(Optional.of(defaultRole));

        studentService.register(studentRequest);

        verify(userValidator).validate(studentRequest);
        verify(studentDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentDao).save(student);
        verify(roleDao).findByUserId(1L);
        verify(roleDao).findByName("ROLE_STUDENT");
        verify(studentDao).findById(1L);
        verify(roleDao).findById(3L);
    }

    @Test
    void registerWithAddingDefaultRoleShouldThrowExceptionIfDefaultRoleNotExist() {
        String email= "Alexey94@gamil.com";
        String password = "12345";
        List<Role> studentRole = Collections.emptyList();
        Student student = Student.builder().withId(1L).withFirstName("Alex").withEmail(email).build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);
        studentRequest.setGroupId(0L);

        doNothing().when(userValidator).validate(studentRequest);
        when(studentDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentDao.save(student)).thenReturn(student);
        when(roleDao.findByUserId(1L)).thenReturn(studentRole);
        when(roleDao.findByName("ROLE_STUDENT")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.register(studentRequest)).hasMessage("ROLE_STUDENT not initialized");

        verify(userValidator).validate(studentRequest);
        verify(studentDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentDao).save(student);
        verify(roleDao).findByUserId(1L);
        verify(roleDao).findByName("ROLE_STUDENT");
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfStudentWithSameNameAlreadyExist() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);

        doNothing().when(userValidator).validate(studentRequest);
        when(studentDao.findByEmail(email)).thenReturn(Optional.of(Student.builder().withEmail(email).build()));

        assertThatThrownBy(() -> studentService.register(studentRequest)).hasMessage("This email already registered");

        verify(userValidator).validate(studentRequest);
        verify(studentDao).findByEmail(email);
    }

    @Test
    void findByIdShouldReturnStudentResponseIfArgumentIsStudentId() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));

        studentService.findById(studentId);

        verify(studentDao).findById(studentId);
    }

    @Test
    void findByIdShouldThrowExceptionIfStudentNotExistIfArgumentIsProfessorId() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> studentService.findById(studentId)).hasMessage("There no student with id: 1");

        verify(studentDao).findById(studentId);
    }

    @Test
    void findByGroupIdShouldReturnListOfStudentResponseIfArgumentIsGroupId() {
        long groupId = 1;

        when(studentDao.findByGroupId(groupId)).thenReturn(Arrays.asList(Student.builder().withId(1L).build()));

        studentService.findByGroupId(groupId);

        verify(studentDao).findByGroupId(groupId);
    }

    @Test
    void findAllIdShouldReturnListOfStudentResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(studentDao.count()).thenReturn(11L);
        when(studentDao.findAll(2, 5)).thenReturn(Arrays.asList(Student.builder().withId(1L).build()));

        studentService.findAll(pageNumber);

        verify(studentDao).count();
        verify(studentDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfStudentResponseNoArguments() {
        when(studentDao.findAll()).thenReturn(Arrays.asList(Student.builder().withId(1L).build()));

        studentService.findAll();

        verify(studentDao).findAll();
    }

    @Test
    void editShouldEditDataOfStudentIfArgumentNewStudentRequestWithGroup() {
        String email= "Alexey94@gamil.com";
        String password = "12345";
        Student student = Student.builder().withId(1L).withFirstName("Alex").withEmail(email).build();
        Group group = Group.builder().withId(1L).withName("Group").build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1L);
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);
        studentRequest.setGroupId(1L);

        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        doNothing().when(studentDao).update(student);
        when(studentDao.findById(1L)).thenReturn(Optional.of(student));
        when(groupDao.findById(1L)).thenReturn(Optional.of(group));
        doNothing().when(studentDao).enterGroup(1L, 1L);

        studentService.edit(studentRequest);

        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentDao).update(student);
        verify(studentDao).findById(1L);
        verify(groupDao).findById(1L);
        verify(studentDao).enterGroup(1L, 1L);
    }

    @Test
    void editShouldEditDataOfStudentIfArgumentNewStudentRequestWithoutGroup() {
        Student student = Student.builder().withId(1L).build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1L);
        studentRequest.setGroupId(0L);

        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        doNothing().when(studentDao).update(student);

        studentService.edit(studentRequest);

        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentDao).update(student);
    }

    @Test
    void deleteShouldDeleteDataOfStudentIfArgumentIsStudentId() {
        long studentId = 1;
        List<Role> professorRoles = Collections.singletonList(Role.builder().withId(2L).withName("ROLE_STUDENT").build());

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(studentId).build()));
        when(studentDao.deleteById(studentId)).thenReturn(true);
        when(roleDao.findByUserId(studentId)).thenReturn(professorRoles);
        doNothing().when(studentDao).removeRoleFromUser(1L, 2L);

        studentService.deleteById(studentId);

        verify(studentDao, times(2)).findById(studentId);
        verify(roleDao).findByUserId(studentId);
        verify(studentDao).removeRoleFromUser(1L, 2L);
        verify(studentDao).deleteById(studentId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentStudentDontExist() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        studentService.deleteById(studentId);

        verify(studentDao).findById(studentId);
    }

}
