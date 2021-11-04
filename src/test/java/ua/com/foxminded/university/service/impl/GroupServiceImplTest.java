package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.DepartmentDao;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.LessonDao;
import ua.com.foxminded.university.dao.interfaces.StudentDao;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupDao groupDao;

    @Mock
    StudentDao studentDao;

    @Mock
    LessonDao lessonDao;

    @Mock
    DepartmentDao departmentDao;

    @Mock
    GroupRequestMapper groupRequestMapper;

    @Mock
    GroupResponseMapper groupResponseMapper;

    @Mock
    FormOfEducationDao formOfEducationDao;

    @InjectMocks
    GroupServiceImpl groupService;


    @Test
    void changeFormOfEducationShouldChangeFormOfEducationIfArgumentsIsGroupIdAndFormOfEducationId() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(formOfEducationId).build()));
        doNothing().when(groupDao).changeFormOfEducation(groupId, formOfEducationId);

        groupService.changeFormOfEducation(groupId, formOfEducationId);

        verify(groupDao).findById(groupId);
        verify(formOfEducationDao).findById(formOfEducationId);
        verify(groupDao).changeFormOfEducation(groupId, formOfEducationId);
    }

    @Test
    void removeFormOfEducationFromGroupShouldRemoveFormOfEducationIfArgumentsIsGroupId() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        doNothing().when(groupDao).removeFormOfEducationFromGroup(groupId);

        groupService.removeFormOfEducationFromGroup(groupId);

        verify(groupDao).findById(groupId);
        verify(groupDao).removeFormOfEducationFromGroup(groupId);
    }

    @Test
    void changeFormOfEducationShouldThrowExceptionIfGroupDontExist() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeFormOfEducation(groupId, formOfEducationId)).hasMessage("There no group with this id:1");

        verify(groupDao).findById(groupId);
    }

    @Test
    void changeFormOfEducationShouldThrowExceptionIfProfessorDontExist() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeFormOfEducation(groupId, formOfEducationId)).hasMessage("There no formOfEducation with this id:2");

        verify(groupDao).findById(groupId);
        verify(formOfEducationDao).findById(formOfEducationId);
    }

    @Test
    void changeDepartmentShouldChangeDepartmentIfArgumentsIsGroupIdAndDepartmentId() {
        long groupId = 1;
        long departmentId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(departmentId).build()));
        doNothing().when(groupDao).changeDepartment(groupId, departmentId);

        groupService.changeDepartment(groupId, departmentId);

        verify(groupDao).findById(groupId);
        verify(departmentDao).findById(departmentId);
        verify(groupDao).changeDepartment(groupId, departmentId);
    }

    @Test
    void removeDepartmentFromGroupShouldRemoveDepartmentIfArgumentsIsGroupId() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        doNothing().when(groupDao).removeDepartmentFromGroup(groupId);

        groupService.removeDepartmentFromGroup(groupId);

        verify(groupDao).findById(groupId);
        verify(groupDao).removeDepartmentFromGroup(groupId);
    }

    @Test
    void changeDepartmentShouldThrowExceptionIfGroupDontExist() {
        long groupId = 1;
        long departmentId = 2;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeDepartment(groupId, departmentId)).hasMessage("There no group with this id:1");

        verify(groupDao).findById(groupId);
    }

    @Test
    void changeDepartmentShouldThrowExceptionIfDepartmentDontExist() {
        long groupId = 1;
        long departmentId = 2;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(departmentDao.findById(departmentId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeDepartment(groupId, departmentId)).hasMessage("There no department with this id:2");

        verify(groupDao).findById(groupId);
        verify(departmentDao).findById(departmentId);
    }

    @Test
    void registerShouldAddGroupToDBIfArgumentsIsGroupRequest() {
        String groupName= "Group of Math";
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupName);

        when(groupDao.findByName(groupName)).thenReturn(Optional.empty());

        groupService.create(groupRequest);

        verify(groupDao).findByName(groupName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfGroupWithSameNameAlreadyExist() {
        String groupName= "Group of Math";
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupName);

        when(groupDao.findByName(groupName)).thenReturn(Optional.of(Group.builder().withName(groupName).build()));

        assertThatThrownBy(() -> groupService.create(groupRequest)).hasMessage("Group with same name already exist");

        verify(groupDao).findByName(groupName);
    }

    @Test
    void findByIdShouldReturnGroupResponseIfArgumentIsGroupId() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(1L).build()));

        groupService.findById(groupId);

        verify(groupDao).findById(groupId);
    }

    @Test
    void findByIdShouldThrowExceptionIfGroupNotExist() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.findById(groupId)).hasMessage("There no group with id: 1");

        verify(groupDao).findById(groupId);
    }

    @Test
    void findByFormOfEducationIdShouldReturnListOfGroupResponseIfArgumentIsFormOfEducationId() {
        long formOfEducationId = 2;
        Group group1 = Group.builder().withId(1L).build();
        Group group2 = Group.builder().withId(2L).build();
        List<Group> formOfEducationGroups = Arrays.asList(group1, group2);

        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(1L).build()));
        when(groupDao.findByFormOfEducation(formOfEducationId)).thenReturn(formOfEducationGroups);

        groupService.findByFormOfEducation(formOfEducationId);

        verify(groupDao).findByFormOfEducation(formOfEducationId);
        verify(formOfEducationDao).findById(formOfEducationId);
    }

    @Test
    void findByDepartmentIdShouldReturnListOfGroupResponseIfArgumentIsFormOfEducationId() {
        long departmentId = 2;
        Group group1 = Group.builder().withId(1L).build();
        Group group2 = Group.builder().withId(2L).build();
        List<Group> formOfEducationGroups = Arrays.asList(group1, group2);

        when(departmentDao.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(2L).build()));
        when(groupDao.findByDepartmentId(departmentId)).thenReturn(formOfEducationGroups);

        groupService.findByDepartmentId(departmentId);

        verify(departmentDao).findById(departmentId);
        verify(groupDao).findByDepartmentId(departmentId);
    }

    @Test
    void findAllIdShouldReturnListOfGroupResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(groupDao.count()).thenReturn(11L);
        when(groupDao.findAll(2, 5)).thenReturn(Arrays.asList(Group.builder().withId(1L).build()));

        groupService.findAll(pageNumber);

        verify(groupDao).count();
        verify(groupDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfGroupResponseNoArguments() {
        when(groupDao.findAll()).thenReturn(Arrays.asList(Group.builder().withId(1L).build()));

        groupService.findAll();

        verify(groupDao).findAll();
    }

    @Test
    void editShouldEditDataOfGroupIfArgumentNewGroupRequest() {
        Group group = Group.builder().withId(1L).build();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);

        when(groupRequestMapper.mapDtoToEntity(groupRequest)).thenReturn(group);
        doNothing().when(groupDao).update(group);

        groupService.edit(groupRequest);

        verify(groupRequestMapper).mapDtoToEntity(groupRequest);
        verify(groupDao).update(group);
    }

    @Test
    void deleteShouldDeleteDataOfGroupIfArgumentIsGroupId() {
        long groupId = 1;
        Student student1 = Student.builder().withId(1L).build();
        Student student2 = Student.builder().withId(2L).build();
        List<Student> groupsStudents = Arrays.asList(student1, student2);
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> groupLessons = Arrays.asList(lesson1, lesson2);

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(groupDao.deleteById(groupId)).thenReturn(true);
        when(studentDao.findByGroupId(1L)).thenReturn(groupsStudents);
        doNothing().when(studentDao).leaveGroup(1L);
        doNothing().when(studentDao).leaveGroup(2L);
        when(lessonDao.findByGroupId(1L)).thenReturn(groupLessons);
        doNothing().when(lessonDao).removeGroupFromLesson(1L);
        doNothing().when(lessonDao).removeGroupFromLesson(2L);

        groupService.deleteById(groupId);

        verify(groupDao).findById(groupId);
        verify(groupDao).deleteById(groupId);
        verify(studentDao).findByGroupId(groupId);
        verify(studentDao).leaveGroup(1L);
        verify(studentDao).leaveGroup(2L);
        verify(lessonDao).findByGroupId(groupId);
        verify(lessonDao).removeGroupFromLesson(1L);
        verify(lessonDao).removeGroupFromLesson(2L);
    }

    @Test
    void deleteShouldDoNothingIfArgumentGroupDontExist() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        groupService.deleteById(groupId);

        verify(groupDao).findById(groupId);
    }

}
