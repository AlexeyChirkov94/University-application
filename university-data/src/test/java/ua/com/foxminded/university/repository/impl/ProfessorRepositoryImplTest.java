package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.ProfessorRepository;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.ScienceDegree;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertRoles;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsers;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsersProfessors;

class ProfessorRepositoryImplTest {

    ApplicationContext context;
    ProfessorRepository professorRepository;
    DepartmentRepository departmentRepository;
    RoleRepository roleRepository;
    Department departmentForTest;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        professorRepository = context.getBean(ProfessorRepository.class);
        departmentRepository = context.getBean(DepartmentRepository.class);
        roleRepository = context.getBean(RoleRepository.class);
        departmentForTest = Department.builder().withId(0L).build();
    }

    @Test
    void createAndReadShouldAddNewProfessorToDatabaseIfArgumentIsProfessor(){
        Professor addingProfessor = Professor.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build();
        professorRepository.save(addingProfessor);
        Professor readingProfessor = professorRepository.findById(11L).get();

        assertUsers(readingProfessor, addingProfessor);
    }

    @Test
    void createAndReadShouldAddListOfNewProfessorToDatabaseIfArgumentIsListOfProfessors(){
        List<Professor> addingProfessorEntities = Arrays.asList (Professor.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build());
        professorRepository.saveAll(addingProfessorEntities);
        List<Professor> readingProfessorEntities = Arrays.asList(professorRepository.findById(11L).get());

        assertUsersProfessors(readingProfessorEntities, addingProfessorEntities);
    }

    @Test
    void updateShouldUpdateDataOfProfessorIfArgumentIsProfessor(){
        Professor expected = Professor.builder()
                .withId(9L)
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentRepository.findById(1L).get())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build();
        professorRepository.save(expected);
        Professor actual = professorRepository.findById(9L).get();

        assertUsers(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfProfessorIfArgumentIsListOfProfessor(){
        List<Professor> expected = Arrays.asList(Professor.builder()
                .withId(9L)
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentRepository.findById(1L).get())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build());
        professorRepository.saveAll(expected);
        List<Professor> actual = Arrays.asList(professorRepository.findById(9L).get());

        assertUsersProfessors(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Professor> expected = Optional.empty();
        professorRepository.deleteById(10L);
        Optional<Professor> actual = professorRepository.findById(10L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllPageableShouldReturnStudentsFromSecondPage(){
        Page<Professor> expected = new PageImpl(Arrays.asList(professorRepository.findById(9L).get(), professorRepository.findById(10L).get()));
        Page<Professor> actual = professorRepository.findAll(PageRequest.of(1, 2));

        List<Professor> expectedProfessor = expected.get().collect(Collectors.toList());
        List<Professor> actualProfessor = actual.get().collect(Collectors.toList());

        assertUsersProfessors(actualProfessor, expectedProfessor);
    }

    @Test
    void findByEmailShouldReturnOptionalOfProfessorEntityIfArgumentIsEmail(){
        Professor expected = Professor.builder()
                .withFirstName("Ivan")
                .withLastName("Petrov")
                .withEmail("petrov@gmail.com")
                .withPassword("RI")
                .withDepartment(departmentRepository.findById(1L).get())
                .withScienceDegree(ScienceDegree.MASTER)
                .build();
        Professor actual = professorRepository.findAllByEmail("petrov@gmail.com").get(0);

        assertUsers(actual, expected);
    }

    @Test
    void findByEmailShouldReturnOptionalEmptyIfArgumentIsEmailAndItDontExist(){
        List<Professor> expected = Collections.emptyList();
        List<Professor> actual = professorRepository.findAllByEmail("unknownemail@gmail.com");

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeScienceDegreeShouldChangeScienceDegreeOfProfessorIfArgumentIsIdOfProfessorAndIdOfNewScienceDegree(){
        Professor expected = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentRepository.findById(1L).get())
                .withScienceDegree(ScienceDegree.PH_D_CANDIDATE)
                .build();
        professorRepository.changeScienceDegree(10, 2);
        Professor actual = professorRepository.findById(10L).get();

        assertUsers(actual, expected);
    }

    @Test
    void findByCourseIdShouldFindListOfProfessorsIfArgumentIsIdOfCourse(){
        List<Professor> expected = Arrays.asList(professorRepository.findById(7L).get(),
                professorRepository.findById(9L).get());
        List<Professor> actual = professorRepository.findAllByCourseId(1);

        assertUsersProfessors(actual, expected);
    }

    @Test
    void findByDepartmentIdShouldFindListOfProfessorsIfArgumentIsIdOfDepartment(){
        List<Professor> expected = Arrays.asList(professorRepository.findById(7L).get(),
                professorRepository.findById(8L).get(), professorRepository.findById(9L).get(), professorRepository.findById(10L).get());
        List<Professor> actual = professorRepository.findAllByDepartmentId(1);

        assertUsersProfessors(actual, expected);
    }

    @Test
    void removeDepartmentFromProfessorShouldRemoveDepartmentFromProfessorIfArgumentIsIdOfProfessor(){
        Professor expectedBeforeChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentRepository.findById(1L).get())
                .withScienceDegree(ScienceDegree.PH_D)
                .build();
        Professor actualBeforeChanging = professorRepository.findById(10L).get();
        assertUsers(actualBeforeChanging, expectedBeforeChanging);

        professorRepository.removeDepartmentFromProfessor(10L);

        Professor expectedAfterChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(Department.builder().withId(0L).build())
                .withScienceDegree(ScienceDegree.PH_D)
                .build();
        Professor actualAfterChanging = professorRepository.findById(10L).get();

        assertThat(actualAfterChanging.getFirstName()).isEqualTo(expectedAfterChanging.getFirstName());
        assertThat(actualAfterChanging.getLastName()).isEqualTo(expectedAfterChanging.getLastName());
        assertThat(actualAfterChanging.getEmail()).isEqualTo(expectedAfterChanging.getEmail());
        assertThat(actualAfterChanging.getPassword()).isEqualTo(expectedAfterChanging.getPassword());
        assertThat(actualAfterChanging.getDepartment()).isNull();
        assertThat(actualAfterChanging.getScienceDegree()).isEqualTo(expectedAfterChanging.getScienceDegree());
    }

    @Test
    void changeDepartmentFromProfessorShouldChangeDepartmentOfProfessorIfArgumentIsIdOfProfessorAndIdOfNewDepartment(){
        Professor expectedBeforeChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentRepository.findById(1L).get())
                .withScienceDegree(ScienceDegree.PH_D)
                .build();
        Professor actualBeforeChanging = professorRepository.findById(10L).get();
        assertUsers(actualBeforeChanging, expectedBeforeChanging);

        professorRepository.changeDepartment(10L, 2L);

        Professor expectedAfterChanging = Professor.builder()
                .withId(10L)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentRepository.findById(2L).get())
                .withScienceDegree(ScienceDegree.PH_D)
                .build();
        Professor actualAfterChanging = professorRepository.findById(10L).get();
        assertUsers(actualAfterChanging, expectedAfterChanging);
    }

    @Test
    void addRoleToUserShouldAddRoleToProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){

        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(2L).withName("ROLE_PROFESSOR").build());
        List<Role> actualListOfRoleBeforeAdding = roleRepository.findAllByUserId(7L);
        assertRoles(actualListOfRoleBeforeAdding, expectedListOfRoleBeforeAdding);

        professorRepository.addRoleToUser(7L, 3L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList(Role.builder().withId(2L).withName("ROLE_PROFESSOR").build(),
                Role.builder().withId(3L).withName("ROLE_ADMIN").build());
        List<Role> actualListOfRoleAfterAdding = roleRepository.findAllByUserId(7L);
        assertRoles(actualListOfRoleAfterAdding, expectedListOfRoleAfterAdding);
    }

    @Test
    void removeRoleFromUserShouldRemoveRoleFromProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){

        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(2L).withName("ROLE_PROFESSOR").build());
        List<Role> actualListOfRoleBeforeAdding = roleRepository.findAllByUserId(7L);
        assertRoles(actualListOfRoleBeforeAdding, expectedListOfRoleBeforeAdding);

        professorRepository.removeRoleFromUser(7L, 2L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList();
        List<Role> actualListOfRoleAfterAdding = roleRepository.findAllByUserId(7L);
        assertRoles(actualListOfRoleAfterAdding, expectedListOfRoleAfterAdding);
    }

}
