package ua.com.foxminded.university.configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import ua.com.foxminded.university.service.impl.ApplicationUserDetails;

@Import(PasswordEncoderBean.class)
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationContext context = new AnnotationConfigApplicationContext(PersistenceContext.class);

    @Bean
    public UserDetailsService userDetailsService() {
        return context.getBean(ApplicationUserDetails.class);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/features" ,"/about", "/registration", "/login" , "/student/new" , "/professor/new").permitAll()
                .antMatchers(HttpMethod.POST, "/student").permitAll()
                .antMatchers(HttpMethod.POST, "/professor").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers(HttpMethod.POST).hasAuthority("WRITE_PRIVILEGE")
                .antMatchers(HttpMethod.DELETE).hasAuthority("DELETE_PRIVILEGE")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/perform_login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .and()
                .logout()
                .logoutUrl("/perform_logout")
                .deleteCookies("JSESSIONID");

    }

}
