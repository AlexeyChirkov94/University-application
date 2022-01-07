package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.com.foxminded.university.dao.PrivilegeDao;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.entity.Privilege;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.service.ProfessorService;
import ua.com.foxminded.university.service.StudentService;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j
@Service
@AllArgsConstructor
public class ApplicationUserDetails implements UserDetailsService {

    private final ProfessorService professorService;
    private final StudentService studentService;
    private final PrivilegeDao privilegeDao;

    @Override
    public UserDetails loadUserByUsername(final String email)
            throws UsernameNotFoundException {

        log.info("Load user by email: " + email);

        Optional<StudentResponse> studentOptional = studentService.findByEmail(email);
        Optional<ProfessorResponse> professorOptional = professorService.findByEmail(email);

        if(studentOptional.isPresent()){
            return getStudentDetails(studentOptional.get());
        }
        if(professorOptional.isPresent()){
            return getProfessorDetails(professorOptional.get());
        } else {
            throw new UsernameNotFoundException("no user with email: " + email);
        }
    }

    private UserDetails getStudentDetails(StudentResponse studentResponse){
        return new UserDetail(studentResponse.getId(), studentResponse.getEmail(), studentResponse.getPassword(),
                getAuthorities(studentResponse.getRoles()));
    }

    private UserDetails getProfessorDetails(ProfessorResponse professorResponse){
        return new UserDetail(professorResponse.getId(), professorResponse.getEmail(), professorResponse.getPassword(),
                getAuthorities(professorResponse.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(
            List<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(List<Role> roles) {
        List<String> allUserPrivileges = roles.stream().map(Role::getName).collect(Collectors.toList());
        List<Privilege> privileges = roles.stream().flatMap(role -> privilegeDao.findByRoleId(role.getId()).stream())
                .collect(Collectors.toList());
        allUserPrivileges.addAll(privileges.stream().map(Privilege::getName).collect(Collectors.toList()));

        return allUserPrivileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {

        return privileges.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    private static class UserDetail extends User {

        private final long id;

        public UserDetail(long id, String username, String password,
                          Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
            this.id = id;
        }

        public long getId() {
            return id;
        }

    }

}
