package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.StudentMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    RoleRepository roleRepository;

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

        when(studentRepository.findAllByEmail(email)).thenReturn(Collections.singletonList(Student.builder().withId(1L).build()));
        when(studentMapper.mapEntityToDto(student)).thenReturn(studentResponse);

        studentService.findByEmail(email);

        verify(studentRepository).findAllByEmail(email);
        verify(studentMapper).mapEntityToDto(student);
    }

    @Test
    void findByEmailShouldReturnOptionalOfEmptyStudentResponseIfEmailNotRegistered() {
        String email= "Alexey94@gamil.com";

        when(studentRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());

        studentService.findByEmail(email);

        verify(studentRepository).findAllByEmail(email);
    }

    @Test
    void leaveGroupShouldDeleteStudentFromGroupIfArgumentsIsStudentId() {
        long studentId = 1;

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));
        doNothing().when(studentRepository).leaveGroup(studentId);

        studentService.leaveGroup(studentId);

        verify(studentRepository).findById(studentId);
        verify(studentRepository).leaveGroup(studentId);
    }

    @Test
    void leaveGroupShouldDoNothingIfStudentDontExist() {
        long studentId = 1;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        studentService.leaveGroup(studentId);

        verify(studentRepository).findById(studentId);
    }

    @Test
    void enterGroupShouldAddStudentToGroupIfArgumentsAreStudentIdAndGroupId() {
        long studentId = 1;
        long groupId = 2;

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));
        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(2L).build()));
        doNothing().when(studentRepository).enterGroup(studentId, groupId);

        studentService.changeGroup(studentId, groupId);

        verify(studentRepository).findById(studentId);
        verify(groupRepository).findById(groupId);
        verify(studentRepository).enterGroup(studentId, groupId);
    }

    @Test
    void enterGroupShouldDoNothingIfStudentDontExist() {
        long studentId = 1;
        long groupId = 2;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        studentService.changeGroup(studentId, groupId);

        verify(studentRepository).findById(studentId);
    }

    @Test
    void enterGroupShouldDoNothingIfGroupDontExist() {
        long studentId = 1;
        long groupId = 2;

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));
        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        studentService.changeGroup(studentId, groupId);

        verify(studentRepository).findById(studentId);
        verify(groupRepository).findById(groupId);
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

        when(studentRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(roleRepository.findAllByUserId(1L)).thenReturn(studentRole);
        doNothing().when(studentRepository).enterGroup(1L, 1L);

        studentService.register(studentRequest);

        verify(studentRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentRepository).save(student);
        verify(studentRepository).findById(1L);
        verify(groupRepository).findById(1L);
        verify(roleRepository).findAllByUserId(1L);
        verify(studentRepository).enterGroup(1L, 1L);
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

        when(studentRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(roleRepository.findAllByUserId(1L)).thenReturn(studentRole);

        studentService.register(studentRequest);

        verify(studentRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentRepository).save(student);
        verify(roleRepository).findAllByUserId(1L);
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

        when(studentRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(roleRepository.findAllByUserId(1L)).thenReturn(studentRole);
        when(roleRepository.findAllByName("ROLE_STUDENT")).thenReturn(Arrays.asList(defaultRole));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(roleRepository.findById(3L)).thenReturn(Optional.of(defaultRole));

        studentService.register(studentRequest);

        verify(studentRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentRepository).save(student);
        verify(roleRepository).findAllByUserId(1L);
        verify(roleRepository).findAllByName("ROLE_STUDENT");
        verify(studentRepository).findById(1L);
        verify(roleRepository).findById(3L);
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

        when(studentRepository.findAllByEmail(email)).thenReturn(Collections.emptyList());
        when(passwordEncoder.encode(password)).thenReturn(password);
        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(roleRepository.findAllByUserId(1L)).thenReturn(studentRole);
        when(roleRepository.findAllByName("ROLE_STUDENT")).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> studentService.register(studentRequest)).hasMessage("ROLE_STUDENT not initialized");

        verify(studentRepository).findAllByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentRepository).save(student);
        verify(roleRepository).findAllByUserId(1L);
        verify(roleRepository).findAllByName("ROLE_STUDENT");
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfStudentWithSameNameAlreadyExist() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);

        when(studentRepository.findAllByEmail(email)).thenReturn(Collections.singletonList(Student.builder().withEmail(email).build()));

        assertThatThrownBy(() -> studentService.register(studentRequest)).hasMessage("This email already registered");

        verify(studentRepository).findAllByEmail(email);
    }

    @Test
    void findByIdShouldReturnStudentResponseIfArgumentIsStudentId() {
        long studentId = 1;

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(1L).build()));

        studentService.findById(studentId);

        verify(studentRepository).findById(studentId);
    }

    @Test
    void findByIdShouldThrowExceptionIfStudentNotExistIfArgumentIsProfessorId() {
        long studentId = 1;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> studentService.findById(studentId)).hasMessage("There no student with id: 1");

        verify(studentRepository).findById(studentId);
    }

    @Test
    void findByGroupIdShouldReturnListOfStudentResponseIfArgumentIsGroupId() {
        long groupId = 1;

        when(studentRepository.findAllByGroupId(groupId)).thenReturn(Arrays.asList(Student.builder().withId(1L).build()));

        studentService.findByGroupId(groupId);

        verify(studentRepository).findAllByGroupId(groupId);
    }

    @Test
    void findAllIdShouldReturnListOfStudentResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(studentRepository.count()).thenReturn(11L);
        when(studentRepository.findAll(PageRequest.of(1, 5))).thenReturn(new PageImpl(Collections.singletonList(Student.builder().withId(1L).build())));

        studentService.findAll(pageNumber);

        verify(studentRepository).count();
        verify(studentRepository).findAll(PageRequest.of(1, 5));
    }

    @Test
    void findAllIdShouldReturnListOfStudentResponseNoArguments() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(Student.builder().withId(1L).build()));

        studentService.findAll();

        verify(studentRepository).findAll();
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
        when(studentRepository.save(student)).thenReturn(student);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        doNothing().when(studentRepository).enterGroup(1L, 1L);

        studentService.edit(studentRequest);

        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentRepository).save(student);
        verify(studentRepository).findById(1L);
        verify(groupRepository).findById(1L);
        verify(studentRepository).enterGroup(1L, 1L);
    }

    @Test
    void editShouldEditDataOfStudentIfArgumentNewStudentRequestWithoutGroup() {
        Student student = Student.builder().withId(1L).build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1L);
        studentRequest.setGroupId(0L);

        when(studentMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);

        studentService.edit(studentRequest);

        verify(studentMapper).mapDtoToEntity(studentRequest);
        verify(studentRepository).save(student);
    }

    @Test
    void deleteShouldDeleteDataOfStudentIfArgumentIsStudentId() {
        long studentId = 1;
        List<Role> professorRoles = Collections.singletonList(Role.builder().withId(2L).withName("ROLE_STUDENT").build());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(studentId).build()));
        doNothing().when(studentRepository).deleteById(studentId);
        when(roleRepository.findAllByUserId(studentId)).thenReturn(professorRoles);
        doNothing().when(studentRepository).removeRoleFromUser(1L, 2L);

        studentService.deleteById(studentId);

        verify(studentRepository, times(2)).findById(studentId);
        verify(roleRepository).findAllByUserId(studentId);
        verify(studentRepository).removeRoleFromUser(1L, 2L);
        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentStudentDontExist() {
        long studentId = 1;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        studentService.deleteById(studentId);

        verify(studentRepository).findById(studentId);
    }

}
