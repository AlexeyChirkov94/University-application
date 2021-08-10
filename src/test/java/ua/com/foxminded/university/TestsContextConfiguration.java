package ua.com.foxminded.university;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ua.com.foxminded.university.config.PersistenceContext;
import ua.com.foxminded.university.config.WebMvcConfiguration;
import ua.com.foxminded.university.service.interfaces.CourseService;
import ua.com.foxminded.university.service.interfaces.DepartmentService;
import ua.com.foxminded.university.service.interfaces.FormOfEducationService;
import ua.com.foxminded.university.service.interfaces.FormOfLessonService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.LessonService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import ua.com.foxminded.university.service.interfaces.StudentService;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.university",
        excludeFilters = @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,
                value= {WebMvcConfiguration.class, PersistenceContext.class}))
public class TestsContextConfiguration implements WebMvcConfigurer {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).addScript("schema.sql").addScript("data.sql").build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

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
