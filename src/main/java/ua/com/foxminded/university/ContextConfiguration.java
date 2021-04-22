package ua.com.foxminded.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.ResourceBundle;


@Configuration
@ComponentScan("ua.com.foxminded.university")
public class ContextConfiguration {

    @Bean()
    public DataSource dataSource() {
        ResourceBundle resource = ResourceBundle.getBundle("db");
        DriverManagerDataSource driver = new DriverManagerDataSource();

        driver.setDriverClassName(resource.getString("db.driver"));
        driver.setUrl(resource.getString("db.url"));
        driver.setUsername(resource.getString("db.user"));
        driver.setPassword(resource.getString("db.password"));
        return driver;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){

        return new JdbcTemplate(dataSource);
    }


}
