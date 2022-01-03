package ua.com.foxminded.university;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.impl.DepartmentServiceImpl;
import javax.sql.DataSource;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.university")
@AllArgsConstructor
public class IntegrationTestsContextConfiguration {

    ApplicationContext applicationContext;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).addScript("schema.sql").addScript("data.sql").build();
    }

    @Bean
    public DepartmentService departmentService() {
        return applicationContext.getBean(DepartmentServiceImpl.class);
    }

}
