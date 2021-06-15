package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Student;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsers;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsersStudents;

public class StudentDaoImplTest {

    ApplicationContext context;
    StudentDao studentDao;
    GroupDao groupDao;
    Group groupForTests;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        studentDao = context.getBean(StudentDaoImpl.class);
        groupDao = context.getBean(GroupDaoImpl.class);
        groupForTests = Group.builder().withId((long)0).build();
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
        Student readingStudent = studentDao.findById((long)11).get();

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
        List<Student> readingStudent = Arrays.asList (studentDao.findById((long)11).get());

        assertUsersStudents(readingStudent, addingStudent);
    }

    @Test
    void updateShouldUpdateDataOfStudentIfArgumentIsStudent(){
        Student expected = Student.builder()
                .withId((long)3)
                .withFirstName("Nikita")
                .withLastName("Grigirev")
                .withEmail("newEmailGrigirev@gmail.com")
                .withPassword("1234")
                .withGroup(groupDao.findById((long)1).get())
                .build();
        studentDao.update(expected);
        Student actual = studentDao.findById((long)3).get();

        assertUsers(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfStudentsIfArgumentIsListOfStudent(){
        List<Student> expected = Arrays.asList(Student.builder()
                .withId((long)3)
                .withFirstName("Nikita")
                .withLastName("Grigirev")
                .withEmail("newEmailGrigirev@gmail.com")
                .withPassword("1234")
                .withGroup(groupDao.findById((long)1).get())
                .build());
        studentDao.updateAll(expected);
        List<Student> actual = Arrays.asList(studentDao.findById((long)3).get());

        assertUsersStudents(actual, expected);
    }


    @Test
    void deleteShouldDeleteDataOfStudentIfArgumentIsIdOfStudent(){
        Optional<Student> expected = Optional.empty();
        studentDao.deleteById((long)3);
        Optional<Student> actual = studentDao.findById((long)3);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByEmailShouldReturnOptionalOfStudentEntityIfArgumentIsEmail(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(groupDao.findById((long)1).get())
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
    void leaveGroupShouldDeleteStudentsFormGroupIfArgumentIsIdOfStudent(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(groupForTests)
                .build();

        studentDao.leaveGroup(1);
        Student actual = studentDao.findById((long)1).get();

        assertUsers(actual, expected);
    }

    @Test
    void enterGroupShouldEditGroupOfStudentIfArgumentIsIdOfStudentAndIdOfNewGroup(){
        Student expected = Student.builder()
                .withFirstName("Alexey")
                .withLastName("Chrikov")
                .withEmail("chrikov@gmail.com")
                .withPassword("1234")
                .withGroup(Group.builder().withId((long)2).withName("History Group №2").build())
                .build();

        studentDao.enterGroup(1,2);
        Student actual = studentDao.findById((long)1).get();

        assertUsers(actual, expected);
    }



}
