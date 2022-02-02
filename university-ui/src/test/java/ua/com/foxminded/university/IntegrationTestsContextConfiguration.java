package ua.com.foxminded.university;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.impl.DepartmentServiceImpl;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.university")
@AllArgsConstructor
@EnableAutoConfiguration
public class IntegrationTestsContextConfiguration {

    ApplicationContext applicationContext;

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).addScript("schema.sql").addScript("data.sql").build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource){
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
    public DepartmentService departmentService() {
        return applicationContext.getBean(DepartmentServiceImpl.class);
    }

}
