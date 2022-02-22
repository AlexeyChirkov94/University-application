package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ua.com.foxminded.university.repository.DepartmentRepository;
import ua.com.foxminded.university.repository.FormOfEducationRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.entity.Department;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Student;
import ua.com.foxminded.university.mapper.GroupMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupRepository groupRepository;

    @Mock
    StudentRepository studentRepository;

    @Mock
    LessonRepository lessonRepository;

    @Mock
    DepartmentRepository departmentRepository;

    @Mock
    GroupMapper groupMapper;

    @Mock
    FormOfEducationRepository formOfEducationRepository;

    @InjectMocks
    GroupServiceImpl groupService;


    @Test
    void changeFormOfEducationShouldChangeFormOfEducationIfArgumentsIsGroupIdAndFormOfEducationId() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(formOfEducationRepository.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(formOfEducationId).build()));
        doNothing().when(groupRepository).changeFormOfEducation(groupId, formOfEducationId);

        groupService.changeFormOfEducation(groupId, formOfEducationId);

        verify(groupRepository).findById(groupId);
        verify(formOfEducationRepository).findById(formOfEducationId);
        verify(groupRepository).changeFormOfEducation(groupId, formOfEducationId);
    }

    @Test
    void removeFormOfEducationFromGroupShouldRemoveFormOfEducationIfArgumentsIsGroupId() {
        long groupId = 1;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        doNothing().when(groupRepository).removeFormOfEducationFromGroup(groupId);

        groupService.removeFormOfEducationFromGroup(groupId);

        verify(groupRepository).findById(groupId);
        verify(groupRepository).removeFormOfEducationFromGroup(groupId);
    }

    @Test
    void changeFormOfEducationShouldThrowExceptionIfGroupDontExist() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeFormOfEducation(groupId, formOfEducationId)).hasMessage("There no group with this id:1");

        verify(groupRepository).findById(groupId);
    }

    @Test
    void changeFormOfEducationShouldThrowExceptionIfProfessorDontExist() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(formOfEducationRepository.findById(formOfEducationId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeFormOfEducation(groupId, formOfEducationId)).hasMessage("There no formOfEducation with this id:2");

        verify(groupRepository).findById(groupId);
        verify(formOfEducationRepository).findById(formOfEducationId);
    }

    @Test
    void changeDepartmentShouldChangeDepartmentIfArgumentsIsGroupIdAndDepartmentId() {
        long groupId = 1;
        long departmentId = 1;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(departmentId).build()));
        doNothing().when(groupRepository).changeDepartment(groupId, departmentId);

        groupService.changeDepartment(groupId, departmentId);

        verify(groupRepository).findById(groupId);
        verify(departmentRepository).findById(departmentId);
        verify(groupRepository).changeDepartment(groupId, departmentId);
    }

    @Test
    void removeDepartmentFromGroupShouldRemoveDepartmentIfArgumentsIsGroupId() {
        long groupId = 1;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        doNothing().when(groupRepository).removeDepartmentFromGroup(groupId);

        groupService.removeDepartmentFromGroup(groupId);

        verify(groupRepository).findById(groupId);
        verify(groupRepository).removeDepartmentFromGroup(groupId);
    }

    @Test
    void changeDepartmentShouldThrowExceptionIfGroupDontExist() {
        long groupId = 1;
        long departmentId = 2;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeDepartment(groupId, departmentId)).hasMessage("There no group with this id:1");

        verify(groupRepository).findById(groupId);
    }

    @Test
    void changeDepartmentShouldThrowExceptionIfDepartmentDontExist() {
        long groupId = 1;
        long departmentId = 2;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeDepartment(groupId, departmentId)).hasMessage("There no department with this id:2");

        verify(groupRepository).findById(groupId);
        verify(departmentRepository).findById(departmentId);
    }

    @Test
    void createShouldAddGroupToDBIfArgumentsIsGroupRequestWithDepartmentAndFormOfEducation() {
        String groupName= "Group of Math";
        Department department = Department.builder().withId(1L).withName("Dep").build();
        FormOfEducation formOfEducation = FormOfEducation.builder().withId(1L).withName("full-time").build();
        Group group = Group.builder().withName(groupName).withId(1L).build();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupName);
        groupRequest.setDepartmentId(1L);
        groupRequest.setFormOfEducationId(1L);

        when(groupRepository.findAllByName(groupName)).thenReturn(Collections.emptyList());
        when(groupMapper.mapDtoToEntity(groupRequest)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(groupRepository).changeDepartment(1L, 1L);
        when(formOfEducationRepository.findById(1L)).thenReturn(Optional.of(formOfEducation));
        doNothing().when(groupRepository).changeFormOfEducation(1L, 1L);

        groupService.create(groupRequest);

        verify(groupRepository).findAllByName(groupName);
        verify(groupMapper).mapDtoToEntity(groupRequest);
        verify(groupRepository).save(group);
        verify(groupRepository, times(2)).findById(1L);
        verify(departmentRepository).findById(1L);
        verify(groupRepository).changeDepartment(1L, 1L);
        verify(formOfEducationRepository).findById(1L);
        verify(groupRepository).findAllByName(groupName);
    }

    @Test
    void createShouldAddGroupToDBIfArgumentsIsGroupRequestWithoutDepartmentAndFormOfEducation() {
        String groupName= "Group of Math";
        Group group = Group.builder().withName(groupName).withId(1L).build();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupName);
        groupRequest.setDepartmentId(0L);
        groupRequest.setFormOfEducationId(0L);

        when(groupRepository.findAllByName(groupName)).thenReturn(Collections.emptyList());
        when(groupMapper.mapDtoToEntity(groupRequest)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);

        groupService.create(groupRequest);

        verify(groupRepository).findAllByName(groupName);
        verify(groupMapper).mapDtoToEntity(groupRequest);
        verify(groupRepository).save(group);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfGroupWithSameNameAlreadyExist() {
        String groupName= "Group of Math";
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupName);

        when(groupRepository.findAllByName(groupName)).thenReturn(Arrays.asList(Group.builder().withName(groupName).build()));

        assertThatThrownBy(() -> groupService.create(groupRequest)).hasMessage("Group with same name already exist");

        verify(groupRepository).findAllByName(groupName);
    }

    @Test
    void findByIdShouldReturnGroupResponseIfArgumentIsGroupId() {
        long groupId = 1;

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(1L).build()));

        groupService.findById(groupId);

        verify(groupRepository).findById(groupId);
    }

    @Test
    void findByIdShouldThrowExceptionIfGroupNotExist() {
        long groupId = 1;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.findById(groupId)).hasMessage("There no group with id: 1");

        verify(groupRepository).findById(groupId);
    }

    @Test
    void findByFormOfEducationIdShouldReturnListOfGroupResponseIfArgumentIsFormOfEducationId() {
        long formOfEducationId = 2;
        Group group1 = Group.builder().withId(1L).build();
        Group group2 = Group.builder().withId(2L).build();
        List<Group> formOfEducationGroups = Arrays.asList(group1, group2);

        when(formOfEducationRepository.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(1L).build()));
        when(groupRepository.findAllByFormOfEducationIdOrderById(formOfEducationId)).thenReturn(formOfEducationGroups);

        groupService.findByFormOfEducation(formOfEducationId);

        verify(groupRepository).findAllByFormOfEducationIdOrderById(formOfEducationId);
        verify(formOfEducationRepository).findById(formOfEducationId);
    }

    @Test
    void findByDepartmentIdShouldReturnListOfGroupResponseIfArgumentIsFormOfEducationId() {
        long departmentId = 2;
        Group group1 = Group.builder().withId(1L).build();
        Group group2 = Group.builder().withId(2L).build();
        List<Group> formOfEducationGroups = Arrays.asList(group1, group2);

        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(Department.builder().withId(2L).build()));
        when(groupRepository.findAllByDepartmentIdOrderById(departmentId)).thenReturn(formOfEducationGroups);

        groupService.findByDepartmentId(departmentId);

        verify(departmentRepository).findById(departmentId);
        verify(groupRepository).findAllByDepartmentIdOrderById(departmentId);
    }

    @Test
    void findAllIdShouldReturnListOfGroupResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(groupRepository.count()).thenReturn(11L);
        when(groupRepository.findAll(PageRequest.of(1, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(Group.builder().withId(1L).build())));

        groupService.findAll(pageNumber);

        verify(groupRepository).count();
        verify(groupRepository).findAll(PageRequest.of(1, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfGroupResponseNoArguments() {
        when(groupRepository.findAll(Sort.by("id"))).thenReturn(Arrays.asList(Group.builder().withId(1L).build()));

        groupService.findAll();

        verify(groupRepository).findAll(Sort.by("id"));
    }

    @Test
    void editShouldEditDataOfGroupIfArgumentNewGroupRequestWithDepartmentAndFormOfEducation() {
        String groupName= "Group of Math";
        Department department = Department.builder().withId(1L).withName("Dep").build();
        FormOfEducation formOfEducation = FormOfEducation.builder().withId(1L).withName("full-time").build();
        Group group = Group.builder().withName(groupName).withId(1L).build();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName(groupName);
        groupRequest.setDepartmentId(1L);
        groupRequest.setFormOfEducationId(1L);

        when(groupMapper.mapDtoToEntity(groupRequest)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        doNothing().when(groupRepository).changeDepartment(1L, 1L);
        when(formOfEducationRepository.findById(1L)).thenReturn(Optional.of(formOfEducation));
        doNothing().when(groupRepository).changeFormOfEducation(1L, 1L);

        groupService.edit(groupRequest);

        verify(groupMapper).mapDtoToEntity(groupRequest);
        verify(groupRepository).save(group);
        verify(groupRepository,times(2)).findById(1L);
        verify(departmentRepository).findById(1L);
        verify(groupRepository).changeDepartment(1L, 1L);
        verify(formOfEducationRepository).findById(1L);
        verify(groupRepository).changeFormOfEducation(1L, 1L);
    }

    @Test
    void editShouldEditDataOfGroupIfArgumentNewGroupRequestWithoutDepartmentAndFormOfEducation() {
        Group group = Group.builder().withId(1L).build();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setDepartmentId(0L);
        groupRequest.setFormOfEducationId(0L);

        when(groupMapper.mapDtoToEntity(groupRequest)).thenReturn(group);
        when(groupRepository.save(group)).thenReturn(group);

        groupService.edit(groupRequest);

        verify(groupMapper).mapDtoToEntity(groupRequest);
        verify(groupRepository).save(group);
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

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        doNothing().when(groupRepository).deleteById(groupId);
        when(studentRepository.findAllByGroupId(1L)).thenReturn(groupsStudents);
        doNothing().when(studentRepository).leaveGroup(1L);
        doNothing().when(studentRepository).leaveGroup(2L);
        when(lessonRepository.findAllByGroupIdOrderByTimeOfStartLesson(1L)).thenReturn(groupLessons);
        doNothing().when(lessonRepository).removeGroupFromLesson(1L);
        doNothing().when(lessonRepository).removeGroupFromLesson(2L);

        groupService.deleteById(groupId);

        verify(groupRepository).findById(groupId);
        verify(groupRepository).deleteById(groupId);
        verify(studentRepository).findAllByGroupId(groupId);
        verify(studentRepository).leaveGroup(1L);
        verify(studentRepository).leaveGroup(2L);
        verify(lessonRepository).findAllByGroupIdOrderByTimeOfStartLesson(groupId);
        verify(lessonRepository).removeGroupFromLesson(1L);
        verify(lessonRepository).removeGroupFromLesson(2L);
    }

    @Test
    void deleteShouldDoNothingIfArgumentGroupDontExist() {
        long groupId = 1;

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());

        groupService.deleteById(groupId);

        verify(groupRepository).findById(groupId);
    }

}
