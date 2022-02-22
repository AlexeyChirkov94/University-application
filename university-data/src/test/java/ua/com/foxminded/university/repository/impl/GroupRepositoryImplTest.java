package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.FormOfEducationRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertGroups;

class GroupRepositoryImplTest {

    ApplicationContext context;
    GroupRepository groupRepository;
    DepartmentRepository departmentRepository;
    Department departmentForTest;
    FormOfEducationRepository formOfEducationRepository;
    FormOfEducation formOfEducationForTest;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        groupRepository = context.getBean(GroupRepository.class);
        departmentRepository = context.getBean(DepartmentRepository.class);
        departmentForTest = Department.builder().withId(1L).withName("Department of History").build();
        formOfEducationRepository = context.getBean(FormOfEducationRepository.class);
        formOfEducationForTest = FormOfEducation.builder().withId(1L).withName("full-time").build();
    }

    @Test
    void createAndReadShouldAddNewGroupToDatabaseIfArgumentIsGroup(){
        Group addingGroup = Group.builder()
                .withName("New Test Group")
                .withDepartment(departmentForTest)
                .withFormOfEducation(formOfEducationForTest)
                .build();
        groupRepository.save(addingGroup);
        Group readingGroup = groupRepository.findById(4L).get();

        assertGroups(readingGroup, addingGroup);
    }

    @Test
    void createAndReadShouldAddListOfNewGroupsToDatabaseIfArgumentIsListOfGroups(){
        List<Group> addingGroupEntities = Arrays.asList(Group.builder()
                .withName("New Test Group")
                .withDepartment(Department.builder().withId(1L).withName("Department of History").build())
                .withFormOfEducation(FormOfEducation.builder().withId(1L).withName("full-time").build())
                .build());
        groupRepository.saveAll(addingGroupEntities);
        List<Group> readingGroupEntities = Arrays.asList(groupRepository.findById(4L).get());

        assertGroups(readingGroupEntities, addingGroupEntities);
    }

    @Test
    void updateShouldUpdateDataOfGroupIfArgumentIsGroup(){
        Group expected = Group.builder()
                .withId(1L)
                .withName("New Test Group")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        groupRepository.save(expected);
        Group actual = groupRepository.findById(1L).get();

        assertGroups(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfGroupsIfArgumentIsListOfGroups(){
        List<Group> expected = Arrays.asList(Group.builder()
                .withId(1L)
                .withName("New Test Group")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build());
        groupRepository.saveAll(expected);
        List<Group> actual = Arrays.asList(groupRepository.findById(1L).get());

        assertGroups(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsIdOfProfessor(){
        Optional<Group> expected = Optional.empty();
        groupRepository.deleteById(3L);
        Optional<Group> actual = groupRepository.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void changeFormOfEducationShouldChangeFormOfEducationOfGroupsIfArgumentIsIdOfGroupAndIdOfNewFormOfEducation(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        Group actualBeforeChange = groupRepository.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupRepository.changeFormOfEducation(1, 2);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(2L).get())
                .build();
        Group actualAfterChange = groupRepository.findById(1L).get();
        assertGroups(actualAfterChange, expectedAfterChange);
    }

    @Test
    void removeFormOfEducationShouldChangeFormOfEducationOfGroupsIfArgumentIsIdOfGroupAndIdOfNewFormOfEducation(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        Group actualBeforeChange = groupRepository.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupRepository.removeFormOfEducationFromGroup(1);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(1L).get())
                .build();
        Group actualAfterChange = groupRepository.findById(1L).get();

        assertThat(actualAfterChange.getName()).isEqualTo(expectedAfterChange.getName());
        assertThat(actualAfterChange.getDepartment().getId()).isEqualTo(expectedAfterChange.getDepartment().getId());
        assertThat(actualAfterChange.getDepartment().getName()).isEqualTo(expectedAfterChange.getDepartment().getName());
        assertThat(actualAfterChange.getFormOfEducation()).isNull();
    }

    @Test
    void changeDepartmentShouldChangeDepartmentOfGroupIfArgumentIsIdOfGroupAndIdOfNewDepartment(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        Group actualBeforeChange = groupRepository.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupRepository.changeDepartment(1, 2);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(2L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        Group actualAfterChange = groupRepository.findById(1L).get();
        assertGroups(actualAfterChange, expectedAfterChange);
    }

    @Test
    void removeDepartmentShouldChangeDepartmentOfGroupIfArgumentIsIdOfGroupAndIdOfNewDepartment(){
        Group expectedBeforeChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        Group actualBeforeChange = groupRepository.findById(1L).get();
        assertGroups(actualBeforeChange, expectedBeforeChange);

        groupRepository.removeDepartmentFromGroup(1);

        Group expectedAfterChange = Group.builder()
                .withId(1L)
                .withName("History Group #1")
                .withDepartment(Department.builder().withId(0L).build())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        Group actualAfterChange = groupRepository.findById(1L).get();

        assertThat(actualAfterChange.getName()).isEqualTo(expectedAfterChange.getName());
        assertThat(actualAfterChange.getDepartment()).isNull();
        assertThat(actualAfterChange.getFormOfEducation().getId()).isEqualTo(expectedAfterChange.getFormOfEducation().getId());
        assertThat(actualAfterChange.getFormOfEducation().getName()).isEqualTo(expectedAfterChange.getFormOfEducation().getName());
    }

    @Test
    void findByNameShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        Group expected = Group.builder()
                .withName("History Group #1")
                .withDepartment(departmentRepository.findById(1L).get())
                .withFormOfEducation(formOfEducationRepository.findById(1L).get())
                .build();
        Group actual = groupRepository.findAllByName("History Group #1").get(0);

        assertGroups(actual, expected);
    }

    @Test
    void findByFromOfEducationIdShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        List<Group> expected = Arrays.asList(groupRepository.findById(1L).get());
        List<Group> actual = groupRepository.findAllByFormOfEducationIdOrderById(1L);

        assertGroups(actual, expected);
    }

    @Test
    void findByDepartmentIdShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        List<Group> expected = Arrays.asList(groupRepository.findById(1L).get(), groupRepository.findById(2L).get(),
                groupRepository.findById(3L).get());
        List<Group> actual = groupRepository.findAllByDepartmentIdOrderById(1L);

        assertGroups(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<Group> expected = Collections.emptyList();
        List<Group> actual = groupRepository.findAllByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
