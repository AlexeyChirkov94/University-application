package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.dao.domain.Page;
import ua.com.foxminded.university.dao.domain.Pageable;
import ua.com.foxminded.university.dao.DepartmentDao;
import ua.com.foxminded.university.dao.FormOfEducationDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertGroups;

class GroupDaoImplTest {

    ApplicationContext context;
    GroupDao groupDao;
    DepartmentDao departmentDao;
    Department departmentForTest;
    FormOfEducationDao formOfEducationDao;
    FormOfEducation formOfEducationForTest;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        groupDao = context.getBean(GroupDaoImpl.class);
        departmentDao = context.getBean(DepartmentDaoImpl.class);
        departmentForTest = Department.builder().withId(0L).build();
        formOfEducationDao = context.getBean(FormOfEducationDaoImpl.class);
        formOfEducationForTest = FormOfEducation.builder().withId(0L).build();
    }

    @Test
    void createAndReadShouldAddNewGroupToDatabaseIfArgumentIsGroup(){
        Group addingGroup = Group.builder()
                .withName("New Test Group")
                .withDepartment(departmentForTest)
                .withFormOfEducation(formOfEducationForTest)
                .build();
        groupDao.save(addingGroup);
        Group readingGroup = groupDao.findById(4L).get();

        assertGroups(readingGroup, addingGroup);
    }

    @Test
    void createAndReadShouldAddListOfNewGroupsToDatabaseIfArgumentIsListOfGroups(){
        List<Group> addingGroupEntities = Arrays.asList(Group.builder()
                .withName("New Test Group")
                .withDepartment(departmentForTest)
                .withFormOfEducation(formOfEducationForTest)
                .build());
        groupDao.saveAll(addingGroupEntities);
        List<Group> readingGroupEntities = Arrays.asList(groupDao.findById(4L).get());

        assertGroups(readingGroupEntities, addingGroupEntities);
    }

    @Test
    void updateShouldUpdateDataOfGroupIfArgumentIsGroup(){
        Group expected = Group.builder()
                .withId(1L)
                .withName("New Test Group")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        groupDao.update(expected);
        Group actual = groupDao.findById(1L).get();

        assertGroups(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfGroupsIfArgumentIsListOfGroups(){
        List<Group> expected = Arrays.asList(Group.builder()
                .withId(1L)
                .withName("New Test Group")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build());
        groupDao.updateAll(expected);
        List<Group> actual = Arrays.asList(groupDao.findById(1L).get());

        assertGroups(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Group> expected = Optional.empty();
        groupDao.deleteById(3L);
        Optional<Group> actual = groupDao.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeFormOfEducationShouldChangeFormOfEducationOfGroupsIfArgumentIsIdOfGroupAndIdOfNewFormOfEducation(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        Group actualBeforeChange = groupDao.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupDao.changeFormOfEducation(1, 2);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(2L).get())
                .build();
        Group actualAfterChange = groupDao.findById(1L).get();
        assertGroups(actualAfterChange, expectedAfterChange);
    }

    @Test
    void removeFormOfEducationShouldChangeFormOfEducationOfGroupsIfArgumentIsIdOfGroupAndIdOfNewFormOfEducation(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        Group actualBeforeChange = groupDao.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupDao.removeFormOfEducationFromGroup(1);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(FormOfEducation.builder().withId(0L).build())
                .build();
        Group actualAfterChange = groupDao.findById(1L).get();
        assertGroups(actualAfterChange, expectedAfterChange);
    }

    @Test
    void changeDepartmentShouldChangeDepartmentOfGroupIfArgumentIsIdOfGroupAndIdOfNewDepartment(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        Group actualBeforeChange = groupDao.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupDao.changeDepartment(1, 2);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(2L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        Group actualAfterChange = groupDao.findById(1L).get();
        assertGroups(actualAfterChange, expectedAfterChange);
    }

    @Test
    void removeDepartmentShouldChangeDepartmentOfGroupIfArgumentIsIdOfGroupAndIdOfNewDepartment(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        Group actualBeforeChange = groupDao.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupDao.removeDepartmentFromGroup(1);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(Department.builder().withId(0L).build())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        Group actualAfterChange = groupDao.findById(1L).get();
        assertGroups(actualAfterChange, expectedAfterChange);
    }

    @Test
    void findAllWithPagesShouldReturnAllGroupEntitiesInCurrentPageIfArgumentNumberOfPage(){
        List<Group> items = new ArrayList<>();
        items.add(Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build());
        items.add(Group.builder()
                .withId(2L)
                .withName("History Group #2")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(2L).get())
                .build());

        Pageable<Group> actual = groupDao.findAll(new Page(1,2),2);
        Pageable<Group> expected = new Pageable<>(items, 1, 2, 2);

        assertThat(actual.getItems()).isEqualTo(expected.getItems());
        assertThat(actual.getPageNumber()).isEqualTo(expected.getPageNumber());
        assertThat(actual.getItemsNumberPerPage()).isEqualTo(expected.getItemsNumberPerPage());
        assertThat(actual.getMaxPageNumber()).isEqualTo(expected.getMaxPageNumber());
    }

    @Test
    void findByNameShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        Group expected = Group.builder()
                .withName("History Group #1")
                .withDepartment(departmentDao.findById(1L).get())
                .withFormOfEducation(formOfEducationDao.findById(1L).get())
                .build();
        Group actual = groupDao.findByName("History Group #1").get();

        assertGroups(actual, expected);
    }

    @Test
    void findByFromOfEducationIdShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        List<Group> expected = Arrays.asList(groupDao.findById(1L).get());
        List<Group> actual = groupDao.findByFormOfEducation(1L);

        assertGroups(actual, expected);
    }

    @Test
    void findByDepartmentIdShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        List<Group> expected = Arrays.asList(groupDao.findById(1L).get(), groupDao.findById(2L).get(),
                groupDao.findById(3L).get());
        List<Group> actual = groupDao.findByDepartmentId(1L);

        assertGroups(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        Optional<Group> expected = Optional.empty();
        Optional<Group> actual = groupDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
