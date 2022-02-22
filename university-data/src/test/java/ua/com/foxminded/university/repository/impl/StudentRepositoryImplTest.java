package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.Student;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertRoles;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsers;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsersStudents;

class StudentRepositoryImplTest {

    ApplicationContext context;
    StudentRepository studentRepository;
    GroupRepository groupRepository;
    RoleRepository roleRepository;
    Group groupForTests;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        studentRepository = context.getBean(StudentRepository.class);
        groupRepository = context.getBean(GroupRepository.class);
        roleRepository = context.getBean(RoleRepository.class);
        groupForTests = Group.builder().withId(0L).build();
    }

    @Test
    void createAndReadShouldAddNewStudentToDatabaseIfArgumentIsStudent(){
        Student addingStudent = Student.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder().withId(1L).withName("History Group #1").build())
                .build();
        studentRepository.save(addingStudent);
        Student readingStudent = studentRepository.findById(11L).get();

        assertUsers(readingStudent, addingStudent);
    }

    @Test
    void createAndReadShouldAddListOfNewStudentToDatabaseIfArgumentIsListOfStudents(){
        List<Student> addingStudent = Arrays.asList (Student.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder().withId(1L).withName("History Group #1").build())
                .build());
        studentRepository.saveAll(addingStudent);
        List<Student> readingStudent = Arrays.asList (studentRepository.findById(11L).get());

        assertUsersStudents(readingStudent, addingStudent);
    }

    @Test
    void updateShouldUpdateDataOfStudentIfArgumentIsStudent(){
        Student expected = Student.builder()
                .withId(3L)
                .withFirstName("Nikita")
                .withLastName("Grigirev")
                .withEmail("newEmailGrigirev@gmail.com")
                .withPassword("1234")
                .withGroup(groupRepository.findById(1L).get())
                .build();
        studentRepository.save(expected);
        Student actual = studentRepository.findById(3L).get();

        assertUsers(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfStudentsIfArgumentIsListOfStudent(){
        List<Student> expected = Arrays.asList(Student.builder()
                .withId(3L)
                .withFirstName("Nikita")
                .withLastName("Grigirev")
                .withEmail("newEmailGrigirev@gmail.com")
                .withPassword("1234")
                .withGroup(groupRepository.findById(1L).get())
                .build());
        studentRepository.saveAll(expected);
        List<Student> actual = Arrays.asList(studentRepository.findById(3L).get());

        assertUsersStudents(actual, expected);
    }


    @Test
    void deleteShouldDeleteDataOfStudentIfArgumentIsIdOfStudent(){
        Optional<Student> expected = Optional.empty();
        studentRepository.deleteById(3L);
        Optional<Student> actual = studentRepository.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllPageableShouldReturnStudentsFromSecondPage(){
        Page<Student> expected = new PageImpl(Arrays.asList(studentRepository.findById(3L).get(), studentRepository.findById(4L).get()));
        Page<Student> actual = studentRepository.findAll(PageRequest.of(1, 2));

        List<Student> expectedStudent = expected.get().collect(Collectors.toList());
        List<Student> actualStudent = actual.get().collect(Collectors.toList());

        assertUsersStudents(actualStudent, expectedStudent);
    }

    @Test
    void findByEmailShouldReturnOptionalOfStudentEntityIfArgumentIsEmail(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(groupRepository.findById(1L).get())
                .build();
        Student actual = studentRepository.findAllByEmail("chrikov@gmail.com").get(0);

        assertUsers(actual, expected);
    }

    @Test
    void findByEmailShouldReturnOptionalEmptyIfArgumentIsEmailAndItDontExist(){
        List<Student> expected = Collections.emptyList();
        List<Student> actual = studentRepository.findAllByEmail("unknownemail@gmail.com");

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByGroupIdShouldReturnListOfStudentIfArgumentIsGroupId(){
        List<Student> expected = Arrays.asList(
                Student.builder()
                    .withFirstName("Maksim")
                    .withLastName("Panichev")
                    .withEmail("panichev@gmail.com")
                    .withPassword("1234")
                    .withGroup(groupRepository.findById(2L).get())
                    .build(),
                Student.builder()
                    .withFirstName("Petr")
                    .withLastName("Elematov")
                    .withEmail("elematov@gmail.com")
                    .withPassword("1234")
                    .withGroup(groupRepository.findById(2L).get())
                    .build()
        );

        List<Student> actual = studentRepository.findAllByGroupId(2L);

        assertUsersStudents(actual, expected);
    }

    @Test
    void leaveGroupShouldDeleteStudentsFormGroupIfArgumentIsIdOfStudent(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder().withId(1L).withName("History Group #1").build())
                .build();

        studentRepository.leaveGroup(1);
        Student actual = studentRepository.findById(1L).get();

        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getGroup()).isNull();
    }

    @Test
    void enterGroupShouldEditGroupOfStudentIfArgumentIsIdOfStudentAndIdOfNewGroup(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder().withId(2L).withName("History Group #2").build())
                .build();

        studentRepository.enterGroup(1,2);
        Student actual = studentRepository.findById(1L).get();

        assertUsers(actual, expected);
    }

    @Test
    void addRoleToUserShouldAddRoleToProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){
        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(1L).withName("ROLE_STUDENT").build());
        List<Role> actualListOfRoleBeforeAdding = roleRepository.findAllByUserId(1L);
        assertRoles(actualListOfRoleBeforeAdding, expectedListOfRoleBeforeAdding);

        studentRepository.addRoleToUser(1L, 3L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList(Role.builder().withId(1L).withName("ROLE_STUDENT").build(),
                Role.builder().withId(3L).withName("ROLE_ADMIN").build());
        List<Role> actualListOfRoleAfterAdding = roleRepository.findAllByUserId(1L);
        assertRoles(actualListOfRoleAfterAdding, expectedListOfRoleAfterAdding);
    }

    @Test
    void removeRoleFromUserShouldRemoveRoleFromProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){
        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(1L).withName("ROLE_STUDENT").build());
        List<Role> actualListOfRoleBeforeAdding = roleRepository.findAllByUserId(1L);
        assertRoles(actualListOfRoleBeforeAdding, expectedListOfRoleBeforeAdding);

        studentRepository.removeRoleFromUser(1L, 1L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList();
        List<Role> actualListOfRoleAfterAdding = roleRepository.findAllByUserId(1L);
        assertRoles(actualListOfRoleAfterAdding, expectedListOfRoleAfterAdding);
    }

}
