package ua.com.foxminded.university;

import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Lesson;
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

    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final FormOfEducationService formOfEducationService;
    private final FormOfLessonService formOfLessonService;
    private final GroupService groupService;
    private final LessonService lessonService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final ViewProvider viewProvider;

    public FrontControllerImpl(CourseService courseService, DepartmentService departmentService,
                               FormOfEducationService formOfEducationService, FormOfLessonService formOfLessonService,
                               GroupService groupService, LessonService lessonService, ProfessorService professorService,
                               StudentService studentService, ViewProvider viewProvider) {
        this.courseService = courseService;
        this.departmentService = departmentService;
        this.formOfEducationService = formOfEducationService;
        this.formOfLessonService = formOfLessonService;
        this.groupService = groupService;
        this.lessonService = lessonService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.viewProvider = viewProvider;
    }

    @Override
    public void startMenu() {
        boolean cycleBreaker = false;

        while (!cycleBreaker){
            viewProvider.printMessage(START_MENU);

            int choose = viewProvider.readInt();
            switch (choose) {
                case 0: cycleBreaker = true; break;
                case 1: timeTableForStudent(); break;
                case 2: timeTableForProfessor(); break;
                case 3: createStudent(); break;
                case 4: showAllStudents(); break;
                case 5: deleteStudent(); break;
                default: viewProvider.printMessage("Incorrect number, try again");
            }
        }

    }

    private void timeTableForStudent(){
        viewProvider.printMessage("Input student Id");
        long studentId = viewProvider.readLong();
        long groupId = studentService.findById(studentId).get().getGroupResponse().getId();
        List<LessonResponse> LessonResponses = lessonService.formTimeTableForGroup(groupId);

        viewProvider.printMessage("Time table for group with id: " + groupId + ": \n");
        for(LessonResponse lessonResponse : LessonResponses){
            viewProvider.printMessage(lessonResponse.toString());
        }
    }

    private void timeTableForProfessor(){
        viewProvider.printMessage("Input professor Id");
        long professorId = viewProvider.readLong();

        List<LessonResponse> LessonResponses = lessonService.formTimeTableForProfessor(professorId);

        viewProvider.printMessage("Time table for professor with id: " + professorId + ": \n");
        for(LessonResponse lessonResponse : LessonResponses){
            viewProvider.printMessage(lessonResponse.toString());
        }
    }

    private void createStudent(){
        StudentRequest studentRequest = new StudentRequest();
        viewProvider.printMessage("Input student first name");
        studentRequest.setFirstName(viewProvider.read());
        viewProvider.printMessage("Input student last name");
        studentRequest.setLastName(viewProvider.read());
        viewProvider.printMessage("Input student email");
        studentRequest.setEmail(viewProvider.read());
        viewProvider.printMessage("Input student password");
        studentRequest.setPassword(viewProvider.read());

        studentService.register(studentRequest);
    }

    private void showAllStudents(){
            viewProvider.printMessage(REQUEST_PAGE_NUMBER);
            String numberOfPage = viewProvider.read();
            List<StudentResponse> studentResponses = studentService.findAll(numberOfPage);
            for(StudentResponse studentResponse : studentResponses){
                viewProvider.printMessage(studentResponse.toString());
            }
    }

    private void deleteStudent(){
        viewProvider.printMessage(REQUEST_STUDENT_ID);
        long studentId = viewProvider.readLong();
        studentService.deleteById(studentId);
    }

}
