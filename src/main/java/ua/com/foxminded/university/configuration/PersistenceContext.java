package ua.com.foxminded.university.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackages = "ua.com.foxminded.university",
        excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebApplicationConfiguration.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebSecurityConfiguration.class),
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class)})
@PropertySource("classpath:db.properties")
@Import(PasswordEncoderBean.class)
public class PersistenceContext {

    @Value("${db.driver}")
    String dbDriver;

    @Value("${db.url}")
    String dbUrl;

    @Value("${db.user}")
    String dbUsername;

    @Value("${db.password}")
    String dbPassword;

    @Bean()
    public DataSource dataSource() {
        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(dbDriver);
        driver.setUrl(dbUrl);
        driver.setUsername(dbUsername);
        driver.setPassword(dbPassword);

        return driver;
    }

    @Bean
    public DataSourceTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
