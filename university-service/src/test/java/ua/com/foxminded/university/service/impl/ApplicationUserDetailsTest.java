package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.repository.PrivilegeRepository;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Privilege;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.service.ProfessorService;
import ua.com.foxminded.university.service.StudentService;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
public class ApplicationUserDetailsTest {


    private static final String EMAIL_OF_PROFESSOR = "professor@gmail.com";
    private static final String EMAIL_OF_STUDENT = "student@gmail.com";
    private static final String PASSWORD = "12345";
    private static final StudentResponse STUDENT_RESPONSE = new StudentResponse();
    private static final ProfessorResponse PROFESSOR_RESPONSE = new ProfessorResponse();
    private static final Role ROLE_STUDENT = Role.builder().withId(1L).withName("ROLE_STUDENT").build();
    private static final Role ROLE_PROFESSOR = Role.builder().withId(2L).withName("ROLE_PROFESSOR").build();
    private static final Privilege READ_PRIVILEGE = Privilege.builder().withId(1L).withName("READ_PRIVILEGE").build();
    private static final Privilege WRITE_PRIVILEGE = Privilege.builder().withId(2L).withName("WRITE_PRIVILEGE").build();

    {
        STUDENT_RESPONSE.setId(1L);
        STUDENT_RESPONSE.setEmail(EMAIL_OF_STUDENT);
        STUDENT_RESPONSE.setPassword(PASSWORD);
        STUDENT_RESPONSE.setRoles(Collections.singletonList(ROLE_STUDENT));
        PROFESSOR_RESPONSE.setId(2L);
        PROFESSOR_RESPONSE.setEmail(EMAIL_OF_PROFESSOR);
        PROFESSOR_RESPONSE.setPassword(PASSWORD);
        PROFESSOR_RESPONSE.setRoles(Collections.singletonList(ROLE_PROFESSOR));
    }

    @Mock
    ProfessorService professorService;

    @Mock
    StudentService studentService;

    @Mock
    PrivilegeRepository privilegeRepository;

    @InjectMocks
    ApplicationUserDetails applicationUserDetails;

    @Test
    void loadUserByUsernameShouldLoadDataOfStudentToSpringSecurityIfArgumentsIsEmailAndPassword() {
        when(studentService.findByEmail(EMAIL_OF_STUDENT)).thenReturn(Collections.singletonList(STUDENT_RESPONSE));
        when(professorService.findByEmail(EMAIL_OF_STUDENT)).thenReturn(Collections.emptyList());
        when(privilegeRepository.findAllByRoleId(1L)).thenReturn(Collections.singletonList(READ_PRIVILEGE));

        applicationUserDetails.loadUserByUsername(EMAIL_OF_STUDENT);

        verify(studentService).findByEmail(EMAIL_OF_STUDENT);
        verify(professorService).findByEmail(EMAIL_OF_STUDENT);
        verify(privilegeRepository).findAllByRoleId(1L);
    }

    @Test
    void loadUserByUsernameShouldLoadDataOfProfessorToSpringSecurityIfArgumentsIsEmailAndPassword() {
        when(studentService.findByEmail(EMAIL_OF_PROFESSOR)).thenReturn(Collections.emptyList());
        when(professorService.findByEmail(EMAIL_OF_PROFESSOR)).thenReturn(Collections.singletonList(PROFESSOR_RESPONSE));
        when(privilegeRepository.findAllByRoleId(2L)).thenReturn(Arrays.asList(READ_PRIVILEGE, WRITE_PRIVILEGE));

        applicationUserDetails.loadUserByUsername(EMAIL_OF_PROFESSOR);

        verify(studentService).findByEmail(EMAIL_OF_PROFESSOR);
        verify(professorService).findByEmail(EMAIL_OF_PROFESSOR);
        verify(privilegeRepository).findAllByRoleId(2L);
    }

    @Test
    void loadUserByUsernameShouldThrowExceptionIfEmailNotExist() {
        when(studentService.findByEmail(EMAIL_OF_PROFESSOR)).thenReturn(Collections.emptyList());
        when(professorService.findByEmail(EMAIL_OF_PROFESSOR)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> applicationUserDetails.loadUserByUsername(EMAIL_OF_PROFESSOR))
                .hasMessage("no user with email: professor@gmail.com");

        verify(studentService).findByEmail(EMAIL_OF_PROFESSOR);
        verify(professorService).findByEmail(EMAIL_OF_PROFESSOR);
    }

}
