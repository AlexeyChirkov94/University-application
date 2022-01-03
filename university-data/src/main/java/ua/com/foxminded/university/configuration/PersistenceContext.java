package ua.com.foxminded.university.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = "ua.com.foxminded.university")
@Import(DataBaseConnectionBeans.class)
public class PersistenceContext {

}
