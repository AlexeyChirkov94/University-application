package ua.com.foxminded.university.configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.PrivilegeDao;
import ua.com.foxminded.university.dao.ProfessorDao;
import ua.com.foxminded.university.dao.RoleDao;
import ua.com.foxminded.university.entity.Privilege;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Role;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private static boolean alreadySetup = false;

    private final ProfessorDao professorDao;
    private final RoleDao roleDao;
    private final PrivilegeDao privilegeDao;
    private final PasswordEncoder passwordEncoder;

    @Override
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

        if (!professorDao.findByEmail("admin@gmail.com").isPresent()) {
            Professor admin = Professor.builder()
                    .withEmail("admin@gmail.com")
                    .withPassword(passwordEncoder.encode("admin"))
                    .build();
            Professor adminAfterSave = professorDao.save(admin);
            professorDao.addRoleToUser(adminAfterSave.getId(), adminRole.getId());
        }

        alreadySetup = true;
    }

    @Transactional(transactionManager = "txManager")
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = Privilege.builder().withName(name).build();
        Optional<Privilege> searchingPrivilege = privilegeDao.findByName(name);

        return searchingPrivilege.orElseGet(() -> privilegeDao.save(privilege));

    }

    @Transactional(transactionManager = "txManager")
    Role createRoleIfNotFound(String name, List<Privilege> necessaryPrivileges) {

        Role role = Role.builder().withName(name).build();
        Optional<Role> searchingRole = roleDao.findByName(name);

        if(!searchingRole.isPresent()){
            Role roleAfterSave = roleDao.save(role);
            for (Privilege privilege : necessaryPrivileges){
             roleDao.addPrivilegeToRole(roleAfterSave.getId(), privilege.getId());
            }
            return roleAfterSave;
        } else {
            long roleId = searchingRole.get().getId();
            List<Privilege> realRolePrivilege = privilegeDao.findByRoleId(roleId);
            for (Privilege privilege : necessaryPrivileges){
                if(!realRolePrivilege.contains(privilege)){
                    roleDao.addPrivilegeToRole(roleId, privilege.getId());
                }
            }

            return searchingRole.get();
        }
    }

}
