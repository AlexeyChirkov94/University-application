package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.PrivilegeRepository;
import ua.com.foxminded.university.entity.Privilege;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertPrivileges;

public class PrivilegeRepositoryImplTest {

    ApplicationContext context;
    PrivilegeRepository privilegeRepository;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        privilegeRepository = context.getBean(PrivilegeRepository.class);
    }

    @Test
    void createAndReadShouldAddNewPrivilegeToDatabaseIfArgumentIsPrivilege(){
        Privilege addingPrivilege = Privilege.builder().withName("new Privilege").build();
        privilegeRepository.save(addingPrivilege);
        Privilege readingPrivilege = privilegeRepository.findById(5L).get();

        assertPrivileges(readingPrivilege, addingPrivilege);
    }

    @Test
    void createAndReadShouldAddListOfNewPrivilegesToDatabaseIfArgumentIsListOfPrivileges(){
        List<Privilege> addingPrivilegeEntities = Arrays.asList (Privilege.builder().withName("new Privilege").build());
        privilegeRepository.saveAll(addingPrivilegeEntities);
        List<Privilege> readingPrivilegeEntities = Arrays.asList(privilegeRepository.findById(5L).get());

        assertPrivileges(readingPrivilegeEntities, addingPrivilegeEntities);
    }

    @Test
    void updateShouldUpdateDataOfPrivilegeIfArgumentIsPrivilege(){
        Privilege expected = Privilege.builder()
                .withId(1L)
                .withName("History")
                .build();

        privilegeRepository.save(expected);
        Privilege actual = privilegeRepository.findById(1L).get();

        assertPrivileges(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfPrivilegesIfArgumentIsListOfPrivileges() {
        List<Privilege> expected = Arrays.asList(Privilege.builder()
                .withId(1L)
                .withName("History")
                .build());
        privilegeRepository.saveAll(expected);
        List<Privilege> actual = Arrays.asList(privilegeRepository.findById(1L).get());

        assertPrivileges(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfPrivilegeIfArgumentIsIdOfPrivilege(){
        Optional<Privilege> expected = Optional.empty();
        privilegeRepository.deleteById(4L);
        Optional<Privilege> actual = privilegeRepository.findById(4L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfPrivilegeEntityIfArgumentIsName(){
        Privilege expected = Privilege.builder()
                .withName("READ_PRIVILEGE")
                .build();
        Privilege actual = privilegeRepository.findAllByName("READ_PRIVILEGE").get(0);

        assertPrivileges(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<Privilege> expected = Collections.emptyList();
        List<Privilege> actual = privilegeRepository.findAllByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
