package ua.com.foxminded.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import ua.com.foxminded.university.dao.impl.CourseDaoImpl;
import ua.com.foxminded.university.dao.impl.DepartmentDaoImpl;
import ua.com.foxminded.university.dao.impl.FormOfEducationDaoImpl;
import ua.com.foxminded.university.dao.impl.FormOfLessonDaoImpl;
import ua.com.foxminded.university.dao.impl.GroupDaoImpl;
import ua.com.foxminded.university.dao.impl.LessonDaoImpl;
import ua.com.foxminded.university.dao.impl.ProfessorDaoImpl;
import ua.com.foxminded.university.dao.impl.StudentDaoImpl;
import ua.com.foxminded.university.dao.interfaces.CourseDao;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.providers.ViewProvider;
import ua.com.foxminded.university.providers.ViewProviderImpl;
import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
public class TestsContextConfiguration {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).addScript("schema.sql").addScript("data.sql").build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CourseDao courseDao(JdbcTemplate jdbcTemplate){
        return new CourseDaoImpl(jdbcTemplate);
    }

    @Bean
    public DepartmentDao departmentDao(JdbcTemplate jdbcTemplate){
        return new DepartmentDaoImpl(jdbcTemplate);
    }

    @Bean
    public FormOfEducationDao formOfEducationDao(JdbcTemplate jdbcTemplate){
        return new FormOfEducationDaoImpl(jdbcTemplate);
    }

    @Bean
    public FormOfLessonDao formOfLessonDao(JdbcTemplate jdbcTemplate){
        return new FormOfLessonDaoImpl(jdbcTemplate);
    }

    @Bean
    public GroupDao groupDao(JdbcTemplate jdbcTemplate){
        return new GroupDaoImpl(jdbcTemplate);
    }

    @Bean
    public LessonDao lessonDao(JdbcTemplate jdbcTemplate){
        return new LessonDaoImpl(jdbcTemplate);
    }

    @Bean
    public ProfessorDao professorDao(JdbcTemplate jdbcTemplate){
        return new ProfessorDaoImpl(jdbcTemplate);
    }

    @Bean
    public StudentDao studentDao(JdbcTemplate jdbcTemplate){
        return new StudentDaoImpl(jdbcTemplate);
    }

    @Bean
    public ViewProvider viewProvider(){
        return new ViewProviderImpl();
    }

}
