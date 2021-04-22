package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dao.domain.Page;
import ua.com.foxminded.university.dao.domain.Pageable;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.domain.Department;
import ua.com.foxminded.university.domain.FormOfEducation;
import ua.com.foxminded.university.domain.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertGroups;

public class GroupDaoImplTest {

    ApplicationContext context;
    GroupDao groupDao;
    DepartmentDao departmentDao;
    Department departmentForTest;
    FormOfEducationDao formOfEducationDao;
    FormOfEducation formOfEducationForTest;

    {
        context = new AnnotationConfigApplicationContext(TestsContextConfiguration.class);
        groupDao = context.getBean(GroupDaoImpl.class);
        departmentDao = context.getBean(DepartmentDaoImpl.class);
        departmentForTest = Department.builder().withId((long)0).build();
        formOfEducationDao = context.getBean(FormOfEducationDaoImpl.class);
        formOfEducationForTest = FormOfEducation.builder().withId((long)0).build();
    }

    @Test
    void createAndReadShouldAddNewGroupToDatabaseIfArgumentIsGroup(){
        Group addingGroup = Group.builder()
                .withName("New Test Group")
                .withDepartment(departmentForTest)
                .withFormOfEducation(formOfEducationForTest)
                .build();
        groupDao.save(addingGroup);
        Group readingGroup = groupDao.findById((long)4).get();

        assertGroups(readingGroup, addingGroup);
    }

    @Test
    void createAndReadShouldAddListOfNewGroupsToDatabaseIfArgumentIsListOfGroups(){
        List<Group> addingGroups = Arrays.asList(Group.builder()
                .withName("New Test Group")
                .withDepartment(departmentForTest)
                .withFormOfEducation(formOfEducationForTest)
                .build());
        groupDao.saveAll(addingGroups);
        List<Group> readingGroups = Arrays.asList(groupDao.findById((long)4).get());

        assertGroups(readingGroups, addingGroups);
    }

    @Test
    void updateShouldUpdateDataOfGroupIfArgumentIsGroup(){
        Group expected = Group.builder()
                .withId((long)1)
                .withName("New Test Group")
                .withDepartment(departmentDao.findById((long)1).get())
                .withFormOfEducation(formOfEducationDao.findById((long)1).get())
                .build();
        groupDao.update(expected);
        Group actual = groupDao.findById((long)1).get();

        assertGroups(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfGroupsIfArgumentIsListOfGroups(){
        List<Group> expected = Arrays.asList(Group.builder()
                .withId((long)1)
                .withName("New Test Group")
                .withDepartment(departmentDao.findById((long)1).get())
                .withFormOfEducation(formOfEducationDao.findById((long)1).get())
                .build());
        groupDao.updateAll(expected);
        List<Group> actual = Arrays.asList(groupDao.findById((long)1).get());

        assertGroups(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Group> expected = Optional.empty();
        groupDao.deleteById((long)3);
        Optional<Group> actual = groupDao.findById((long)3);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeFormOfEducationShouldChangeFormOfEducationOfGroupsIfArgumentIsIdOfGroupAndIdOfNewFormOfEducation(){
        Group expectedBeforeChange = Group.builder()
                .withId((long)1)
                .withName("History Group №1")
                .withDepartment(departmentDao.findById((long)1).get())
                .withFormOfEducation(formOfEducationDao.findById((long)1).get())
                .build();
        Group actualBeforeChange = groupDao.findById((long)1).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupDao.changeFormOfEducation(1, 2);

        Group expectedAfterChange = Group.builder()
                .withId((long)1)
                .withName("History Group №1")
                .withDepartment(departmentDao.findById((long)1).get())
                .withFormOfEducation(formOfEducationDao.findById((long)2).get())
                .build();
        Group actualAfterChange = groupDao.findById((long)1).get();
        assertGroups(actualAfterChange, expectedAfterChange);
    }

    @Test
    void findAllWithPagesShouldReturnAllGroupEntitiesInCurrentPageIfArgumentNumberOfPage(){
        List<Group> items = new ArrayList<>();
        items.add(Group.builder()
                .withId((long)1)
                .withName("History Group №1")
                .withDepartment(departmentDao.findById((long)1).get())
                .withFormOfEducation(formOfEducationDao.findById((long)1).get())
                .build());
        items.add(Group.builder()
                .withId((long)2)
                .withName("History Group №2")
                .withDepartment(departmentDao.findById((long)1).get())
                .withFormOfEducation(formOfEducationDao.findById((long)2).get())
                .build());

        Pageable<Group> actual = groupDao.findAll(new Page(1,2),2);
        Pageable<Group> expected = new Pageable<>(items, 1, 2, 2);

        assertThat(actual.getItems()).isEqualTo(expected.getItems());
        assertThat(actual.getPageNumber()).isEqualTo(expected.getPageNumber());
        assertThat(actual.getItemsNumberPerPage()).isEqualTo(expected.getItemsNumberPerPage());
        assertThat(actual.getMaxPageNumber()).isEqualTo(expected.getMaxPageNumber());
    }

}
