package ua.com.foxminded.university.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource("classpath:dataSourceNameSwitcher.properties")
public class DataBaseConnectionBeans {

    @Bean
    public DataSource dataSource(@Value("${data.source.name}") String dataSourceName) {
        JndiDataSourceLookup dataSource = new JndiDataSourceLookup();
        dataSource.setResourceRef(true);
        return dataSource.getDataSource(dataSourceName);
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
