package ua.com.foxminded.university;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import ua.com.foxminded.university.configuration.PersistenceContext;
import javax.sql.DataSource;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.university",
        excludeFilters = @ComponentScan.Filter(type= FilterType.ASSIGNABLE_TYPE,
                value= {PersistenceContext.class}))
public class PersistenceTestsContextConfiguration {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(H2).addScript("schema.sql").addScript("data.sql").build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
