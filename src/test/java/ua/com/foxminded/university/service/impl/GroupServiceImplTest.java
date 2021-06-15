package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.interfaces.GroupRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;
import java.util.Arrays;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    private GroupDao groupDao;

    @Mock
    private GroupRequestMapper groupRequestMapper;

    @Mock
    private GroupResponseMapper groupResponseMapper;

    @Mock
    private FormOfEducationDao formOfEducationDao;

    @InjectMocks
    private GroupServiceImpl groupService;


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
    void changeFormOfEducationShouldThrowExceptionIfGroupDontExist() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeFormOfEducation(groupId, formOfEducationId)).hasMessage("There no group or FormOfEducation with this ids");

        verify(groupDao).findById(groupId);
    }

    @Test
    void changeFormOfEducationShouldThrowExceptionIfProfessorDontExist() {
        long groupId = 1;
        long formOfEducationId = 2;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> groupService.changeFormOfEducation(groupId, formOfEducationId)).hasMessage("There no group or FormOfEducation with this ids");

        verify(groupDao).findById(groupId);
        verify(formOfEducationDao).findById(formOfEducationId);
    }

    @Test
    void registerShouldAddGroupToDBIfArgumentsIsGroupRequest() {
        String groupName= "Group of Math";
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupName);

        when(groupDao.findByName(groupName)).thenReturn(Optional.empty());

        groupService.register(groupRequest);

        verify(groupDao).findByName(groupName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfGroupWithSameNameAlreadyExist() {
        String groupName= "Group of Math";
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupName);

        when(groupDao.findByName(groupName)).thenReturn(Optional.of(Group.builder().withName(groupName).build()));

        assertThatThrownBy(() -> groupService.register(groupRequest)).hasMessage("Group with same name already exist");

        verify(groupDao).findByName(groupName);
    }

    @Test
    void findByIdShouldReturnOptionalOfGroupResponseIfArgumentIsGroupId() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId((long)1).build()));

        groupService.findById(groupId);

        verify(groupDao).findById(groupId);
    }

    @Test
    void findAllIdShouldReturnListOfGroupResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(groupDao.count()).thenReturn((long)11);
        when(groupDao.findAll(2, 5)).thenReturn(Arrays.asList(Group.builder().withId((long)1).build()));

        groupService.findAll(pageNumber);

        verify(groupDao).count();
        verify(groupDao).findAll(2, 5);
    }

    @Test
    void editShouldEditDataOfGroupIfArgumentNewGroupRequest() {
        Group group = Group.builder().withId((long)1).build();
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId((long)1);

        when(groupRequestMapper.mapDtoToEntity(groupRequest)).thenReturn(group);
        doNothing().when(groupDao).update(group);

        groupService.edit(groupRequest);

        verify(groupRequestMapper).mapDtoToEntity(groupRequest);
        verify(groupDao).update(group);
    }

    @Test
    void deleteShouldDeleteDataOfGroupIfArgumentIsGroupId() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.of(Group.builder().withId(groupId).build()));
        when(groupDao.deleteById(groupId)).thenReturn(true);

        groupService.deleteById(groupId);

        verify(groupDao).findById(groupId);
        verify(groupDao).deleteById(groupId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentGroupDontExist() {
        long groupId = 1;

        when(groupDao.findById(groupId)).thenReturn(Optional.empty());

        groupService.deleteById(groupId);

        verify(groupDao).findById(groupId);
    }

}
