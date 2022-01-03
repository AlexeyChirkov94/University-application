package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.dao.PrivilegeDao;
import ua.com.foxminded.university.dao.RoleDao;
import ua.com.foxminded.university.entity.Privilege;
import ua.com.foxminded.university.entity.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertPrivileges;
import static ua.com.foxminded.university.testUtils.TestUtility.assertRoles;

public class RoleDaoImplTest {

    ApplicationContext context;
    RoleDao roleDao;
    PrivilegeDao privilegeDao;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        roleDao = context.getBean(RoleDaoImpl.class);
        privilegeDao = context.getBean(PrivilegeDaoImpl.class);
    }

    @Test
    void createAndReadShouldAddNewRoleToDatabaseIfArgumentIsRole(){
        Role addingRole = Role.builder().withName("new Role").build();
        roleDao.save(addingRole);
        Role readingRole = roleDao.findById(5L).get();

        assertRoles(readingRole, addingRole);
    }

    @Test
    void createAndReadShouldAddListOfNewRolesToDatabaseIfArgumentIsListOfRoles(){
        List<Role> addingRoleEntities = Arrays.asList (Role.builder().withName("new Role").build());
        roleDao.saveAll(addingRoleEntities);
        List<Role> readingRoleEntities = Arrays.asList(roleDao.findById(5L).get());

        assertRoles(readingRoleEntities, addingRoleEntities);
    }

    @Test
    void updateShouldUpdateDataOfRoleIfArgumentIsRole(){
        Role expected = Role.builder()
                .withId(1L)
                .withName("updated role")
                .build();

        roleDao.update(expected);
        Role actual = roleDao.findById(1L).get();

        assertRoles(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfRolesIfArgumentIsListOfRoles() {
        List<Role> expected = Arrays.asList(Role.builder()
                .withId(1L)
                .withName("updated roles")
                .build());
        roleDao.updateAll(expected);
        List<Role> actual = Arrays.asList(roleDao.findById(1L).get());

        assertRoles(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfRoleIfArgumentIsIdOfRole(){
        Optional<Role> expected = Optional.empty();
        roleDao.deleteById(4L);
        Optional<Role> actual = roleDao.findById(4L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfRoleEntityIfArgumentIsName(){
        Role expected = Role.builder()
                .withName("ROLE_STUDENT")
                .build();
        Role actual = roleDao.findByName("ROLE_STUDENT").get();

        assertRoles(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        Optional<Role> expected = Optional.empty();
        Optional<Role> actual = roleDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void addPrivilegeToRoleShouldAddPrivilegeToRoleIfArgumentsAreIdeOfRoleAndIdOfPrivilege(){
        List<Privilege> expectedBeforeAdding = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build(),
                Privilege.builder().withName("WRITE_PRIVILEGE").build());
        List<Privilege> actualBeforeAdding = privilegeDao.findByRoleId(2L);
        assertPrivileges(actualBeforeAdding, expectedBeforeAdding);

        roleDao.addPrivilegeToRole(2, 3);

        List<Privilege> expectedAfterAdding = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build(),
                Privilege.builder().withName("WRITE_PRIVILEGE").build(), Privilege.builder().withName("DELETE_PRIVILEGE").build());
        List<Privilege> actualAfterAdding = privilegeDao.findByRoleId(2L);
        assertPrivileges(actualAfterAdding, expectedAfterAdding);
    }

    @Test
    void RemovePrivilegeFromRoleShouldRemovePrivilegeToRoleIfArgumentsAreIdeOfRoleAndIdOfPrivilege(){
        List<Privilege> expectedBeforeRemoving = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build(),
                Privilege.builder().withName("WRITE_PRIVILEGE").build());
        List<Privilege> actualBeforeRemoving = privilegeDao.findByRoleId(2L);
        assertPrivileges(actualBeforeRemoving, expectedBeforeRemoving);

        roleDao.removePrivilegeFromRole(2, 2);

        List<Privilege> expectedAfterRemoving = Arrays.asList(Privilege.builder().withName("READ_PRIVILEGE").build());
        List<Privilege> actualAfterRemoving = privilegeDao.findByRoleId(2L);
        assertPrivileges(actualAfterRemoving, expectedAfterRemoving);
    }

}
