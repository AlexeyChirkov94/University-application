package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.PrivilegeRepository;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.entity.Privilege;
import ua.com.foxminded.university.entity.Role;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertPrivileges;
import static ua.com.foxminded.university.testUtils.TestUtility.assertRoles;

public class RoleRepositoryImplTest {

    ApplicationContext context;
    RoleRepository roleRepository;
    PrivilegeRepository privilegeRepository;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        roleRepository = context.getBean(RoleRepository.class);
        privilegeRepository = context.getBean(PrivilegeRepository.class);
    }

    @Test
    void createAndReadShouldAddNewRoleToDatabaseIfArgumentIsRole(){
        Role addingRole = Role.builder().withName("new Role").build();
        roleRepository.save(addingRole);
        Role readingRole = roleRepository.findById(5L).get();

        assertRoles(readingRole, addingRole);
    }

    @Test
    void createAndReadShouldAddListOfNewRolesToDatabaseIfArgumentIsListOfRoles(){
        List<Role> addingRoleEntities = Arrays.asList (Role.builder().withName("new Role").build());
        roleRepository.saveAll(addingRoleEntities);
        List<Role> readingRoleEntities = Arrays.asList(roleRepository.findById(5L).get());

        assertRoles(readingRoleEntities, addingRoleEntities);
    }

    @Test
    void updateShouldUpdateDataOfRoleIfArgumentIsRole(){
        Role expected = Role.builder()
                .withId(1L)
                .withName("updated role")
                .build();

        roleRepository.save(expected);
        Role actual = roleRepository.findById(1L).get();

        assertRoles(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfRolesIfArgumentIsListOfRoles() {
        List<Role> expected = Arrays.asList(Role.builder()
                .withId(1L)
                .withName("updated roles")
                .build());
        roleRepository.saveAll(expected);
        List<Role> actual = Arrays.asList(roleRepository.findById(1L).get());

        assertRoles(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfRoleIfArgumentIsIdOfRole(){
        Optional<Role> expected = Optional.empty();
        roleRepository.deleteById(4L);
        Optional<Role> actual = roleRepository.findById(4L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfRoleEntityIfArgumentIsName(){
        Role expected = Role.builder()
                .withName("ROLE_STUDENT")
                .build();
        Role actual = roleRepository.findAllByName("ROLE_STUDENT").get(0);

        assertRoles(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<Role> expected = Collections.emptyList();
        List<Role> actual = roleRepository.findAllByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void addPrivilegeToRoleShouldAddPrivilegeToRoleIfArgumentsAreIdeOfRoleAndIdOfPrivilege(){
        List<Privilege> expectedBeforeAdding = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build(),
                Privilege.builder().withName("WRITE_PRIVILEGE").build());
        List<Privilege> actualBeforeAdding = privilegeRepository.findAllByRoleId(2L);
        assertPrivileges(actualBeforeAdding, expectedBeforeAdding);

        roleRepository.addPrivilegeToRole(2, 3);

        List<Privilege> expectedAfterAdding = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build(),
                Privilege.builder().withName("WRITE_PRIVILEGE").build(), Privilege.builder().withName("DELETE_PRIVILEGE").build());
        List<Privilege> actualAfterAdding = privilegeRepository.findAllByRoleId(2L);
        assertPrivileges(actualAfterAdding, expectedAfterAdding);
    }

    @Test
    void removePrivilegeFromRoleShouldRemovePrivilegeToRoleIfArgumentsAreIdeOfRoleAndIdOfPrivilege(){
        List<Privilege> expectedBeforeRemoving = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build(),
                Privilege.builder().withName("WRITE_PRIVILEGE").build());
        List<Privilege> actualBeforeRemoving = privilegeRepository.findAllByRoleId(2L);
        assertPrivileges(actualBeforeRemoving, expectedBeforeRemoving);

        roleRepository.removePrivilegeFromRole(2, 2);

        List<Privilege> expectedAfterRemoving = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build());
        List<Privilege> actualAfterRemoving = privilegeRepository.findAllByRoleId(2L);
        assertPrivileges(actualAfterRemoving, expectedAfterRemoving);
    }

}
