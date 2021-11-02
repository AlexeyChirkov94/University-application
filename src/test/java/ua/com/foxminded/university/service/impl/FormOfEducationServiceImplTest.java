package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationResponseMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class FormOfEducationServiceImplTest {

    @Mock
    FormOfEducationDao formOfEducationDao;

    @Mock
    GroupDao groupDao;

    @Mock
    FormOfEducationRequestMapper formOfEducationRequestMapper;

    @Mock
    FormOfEducationResponseMapper formOfEducationResponseMapper;

    @InjectMocks
    FormOfEducationServiceImpl formOfEducationService;

    @Test
    void registerShouldAddFormOfEducationToDBIfArgumentsIsFormOfEducationRequest() {
        String formOfEducationName= "FormOfEducation of Math";
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setName(formOfEducationName);

        when(formOfEducationDao.findByName(formOfEducationName)).thenReturn(Optional.empty());

        formOfEducationService.create(formOfEducationRequest);

        verify(formOfEducationDao).findByName(formOfEducationName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfFormOfEducationWithSameNameAlreadyExist() {
        String formOfEducationName= "FormOfEducation of Math";
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setName(formOfEducationName);

        when(formOfEducationDao.findByName(formOfEducationName)).thenReturn(Optional.of(FormOfEducation.builder()
                .withName(formOfEducationName).build()));

        assertThatThrownBy(() -> formOfEducationService.create(formOfEducationRequest)).hasMessage("FormOfEducation with same name already exist");

        verify(formOfEducationDao).findByName(formOfEducationName);
    }

    @Test
    void findByIdShouldReturnOptionalOfFormOfEducationResponseIfArgumentIsFormOfEducationId() {
        long formOfEducationId = 1;

        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(1L).build()));

        formOfEducationService.findById(formOfEducationId);

        verify(formOfEducationDao).findById(formOfEducationId);
    }

    @Test
    void findAllIdShouldReturnListOfFormOfEducationResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(formOfEducationDao.count()).thenReturn(11L);
        when(formOfEducationDao.findAll(2, 5)).thenReturn(Arrays.asList(FormOfEducation.builder().withId(1L).build()));

        formOfEducationService.findAll(pageNumber);

        verify(formOfEducationDao).count();
        verify(formOfEducationDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfFormOfEducationResponseNoArguments() {
        when(formOfEducationDao.findAll()).thenReturn(Arrays.asList(FormOfEducation.builder().withId(1L).build()));

        formOfEducationService.findAll();

        verify(formOfEducationDao).findAll();
    }

    @Test
    void editShouldEditDataOfFormOfEducationIfArgumentNewFormOfEducationRequest() {
        FormOfEducation formOfEducation = FormOfEducation.builder().withId(1L).build();
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(1L);

        when(formOfEducationRequestMapper.mapDtoToEntity(formOfEducationRequest)).thenReturn(formOfEducation);
        doNothing().when(formOfEducationDao).update(formOfEducation);

        formOfEducationService.edit(formOfEducationRequest);

        verify(formOfEducationRequestMapper).mapDtoToEntity(formOfEducationRequest);
        verify(formOfEducationDao).update(formOfEducation);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfEducationIfArgumentIsFormOfEducationId() {
        long formOfEducationId = 1;
        Group group1 = Group.builder().withId(1L).build();
        Group group2 = Group.builder().withId(2L).build();
        List<Group> formOfEducationGroups = Arrays.asList(group1, group2);

        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(formOfEducationId).build()));
        when(groupDao.findByFormOfEducation(formOfEducationId)).thenReturn(formOfEducationGroups);
        doNothing().when(groupDao).removeFormOfEducationFromGroup(1L);
        doNothing().when(groupDao).removeFormOfEducationFromGroup(2L);
        when(formOfEducationDao.deleteById(formOfEducationId)).thenReturn(true);

        formOfEducationService.deleteById(formOfEducationId);

        verify(formOfEducationDao).findById(formOfEducationId);
        verify(groupDao).findByFormOfEducation(formOfEducationId);
        verify(groupDao).removeFormOfEducationFromGroup(1L);
        verify(groupDao).removeFormOfEducationFromGroup(2L);
        verify(formOfEducationDao).deleteById(formOfEducationId);
        verifyNoMoreInteractions(groupDao);
        verifyNoMoreInteractions(formOfEducationDao);
    }

    @Test
    void deleteShouldDoNothingIfArgumentFormOfEducationDontExist() {
        long formOfEducationId = 1;

        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.empty());

        formOfEducationService.deleteById(formOfEducationId);

        verify(formOfEducationDao).findById(formOfEducationId);
    }

}
