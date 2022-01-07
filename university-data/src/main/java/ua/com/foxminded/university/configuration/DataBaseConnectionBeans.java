package ua.com.foxminded.university.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:db.properties")
public class DataBaseConnectionBeans {

    @Bean
    public DataSource dataSource(@Value("${db.driver}") String dbDriver, @Value("${db.url}") String dbUrl,
                                 @Value("${db.user}") String dbUsername, @Value("${db.password}") String dbPassword) {

        DriverManagerDataSource driver = new DriverManagerDataSource();
        driver.setDriverClassName(dbDriver);
        driver.setUrl(dbUrl);
        driver.setUsername(dbUsername);
        driver.setPassword(dbPassword);

        return driver;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSourceTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
