package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.RoleDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Role;
import ua.com.foxminded.university.entity.Student;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsers;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsersStudents;

class StudentDaoImplTest {

    ApplicationContext context;
    StudentDao studentDao;
    GroupDao groupDao;
    RoleDao roleDao;
    Group groupForTests;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        studentDao = context.getBean(StudentDaoImpl.class);
        groupDao = context.getBean(GroupDaoImpl.class);
        roleDao = context.getBean(RoleDaoImpl.class);
        groupForTests = Group.builder().withId(0L).build();
    }

    @Test
    void createAndReadShouldAddNewStudentToDatabaseIfArgumentIsStudent(){
        Student addingStudent = Student.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withGroup(groupForTests)
                .build();
        studentDao.save(addingStudent);
        Student readingStudent = studentDao.findById(11L).get();

        assertUsers(readingStudent, addingStudent);
    }

    @Test
    void createAndReadShouldAddListOfNewStudentToDatabaseIfArgumentIsListOfStudents(){
        List<Student> addingStudent = Arrays.asList (Student.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withGroup(groupForTests)
                .build());
        studentDao.saveAll(addingStudent);
        List<Student> readingStudent = Arrays.asList (studentDao.findById(11L).get());

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
                .withGroup(groupDao.findById(1L).get())
                .build();
        studentDao.update(expected);
        Student actual = studentDao.findById(3L).get();

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
                .withGroup(groupDao.findById(1L).get())
                .build());
        studentDao.updateAll(expected);
        List<Student> actual = Arrays.asList(studentDao.findById(3L).get());

        assertUsersStudents(actual, expected);
    }


    @Test
    void deleteShouldDeleteDataOfStudentIfArgumentIsIdOfStudent(){
        Optional<Student> expected = Optional.empty();
        studentDao.deleteById(3L);
        Optional<Student> actual = studentDao.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByEmailShouldReturnOptionalOfStudentEntityIfArgumentIsEmail(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(groupDao.findById(1L).get())
                .build();
        Student actual = studentDao.findByEmail("chrikov@gmail.com").get();

        assertUsers(actual, expected);
    }

    @Test
    void findByEmailShouldReturnOptionalEmptyIfArgumentIsEmailAndItDontExist(){
        Optional<Student> expected = Optional.empty();
        Optional<Student> actual = studentDao.findByEmail("unknownemail@gmail.com");

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
                    .withGroup(groupDao.findById(2L).get())
                    .build(),
                Student.builder()
                    .withFirstName("Petr")
                    .withLastName("Elematov")
                    .withEmail("elematov@gmail.com")
                    .withPassword("1234")
                    .withGroup(groupDao.findById(2L).get())
                    .build()
        );

        List<Student> actual = studentDao.findByGroupId(2L);

        assertUsersStudents(actual, expected);
    }


    @Test
    void leaveGroupShouldDeleteStudentsFormGroupIfArgumentIsIdOfStudent(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(groupForTests)
                .build();

        studentDao.leaveGroup(1);
        Student actual = studentDao.findById(1L).get();

        assertUsers(actual, expected);
    }

    @Test
    void enterGroupShouldEditGroupOfStudentIfArgumentIsIdOfStudentAndIdOfNewGroup(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder().withId(2L).withName("History Group №2").build())
                .build();

        studentDao.enterGroup(1,2);
        Student actual = studentDao.findById(1L).get();

        assertUsers(actual, expected);
    }

    @Test
    void addRoleToUserShouldAddRoleToProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){
        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(1L).withName("ROLE_STUDENT").build());
        List<Role> actualListOfRoleBeforeAdding = roleDao.findByUserId(1L);
        assertThat(actualListOfRoleBeforeAdding).isEqualTo(expectedListOfRoleBeforeAdding);

        studentDao.addRoleToUser(1L, 3L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList(Role.builder().withId(1L).withName("ROLE_STUDENT").build(),
                Role.builder().withId(3L).withName("ROLE_ADMIN").build());
        List<Role> actualListOfRoleAfterAdding = roleDao.findByUserId(1L);
        assertThat(actualListOfRoleAfterAdding).isEqualTo(expectedListOfRoleAfterAdding);
    }

    @Test
    void removeRoleFromUserShouldRemoveRoleFromProfessorsIfArgumentIsIdOfProfessorAndIdOfRole(){
        List<Role> expectedListOfRoleBeforeAdding  = Arrays.asList(Role.builder().withId(1L).withName("ROLE_STUDENT").build());
        List<Role> actualListOfRoleBeforeAdding = roleDao.findByUserId(1L);
        assertThat(actualListOfRoleBeforeAdding).isEqualTo(expectedListOfRoleBeforeAdding);

        studentDao.removeRoleFromUser(1L, 1L);

        List<Role> expectedListOfRoleAfterAdding  = Arrays.asList();
        List<Role> actualListOfRoleAfterAdding = roleDao.findByUserId(1L);
        assertThat(actualListOfRoleAfterAdding).isEqualTo(expectedListOfRoleAfterAdding);
    }

}
