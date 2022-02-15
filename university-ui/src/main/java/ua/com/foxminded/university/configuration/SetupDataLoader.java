package ua.com.foxminded.university.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.PrivilegeRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.entity.Privilege;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Role;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean alreadySetup = false;

    private final ProfessorRepository professorRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (alreadySetup){
            return;
        }

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege, deletePrivilege);
        List<Privilege> professorPrivileges = Arrays.asList(readPrivilege, writePrivilege);
        List<Privilege> studentPrivileges = Collections.singletonList(readPrivilege);

        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_PROFESSOR", professorPrivileges);
        createRoleIfNotFound("ROLE_STUDENT", studentPrivileges);

        if (professorRepository.findAllByEmail("admin@gmail.com").isEmpty()) {
            Professor admin = Professor.builder()
                    .withEmail("admin@gmail.com")
                    .withPassword(passwordEncoder.encode("admin"))
                    .build();
            Professor adminAfterSave = professorRepository.save(admin);
            professorRepository.addRoleToUser(adminAfterSave.getId(), adminRole.getId());
        }

        alreadySetup = true;
    }


    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = Privilege.builder().withName(name).build();
        List<Privilege> searchingPrivilege = privilegeRepository.findAllByName(name);

        if (!searchingPrivilege.isEmpty()){
            return searchingPrivilege.get(0);
        } else {
            return privilegeRepository.save(privilege);
        }

    }

    private Role createRoleIfNotFound(String name, List<Privilege> necessaryPrivileges) {

        Role role = Role.builder().withName(name).build();
        List<Role> searchingRole = roleRepository.findAllByName(name);

        if(searchingRole.isEmpty()){
            Role roleAfterSave = roleRepository.save(role);
            for (Privilege privilege : necessaryPrivileges){
             roleRepository.addPrivilegeToRole(roleAfterSave.getId(), privilege.getId());
            }
            return roleAfterSave;
        } else {
            long roleId = searchingRole.get(0).getId();
            List<Privilege> realRolePrivilege = privilegeRepository.findAllByRoleId(roleId);
            for (Privilege privilege : necessaryPrivileges){
                if(!realRolePrivilege.contains(privilege)){
                    roleRepository.addPrivilegeToRole(roleId, privilege.getId());
                }
            }

            return searchingRole.get(0);
        }
    }

}
