package ua.com.foxminded.university;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.FormOfEducationService;
import ua.com.foxminded.university.service.FormOfLessonService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.ProfessorService;
import ua.com.foxminded.university.service.StudentService;


@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.university.controllers")
public class ControllerTestsContextConfiguration {

    @Bean
    public StudentService studentService(){
        return Mockito.mock(StudentService.class);
    }

    @Bean
    public ProfessorService professorService() {
        return Mockito.mock(ProfessorService.class);
    }

    @Bean
    public GroupService groupService(){
        return Mockito.mock(GroupService.class);
    }

    @Bean
    public CourseService courseService() {
        return Mockito.mock(CourseService.class);
    }

    @Bean
    public DepartmentService departmentService() {
        return Mockito.mock(DepartmentService.class);
    }

    @Bean
    public FormOfEducationService formOfEducationService() {
        return Mockito.mock(FormOfEducationService.class);
    }

    @Bean
    public FormOfLessonService formOfLessonService() {
        return Mockito.mock(FormOfLessonService.class);
    }

    @Bean
    public LessonService lessonService() {
        return Mockito.mock(LessonService.class);
    }
}
