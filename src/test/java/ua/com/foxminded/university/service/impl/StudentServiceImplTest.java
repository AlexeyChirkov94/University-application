package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.StudentRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.StudentResponseMapper;
import ua.com.foxminded.university.service.validator.UserValidator;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.util.Arrays;
import java.util.Optional;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class StudentServiceImplTest {

    @Mock
    private StudentDao studentDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private UserValidator userValidator;

    @Mock
    protected PasswordEncoder passwordEncoder;

    @Mock
    private StudentRequestMapper studentRequestMapper;

    @Mock
    private StudentResponseMapper studentResponseMapper;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void loginShouldReturnTrueIfArgumentEmailAndPasswordIsEqualEmailAndPasswordFromDB() {
        String email= "Alexey94@gamil.com";
        String password= "12345";

        when(studentDao.findByEmail(email)).thenReturn(Optional.of(Student.builder().withEmail(email).withPassword(password)
                .withId((long)1).build()));
        when(passwordEncoder.matches(password, password)).thenReturn(true);


        studentService.login(email, password);

        verify(studentDao).findByEmail(email);
    }

    @Test
    void findByEmailShouldReturnOptionalOfStudentResponseIfArgumentIsEmail() {
        String email= "Alexey94@gamil.com";

        when(studentDao.findByEmail(email)).thenReturn(Optional.of(Student.builder().withId((long)1).build()));

        studentService.findByEmail(email);

        verify(studentDao).findByEmail(email);
    }

    @Test
    void leaveGroupShouldDeleteStudentFromGroupIfArgumentsIsStudentId() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId((long)1).build()));
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

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId((long)1).build()));
        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId((long)2).build()));
        doNothing().when(studentDao).enterGroup(studentId, groupId);

        studentService.enterGroup(studentId, groupId);

        verify(studentDao).findById(studentId);
        verify(groupDao).findById(groupId);
        verify(studentDao).enterGroup(studentId, groupId);
    }

    @Test
    void enterGroupShouldDoNothingIfStudentDontExist() {
        long studentId = 1;
        long groupId = 2;

        when(studentDao.findById(studentId)).thenReturn(Optional.empty());

        studentService.enterGroup(studentId, groupId);

        verify(studentDao).findById(studentId);
    }

    @Test
    void enterGroupShouldDoNothingIfGroupDontExist() {
        long studentId = 1;
        long groupId = 2;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId((long)1).build()));
        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        studentService.enterGroup(studentId, groupId);

        verify(studentDao).findById(studentId);
        verify(groupDao).findById(groupId);
    }

    @Test
    void registerShouldAddStudentToDBIfArgumentsIsStudentRequest() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setEmail(email);
        studentRequest.setPassword(password);

        doNothing().when(userValidator).validate(studentRequest);
        when(studentDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);

        studentService.register(studentRequest);

        verify(userValidator).validate(studentRequest);
        verify(studentDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
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
    void findByIdShouldReturnOptionalOfStudentResponseIfArgumentIsStudentId() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId((long)1).build()));

        studentService.findById(studentId);

        verify(studentDao).findById(studentId);
    }

    @Test
    void findAllIdShouldReturnListOfStudentResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(studentDao.count()).thenReturn((long)11);
        when(studentDao.findAll(2, 5)).thenReturn(Arrays.asList(Student.builder().withId((long)1).build()));

        studentService.findAll(pageNumber);

        verify(studentDao).count();
        verify(studentDao).findAll(2, 5);
    }

    @Test
    void editShouldEditDataOfStudentIfArgumentNewStudentRequest() {
        Student student = Student.builder().withId((long)1).build();
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId((long)1);

        when(studentRequestMapper.mapDtoToEntity(studentRequest)).thenReturn(student);
        doNothing().when(studentDao).update(student);

        studentService.edit(studentRequest);

        verify(studentRequestMapper).mapDtoToEntity(studentRequest);
        verify(studentDao).update(student);
    }

    @Test
    void deleteShouldDeleteDataOfStudentIfArgumentIsStudentId() {
        long studentId = 1;

        when(studentDao.findById(studentId)).thenReturn(Optional.of(Student.builder().withId(studentId).build()));
        when(studentDao.deleteById(studentId)).thenReturn(true);

        studentService.deleteById(studentId);

        verify(studentDao).findById(studentId);
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
