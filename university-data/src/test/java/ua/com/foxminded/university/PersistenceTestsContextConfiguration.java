package ua.com.foxminded.university;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ua.com.foxminded.university.dao.CourseDao;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.dao.FormOfEducationDao;
import ua.com.foxminded.university.dao.FormOfLessonDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dao.PrivilegeDao;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.dao.RoleDao;
import ua.com.foxminded.university.dao.StudentDao;
import ua.com.foxminded.university.dao.impl.CourseDaoImpl;
import ua.com.foxminded.university.dao.impl.DepartmentDaoImpl;
import ua.com.foxminded.university.dao.impl.FormOfEducationDaoImpl;
import ua.com.foxminded.university.dao.impl.FormOfLessonDaoImpl;
import ua.com.foxminded.university.dao.impl.GroupDaoImpl;
import ua.com.foxminded.university.dao.impl.LessonDaoImpl;
import ua.com.foxminded.university.dao.impl.PrivilegeDaoImpl;
import ua.com.foxminded.university.dao.impl.ProfessorDaoImpl;
import ua.com.foxminded.university.dao.impl.RoleDaoImpl;
import ua.com.foxminded.university.dao.impl.StudentDaoImpl;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@EnableAutoConfiguration
public class PersistenceTestsContextConfiguration {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).addScript("schema.sql").addScript("data.sql").build();
    }

    @Bean
    public EntityManagerFactory  entityManagerFactory(DataSource dataSource){
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        Properties props = new Properties();
        props.setProperty("hibernate.format_sql", String.valueOf(true));
        props.setProperty("hibernate.connection.autocommit", String.valueOf(true));

        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("ua.com.foxminded.university");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setJpaProperties(props);
        factoryBean.afterPropertiesSet();

        return factoryBean.getNativeEntityManagerFactory();
    }

    @Bean
    public CourseDao courseDao(EntityManager entityManager){
        return new CourseDaoImpl(entityManager);
    }

    @Bean
    public DepartmentDao departmentDao(EntityManager entityManager){
        return new DepartmentDaoImpl(entityManager);
    }

    @Bean
    public FormOfEducationDao formOfEducationDao(EntityManager entityManager){
        return new FormOfEducationDaoImpl(entityManager);
    }

    @Bean
    public FormOfLessonDao formOfLessonDao(EntityManager entityManager){
        return new FormOfLessonDaoImpl(entityManager);
    }

    @Bean
    public GroupDao groupDao(EntityManager entityManager){
        return new GroupDaoImpl(entityManager);
    }

    @Bean
    public LessonDao lessonDao(EntityManager entityManager){
        return new LessonDaoImpl(entityManager);
    }

    @Bean
    public PrivilegeDao privilegeDao(EntityManager entityManager){
        return new PrivilegeDaoImpl(entityManager);
    }

    @Bean
    public ProfessorDao professorDao(EntityManager entityManager){
        return new ProfessorDaoImpl(entityManager);
    }

    @Bean
    public RoleDao roleDao(EntityManager entityManager){
        return new RoleDaoImpl(entityManager);
    }

    @Bean
    public StudentDao studentDao(EntityManager entityManager){
        return new StudentDaoImpl(entityManager);
    }

}
