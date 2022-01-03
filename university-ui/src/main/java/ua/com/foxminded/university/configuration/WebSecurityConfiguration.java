package ua.com.foxminded.university.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Import(WebApplicationConfiguration.class)
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

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
