package ua.com.foxminded.university;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.domain.Group;
import ua.com.foxminded.university.domain.Lesson;
import ua.com.foxminded.university.domain.Student;
import ua.com.foxminded.university.providers.ViewProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
public class FrontControllerImplTest {

    private static final String START_MENU = "\nPress 0 to exit; \n" +
            "Press 1 show time table for student; \n" +
            "Press 2 show time table for professor; \n" +
            "Press 3 create student; \n" +
            "Press 4 show all students; \n" +
            "Press 5 delete student; \n";

    private static final List<Student> STUDENTS = Arrays.asList(
            Student.builder().withFirstName("Nikita").withLastName("Grigirev").build(),
            Student.builder().withFirstName("Alex").withLastName("Ivanov").build());

    private static final List<Lesson> LESSONS = Arrays.asList(
            Lesson.builder().withId((long)1).build(),
            Lesson.builder().withId((long)2).build(),
            Lesson.builder().withId((long)3).build());

    private static final int ITEMS_PER_PAGE = 3;

    @Mock
    private CourseDao courseDao;

    @Mock
    private DepartmentDao departmentDao;

    @Mock
    private FormOfEducationDao formOfEducationDao;

    @Mock
    private FormOfLessonDao formOfLessonDao;

    @Mock
    private GroupDao groupDao;

    @Mock
    private LessonDao lessonDao;

    @Mock
    private ProfessorDao professorDao;

    @Mock
    private StudentDao studentDao;

    @Mock
    private ViewProvider viewProvider;

    @InjectMocks
    private FrontControllerImpl frontController;

    @Test
    void timeTableForStudentFromStartMenuShouldShowTimeTableForStudentFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(1, 0);
        doNothing().when(viewProvider).printMessage("Input student Id");
        when(viewProvider.readLong()).thenReturn((long)2);
        when(studentDao.findById((long)2)).thenReturn(
                Optional.of(Student.builder().withGroup(Group.builder().withId((long)1).build()).build()));
        when(lessonDao.formTimeTableForGroup(1)).thenReturn(LESSONS);
        doNothing().when(viewProvider).printMessage("Time table for group with id: 1: \n");
        doNothing().when(viewProvider).printMessage(LESSONS.get(0).toString());
        doNothing().when(viewProvider).printMessage(LESSONS.get(1).toString());
        doNothing().when(viewProvider).printMessage(LESSONS.get(2).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).readLong();
        verify(viewProvider).printMessage("Input student Id");
        verify(studentDao).findById((long)2);
        verify(lessonDao).formTimeTableForGroup(1);
        verify(viewProvider).printMessage("Time table for group with id: 1: \n");
        verify(viewProvider).printMessage(LESSONS.get(0).toString());
        verify(viewProvider).printMessage(LESSONS.get(1).toString());
        verify(viewProvider).printMessage(LESSONS.get(2).toString());
    }

    @Test
    void timeTableForProfessorFromStartMenuShouldShowTimeTableForProfessorFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(2, 0);
        doNothing().when(viewProvider).printMessage("Input professor Id");
        when(viewProvider.readLong()).thenReturn((long)2);
        when(lessonDao.formTimeTableForProfessor(2)).thenReturn(LESSONS);
        doNothing().when(viewProvider).printMessage("Time table for professor with id: 2: \n");
        doNothing().when(viewProvider).printMessage(LESSONS.get(0).toString());
        doNothing().when(viewProvider).printMessage(LESSONS.get(1).toString());
        doNothing().when(viewProvider).printMessage(LESSONS.get(2).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Input professor Id");
        verify(viewProvider).readLong();
        verify(lessonDao).formTimeTableForProfessor(2);
        verify(viewProvider).printMessage("Time table for professor with id: 2: \n");
        verify(viewProvider).printMessage(LESSONS.get(0).toString());
        verify(viewProvider).printMessage(LESSONS.get(1).toString());
        verify(viewProvider).printMessage(LESSONS.get(2).toString());
    }

    @Test
    void createStudentFromStartMenuShouldCreateNewStudentFromStartMenuNoArguments() {
        String firstName = "Alex";
        String lastName = "Chrikov";
        String email = "Chrikov@gmail.com";
        String password = "1234";

        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(3, 0);
        doNothing().when(viewProvider).printMessage("Input student first name");
        doNothing().when(viewProvider).printMessage("Input student last name");
        doNothing().when(viewProvider).printMessage("Input student email");
        doNothing().when(viewProvider).printMessage("Input student password");
        when(viewProvider.read()).thenReturn(firstName, lastName, email, password);
        when(studentDao.save(Student.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withPassword(password)
                .build()))
                .thenReturn(Student.builder()
                        .withId((long)11)
                        .withFirstName(firstName)
                        .withLastName(lastName)
                        .withEmail(email)
                        .withPassword(password)
                        .build());


        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Input student first name");
        verify(viewProvider).printMessage("Input student last name");
        verify(viewProvider).printMessage("Input student email");
        verify(viewProvider).printMessage("Input student password");
        verify(viewProvider, times(4)).read();
        verify(studentDao).save(Student.builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withPassword(password)
                .build());
    }

    @Test
    void showAllStudentsFromStartMenuShouldShowAllStudentsFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(4, 1, 0);
        doNothing().when(viewProvider).printMessage("Input number of page");
        when(studentDao.findAll(1, 3)).thenReturn(STUDENTS);
        doNothing().when(viewProvider).printMessage(STUDENTS.get(0).toString());
        doNothing().when(viewProvider).printMessage(STUDENTS.get(1).toString());

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(3)).readInt();
        verify(viewProvider).printMessage("Input number of page");
        verify(studentDao).findAll(1, 3);
        verify(viewProvider).printMessage(STUDENTS.get(0).toString());
        verify(viewProvider).printMessage(STUDENTS.get(1).toString());
    }

    @Test
    void deleteStudentFromStartMenuShouldDeleteStudentFromStartMenuNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(5, 0);
        doNothing().when(viewProvider).printMessage("Enter Id of the student");
        when(viewProvider.readLong()).thenReturn((long)1);
        when(studentDao.deleteById((long)1)).thenReturn(true);

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Enter Id of the student");
        verify(viewProvider).readLong();
        verify(studentDao).deleteById((long)1);
    }

    @Test
    void incorrectInputValueFromStartMenuShouldWriteAboutIncorrectInputValueAboutNoArguments() {
        doNothing().when(viewProvider).printMessage(START_MENU);
        when(viewProvider.readInt()).thenReturn(20, 0);
        doNothing().when(viewProvider).printMessage("Incorrect number, try again");

        frontController.startMenu(ITEMS_PER_PAGE);

        verify(viewProvider, times(2)).printMessage(START_MENU);
        verify(viewProvider, times(2)).readInt();
        verify(viewProvider).printMessage("Incorrect number, try again");
    }

}
