package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.dao.PrivilegeDao;
import ua.com.foxminded.university.entity.Privilege;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertPrivileges;

public class PrivilegeDaoImplTest {

    ApplicationContext context;
    PrivilegeDao privilegeDao;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        privilegeDao = context.getBean(PrivilegeDaoImpl.class);
    }

    @Test
    void createAndReadShouldAddNewPrivilegeToDatabaseIfArgumentIsPrivilege(){
        Privilege addingPrivilege = Privilege.builder().withName("new Privilege").build();
        privilegeDao.save(addingPrivilege);
        Privilege readingPrivilege = privilegeDao.findById(5L).get();

        assertPrivileges(readingPrivilege, addingPrivilege);
    }

    @Test
    void createAndReadShouldAddListOfNewPrivilegesToDatabaseIfArgumentIsListOfPrivileges(){
        List<Privilege> addingPrivilegeEntities = Arrays.asList (Privilege.builder().withName("new Privilege").build());
        privilegeDao.saveAll(addingPrivilegeEntities);
        List<Privilege> readingPrivilegeEntities = Arrays.asList(privilegeDao.findById(5L).get());

        assertPrivileges(readingPrivilegeEntities, addingPrivilegeEntities);
    }

    @Test
    void updateShouldUpdateDataOfPrivilegeIfArgumentIsPrivilege(){
        Privilege expected = Privilege.builder()
                .withId(1L)
                .withName("History")
                .build();

        privilegeDao.update(expected);
        Privilege actual = privilegeDao.findById(1L).get();

        assertPrivileges(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfPrivilegesIfArgumentIsListOfPrivileges() {
        List<Privilege> expected = Arrays.asList(Privilege.builder()
                .withId(1L)
                .withName("History")
                .build());
        privilegeDao.updateAll(expected);
        List<Privilege> actual = Arrays.asList(privilegeDao.findById(1L).get());

        assertPrivileges(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfPrivilegeIfArgumentIsIdOfPrivilege(){
        Optional<Privilege> expected = Optional.empty();
        privilegeDao.deleteById(4L);
        Optional<Privilege> actual = privilegeDao.findById(4L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfPrivilegeEntityIfArgumentIsName(){
        Privilege expected = Privilege.builder()
                .withName("READ_PRIVILEGE")
                .build();
        Privilege actual = privilegeDao.findByName("READ_PRIVILEGE").get(0);

        assertPrivileges(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<Privilege> expected = Collections.emptyList();
        List<Privilege> actual = privilegeDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
