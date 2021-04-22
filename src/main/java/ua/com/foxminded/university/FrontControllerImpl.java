package ua.com.foxminded.university;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.impl.CourseDaoImpl;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.domain.Lesson;
import ua.com.foxminded.university.domain.Student;
import ua.com.foxminded.university.providers.ViewProvider;

import javax.inject.Inject;
import java.util.List;

@Service
public class FrontControllerImpl implements FrontController {

    private static final String REQUEST_PAGE_NUMBER = "Input number of page";
    private static final String REQUEST_STUDENT_ID = "Enter Id of the student";
    private static final String START_MENU = "\nPress 0 to exit; \n" +
            "Press 1 show time table for student; \n" +
            "Press 2 show time table for professor; \n" +
            "Press 3 create student; \n" +
            "Press 4 show all students; \n" +
            "Press 5 delete student; \n";

    private final CourseDao courseDao;
    private final DepartmentDao departmentDao;
    private final FormOfEducationDao formOfEducationDao;
    private final FormOfLessonDao formOfLessonDao;
    private final GroupDao groupDao;
    private final LessonDao lessonDao;
    private final ProfessorDao professorDao;
    private final StudentDao studentDao;
    private final ViewProvider viewProvider;

    @Inject
    public FrontControllerImpl(CourseDao courseDao, DepartmentDao departmentDao, FormOfEducationDao formOfEducationDao,
                               FormOfLessonDao formOfLessonDao, GroupDao groupDao, LessonDao lessonDao,
                               ProfessorDao professorDao, StudentDao studentDao, ViewProvider viewProvider) {
        this.courseDao = courseDao;
        this.departmentDao = departmentDao;
        this.formOfEducationDao = formOfEducationDao;
        this.formOfLessonDao = formOfLessonDao;
        this.groupDao = groupDao;
        this.lessonDao = lessonDao;
        this.professorDao = professorDao;
        this.studentDao = studentDao;
        this.viewProvider = viewProvider;
    }

    @Override
    public void startMenu(int itemsPerPage) {
        boolean cycleBreaker = false;

        while (!cycleBreaker){
            viewProvider.printMessage(START_MENU);

            int choose = viewProvider.readInt();
            switch (choose) {
                case 0: cycleBreaker = true; break;
                case 1: timeTableForStudent(); break;
                case 2: timeTableForProfessor(); break;
                case 3: createStudent(); break;
                case 4: showAllStudents(itemsPerPage); break;
                case 5: deleteStudent(); break;
                default: viewProvider.printMessage("Incorrect number, try again");
            }
        }

    }

    private void timeTableForStudent(){
        viewProvider.printMessage("Input student Id");
        long studentId = viewProvider.readLong();
        long groupId = studentDao.findById(studentId).get().getGroup().getId();
        List<Lesson> lessons = lessonDao.formTimeTableForGroup(groupId);

        viewProvider.printMessage("Time table for group with id: " + groupId + ": \n");
        for(Lesson lesson : lessons){
            viewProvider.printMessage(lesson.toString());
        }
    }

    private void timeTableForProfessor(){
        viewProvider.printMessage("Input professor Id");
        long professorId = viewProvider.readLong();

        List<Lesson> lessons = lessonDao.formTimeTableForProfessor(professorId);

        viewProvider.printMessage("Time table for professor with id: " + professorId + ": \n");
        for(Lesson lesson : lessons){
            viewProvider.printMessage(lesson.toString());
        }
    }

    private void createStudent(){
        viewProvider.printMessage("Input student first name");
        String studentFirstName = viewProvider.read();
        viewProvider.printMessage("Input student last name");
        String studentLastName = viewProvider.read();
        viewProvider.printMessage("Input student email");
        String studentEmail = viewProvider.read();
        viewProvider.printMessage("Input student password");
        String studentPassword = viewProvider.read();

        studentDao.save(Student.builder()
                .withFirstName(studentFirstName)
                .withLastName(studentLastName)
                .withEmail(studentEmail)
                .withPassword(studentPassword)
                .build());
    }

    private void showAllStudents(int itemsPerPage){
            viewProvider.printMessage(REQUEST_PAGE_NUMBER);
            int numberOfPage = viewProvider.readInt();
            List<Student> students = studentDao.findAll(numberOfPage, itemsPerPage);
            for(Student student : students){
                viewProvider.printMessage(student.toString());
            }
    }

    private void deleteStudent(){
        viewProvider.printMessage(REQUEST_STUDENT_ID);
        long studentId = viewProvider.readLong();
        studentDao.deleteById(studentId);
    }

}
