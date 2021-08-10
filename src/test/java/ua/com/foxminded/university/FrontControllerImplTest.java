package ua.com.foxminded.university;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.providers.ViewProvider;
import ua.com.foxminded.university.service.interfaces.CourseService;
import ua.com.foxminded.university.service.interfaces.DepartmentService;
import ua.com.foxminded.university.service.interfaces.FormOfEducationService;
import ua.com.foxminded.university.service.interfaces.FormOfLessonService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.LessonService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import ua.com.foxminded.university.service.interfaces.StudentService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class FrontControllerImplTest {

    private static final String START_MENU = "\nPress 0 to exit; \n" +
            "Press 1 show time table for student; \n" +
            "Press 2 show time table for professor; \n" +
            "Press 3 create student; \n" +
            "Press 4 show all students; \n" +
            "Press 5 delete student; \n";

    private static final List<Student> STUDENT_ENTITIES = Arrays.asList(
            Student.builder().withFirstName("Nikita").withLastName("Grigirev").build(),
            Student.builder().withFirstName("Alex").withLastName("Ivanov").build());

    private static final LessonResponse LESSON_RESPONSE_1 = new LessonResponse();
    private static final LessonResponse LESSON_RESPONSE_2 = new LessonResponse();
    private static final LessonResponse LESSON_RESPONSE_3 = new LessonResponse();
    private static final GroupResponse GROUP_RESPONSE_1 = new GroupResponse();
    private static final StudentResponse STUDENT_RESPONSE_1 = new StudentResponse();
    private static final StudentResponse STUDENT_RESPONSE_2 = new StudentResponse();
    private static final StudentResponse STUDENT_RESPONSE_3 = new StudentResponse();
    final List<LessonResponse> LESSON_RESPONSES = Arrays.asList(
            LESSON_RESPONSE_1, LESSON_RESPONSE_2, LESSON_RESPONSE_3);
    final List<StudentResponse> STUDENT_RESPONSES = Arrays.asList(
            STUDENT_RESPONSE_2, STUDENT_RESPONSE_3);

    @Mock
    CourseService courseService;

    @Mock
    DepartmentService departmentService;

    @Mock
    FormOfEducationService formOfEducationService;

    @Mock
    FormOfLessonService formOfLessonService;

    @Mock
    GroupService groupService;

    @Mock
    LessonService lessonService;

    @Mock
    ProfessorService professorService;

    @Mock
    StudentService studentService;

    @Mock
    ViewProvider viewProvider;

    @InjectMocks
    FrontControllerImpl frontController;

    static {
        LESSON_RESPONSE_1.setId(1L);
        LESSON_RESPONSE_2.setId(2L);
        LESSON_RESPONSE_3.setId(3L);
        GROUP_RESPONSE_1.setId(1L);
        STUDENT_RESPONSE_1.setGroupResponse(GROUP_RESPONSE_1);
        STUDENT_RESPONSE_2.setFirstName("Nikita");
        STUDENT_RESPONSE_2.setLastName("Grigirev");
        STUDENT_RESPONSE_3.setFirstName("Alex");
        STUDENT_RESPONSE_3.setLastName("Ivanov");
    }

    @Test
    void timeTableForStudentFromStartMenuShouldShowTimeTableForStudentFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(1, 0);
        doNothing().when(viewProvider).printMessage("Input student Id");
        when(viewProvider.readLong()).thenReturn(2L);
        when(studentService.findById(2)).thenReturn(Optional.of(STUDENT_RESPONSE_1));
        when(lessonService.formTimeTableForGroup(1)).thenReturn(LESSON_RESPONSES);
        doNothing().when(viewProvider).printMessage("Time table for group with id: 1: \n");
        doNothing().when(viewProvider).printMessage(LESSON_RESPONSES.get(0).toString());
        doNothing().when(viewProvider).printMessage(LESSON_RESPONSES.get(1).toString());
        doNothing().when(viewProvider).printMessage(LESSON_RESPONSES.get(2).toString());

        frontController.startMenu();

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).readLong();
        verify(viewProvider).printMessage("Input student Id");
        verify(studentService).findById(2);
        verify(lessonService).formTimeTableForGroup(1);
        verify(viewProvider).printMessage("Time table for group with id: 1: \n");
        verify(viewProvider).printMessage(LESSON_RESPONSES.get(0).toString());
        verify(viewProvider).printMessage(LESSON_RESPONSES.get(1).toString());
        verify(viewProvider).printMessage(LESSON_RESPONSES.get(2).toString());
    }

    @Test
    void timeTableForProfessorFromStartMenuShouldShowTimeTableForProfessorFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(2, 0);
        doNothing().when(viewProvider).printMessage("Input professor Id");
        when(viewProvider.readLong()).thenReturn(2L);
        when(lessonService.formTimeTableForProfessor(2)).thenReturn(LESSON_RESPONSES);
        doNothing().when(viewProvider).printMessage("Time table for professor with id: 2: \n");
        doNothing().when(viewProvider).printMessage(LESSON_RESPONSES.get(0).toString());
        doNothing().when(viewProvider).printMessage(LESSON_RESPONSES.get(1).toString());
        doNothing().when(viewProvider).printMessage(LESSON_RESPONSES.get(2).toString());

        frontController.startMenu();

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Input professor Id");
        verify(viewProvider).readLong();
        verify(lessonService).formTimeTableForProfessor(2);
        verify(viewProvider).printMessage("Time table for professor with id: 2: \n");
        verify(viewProvider).printMessage(LESSON_RESPONSES.get(0).toString());
        verify(viewProvider).printMessage(LESSON_RESPONSES.get(1).toString());
        verify(viewProvider).printMessage(LESSON_RESPONSES.get(2).toString());
    }

    @Test
    void createStudentFromStartMenuShouldCreateNewStudentFromStartMenuNoArguments() {
        StudentRequest studentRequestToRegistration = new StudentRequest();
        studentRequestToRegistration.setFirstName("Alex");
        studentRequestToRegistration.setLastName("Chrikov");
        studentRequestToRegistration.setEmail("Chrikov@gmail.com");
        studentRequestToRegistration.setPassword("12AsqW!L");

        StudentResponse studentResponseAfterRegistration = new StudentResponse();
        studentResponseAfterRegistration.setId(11L);
        studentResponseAfterRegistration.setFirstName("Alex");
        studentResponseAfterRegistration.setLastName("Chrikov");
        studentResponseAfterRegistration.setEmail("Chrikov@gmail.com");
        studentResponseAfterRegistration.setPassword("12AsqW!L");

        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(3, 0);
        doNothing().when(viewProvider).printMessage("Input student first name");
        doNothing().when(viewProvider).printMessage("Input student last name");
        doNothing().when(viewProvider).printMessage("Input student email");
        doNothing().when(viewProvider).printMessage("Input student password");
        when(viewProvider.read()).thenReturn("Alex", "Chrikov", "Chrikov@gmail.com", "12AsqW!L");
        when(studentService.register(studentRequestToRegistration))
                .thenReturn(studentResponseAfterRegistration);

        frontController.startMenu();

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Input student first name");
        verify(viewProvider).printMessage("Input student last name");
        verify(viewProvider).printMessage("Input student email");
        verify(viewProvider).printMessage("Input student password");
        verify(viewProvider, times(4)).read();
        verify(studentService).register(studentRequestToRegistration);
    }

        @Test
    void showAllStudentsFromStartMenuShouldShowAllStudentsFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(4, 0);
        when(viewProvider.read()).thenReturn("1");
        doNothing().when(viewProvider).printMessage("Input number of page");
        when(studentService.findAll("1")).thenReturn(STUDENT_RESPONSES);
        doNothing().when(viewProvider).printMessage(STUDENT_RESPONSES.get(0).toString());
        doNothing().when(viewProvider).printMessage(STUDENT_RESPONSES.get(1).toString());

        frontController.startMenu();

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).read();
        verify(viewProvider).printMessage("Input number of page");
        verify(studentService).findAll("1");
        verify(viewProvider).printMessage(STUDENT_RESPONSES.get(0).toString());
        verify(viewProvider).printMessage(STUDENT_RESPONSES.get(1).toString());
    }

        @Test
    void deleteStudentFromStartMenuShouldDeleteStudentFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(5, 0);
        doNothing().when(viewProvider).printMessage("Enter Id of the student");
        when(viewProvider.readLong()).thenReturn(1L);
        when(studentService.deleteById(1)).thenReturn(true);

        frontController.startMenu();

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Enter Id of the student");
        verify(viewProvider).readLong();
        verify(studentService).deleteById(1);
    }

    @Test
    void incorrectInputValueFromStartMenuShouldWriteAboutIncorrectInputValueAboutNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(20, 0);
        doNothing().when(viewProvider).printMessage("Incorrect number, try again");

        frontController.startMenu();

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Incorrect number, try again");
    }

}
