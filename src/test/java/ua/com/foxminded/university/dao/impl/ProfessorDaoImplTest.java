package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.domain.Department;
import ua.com.foxminded.university.domain.Professor;
import ua.com.foxminded.university.domain.ScienceDegree;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsers;
import static ua.com.foxminded.university.testUtils.TestUtility.assertUsersProfessors;

public class ProfessorDaoImplTest {

    ApplicationContext context;
    ProfessorDao professorDao;
    DepartmentDao departmentDao;
    Department departmentForTest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        professorDao = context.getBean(ProfessorDaoImpl.class);
        departmentDao = context.getBean(DepartmentDaoImpl.class);
        departmentForTest = Department.builder().withId((long)0).build();
    }

    @Test
    void createAndReadShouldAddNewProfessorToDatabaseIfArgumentIsProfessor(){
        Professor addingProfessor = Professor.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentForTest)
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build();
        professorDao.save(addingProfessor);
        Professor readingProfessor = professorDao.findById((long)11).get();

        assertUsers(readingProfessor, addingProfessor);
    }

    @Test
    void createAndReadShouldAddListOfNewProfessorToDatabaseIfArgumentIsListOfProfessors(){
        List<Professor> addingProfessor = Arrays.asList (Professor.builder()
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentForTest)
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build());
        professorDao.saveAll(addingProfessor);
        List<Professor> readingProfessor = Arrays.asList(professorDao.findById((long)11).get());

        assertUsersProfessors(readingProfessor, addingProfessor);
    }

    @Test
    void updateShouldUpdateDataOfProfessorIfArgumentIsProfessor(){
        Professor expected = Professor.builder()
                .withId((long)9)
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById((long)1).get())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build();
        professorDao.update(expected);
        Professor actual = professorDao.findById((long)9).get();

        assertUsers(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfProfessorIfArgumentIsListOfProfessor(){
        List<Professor> expected = Arrays.asList(Professor.builder()
                .withId((long)9)
                .withFirstName("Alex")
                .withLastName("Chirkov")
                .withEmail("chirkov@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById((long)1).get())
                .withScienceDegree(ScienceDegree.GRADUATE)
                .build());
        professorDao.updateAll(expected);
        List<Professor> actual = Arrays.asList(professorDao.findById((long)9).get());

        assertUsersProfessors(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Professor> expected = Optional.empty();
        professorDao.deleteById((long)10);
        Optional<Professor> actual = professorDao.findById((long)10);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeScienceDegreeShouldChangeScienceDegreeOfProfessorIfArgumentIsIdOfProfessorAndIdOfNewScienceDegree(){
        Professor expected = Professor.builder()
                .withId((long)10)
                .withFirstName("Ivan")
                .withLastName("Mazurin")
                .withEmail("Mazurin@gmail.com")
                .withPassword("1234")
                .withDepartment(departmentDao.findById((long)1).get())
                .withScienceDegree(ScienceDegree.MASTER)
                .build();
        professorDao.changeScienceDegree(10, 2);
        Professor actual = professorDao.findById((long)10).get();

        assertUsers(actual, expected);
    }

    @Test
    void findByCourseIdShouldFindListOfProfessorsIfArgumentIsIdOfCourse(){
        List<Professor> expected = Arrays.asList(professorDao.findById((long)7).get(),
                professorDao.findById((long)9).get());
        List<Professor> actual = professorDao.findByCourseId(1);

        assertUsersProfessors(actual, expected);
    }

}
