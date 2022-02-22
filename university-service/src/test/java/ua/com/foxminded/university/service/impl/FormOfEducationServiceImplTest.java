package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ua.com.foxminded.university.repository.FormOfEducationRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.FormOfEducationMapper;

import java.util.Arrays;
import java.util.Collections;
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
    FormOfEducationRepository formOfEducationRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    FormOfEducationMapper formOfEducationMapper;

    @InjectMocks
    FormOfEducationServiceImpl formOfEducationService;

    @Test
    void registerShouldAddFormOfEducationToDBIfArgumentsIsFormOfEducationRequest() {
        String formOfEducationName= "FormOfEducation of Math";
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setName(formOfEducationName);

        when(formOfEducationRepository.findAllByName(formOfEducationName)).thenReturn(Collections.emptyList());

        formOfEducationService.create(formOfEducationRequest);

        verify(formOfEducationRepository).findAllByName(formOfEducationName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfFormOfEducationWithSameNameAlreadyExist() {
        String formOfEducationName= "FormOfEducation of Math";
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setName(formOfEducationName);

        when(formOfEducationRepository.findAllByName(formOfEducationName)).thenReturn(Arrays.asList(FormOfEducation.builder()
                .withName(formOfEducationName).build()));

        assertThatThrownBy(() -> formOfEducationService.create(formOfEducationRequest)).hasMessage("Form of education with same name already exist");

        verify(formOfEducationRepository).findAllByName(formOfEducationName);
    }

    @Test
    void findByIdShouldReturnFormOfEducationResponseIfArgumentIsFormOfEducationId() {
        long formOfEducationId = 1;

        when(formOfEducationRepository.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(1L).build()));

        formOfEducationService.findById(formOfEducationId);

        verify(formOfEducationRepository).findById(formOfEducationId);
    }

    @Test
    void findByIdShouldThrowExceptionIfFormOfEducationNotExistIfArgumentIsFormOfEducationId() {
        long formOfEducationId = 1;

        when(formOfEducationRepository.findById(formOfEducationId)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> formOfEducationService.findById(formOfEducationId)).hasMessage("There no form of education with id: 1");

        verify(formOfEducationRepository).findById(formOfEducationId);
    }

    @Test
    void findAllIdShouldReturnListOfFormOfEducationResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(formOfEducationRepository.count()).thenReturn(11L);
        when(formOfEducationRepository.findAll(PageRequest.of(1, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(FormOfEducation.builder().withId(1L).build())));

        formOfEducationService.findAll(pageNumber);

        verify(formOfEducationRepository).count();
        verify(formOfEducationRepository).findAll(PageRequest.of(1, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfFormOfEducationResponseNoArguments() {
        when(formOfEducationRepository.findAll(Sort.by("id"))).thenReturn(Arrays.asList(FormOfEducation.builder().withId(1L).build()));

        formOfEducationService.findAll();

        verify(formOfEducationRepository).findAll(Sort.by("id"));
    }

    @Test
    void editShouldEditDataOfFormOfEducationIfArgumentNewFormOfEducationRequest() {
        FormOfEducation formOfEducation = FormOfEducation.builder().withId(1L).build();
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(1L);

        when(formOfEducationMapper.mapDtoToEntity(formOfEducationRequest)).thenReturn(formOfEducation);
        when(formOfEducationRepository.save(formOfEducation)).thenReturn(formOfEducation);

        formOfEducationService.edit(formOfEducationRequest);

        verify(formOfEducationMapper).mapDtoToEntity(formOfEducationRequest);
        verify(formOfEducationRepository).save(formOfEducation);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfEducationIfArgumentIsFormOfEducationId() {
        long formOfEducationId = 1;
        Group group1 = Group.builder().withId(1L).build();
        Group group2 = Group.builder().withId(2L).build();
        List<Group> formOfEducationGroups = Arrays.asList(group1, group2);

        when(formOfEducationRepository.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(formOfEducationId).build()));
        when(groupRepository.findAllByFormOfEducationIdOrderById(formOfEducationId)).thenReturn(formOfEducationGroups);
        doNothing().when(groupRepository).removeFormOfEducationFromGroup(1L);
        doNothing().when(groupRepository).removeFormOfEducationFromGroup(2L);
        doNothing().when(formOfEducationRepository).deleteById(formOfEducationId);


        formOfEducationService.deleteById(formOfEducationId);

        verify(formOfEducationRepository).findById(formOfEducationId);
        verify(groupRepository).findAllByFormOfEducationIdOrderById(formOfEducationId);
        verify(groupRepository).removeFormOfEducationFromGroup(1L);
        verify(groupRepository).removeFormOfEducationFromGroup(2L);
        verify(formOfEducationRepository).deleteById(formOfEducationId);
        verifyNoMoreInteractions(groupRepository);
        verifyNoMoreInteractions(formOfEducationRepository);
    }

    @Test
    void deleteShouldDoNothingIfArgumentFormOfEducationDontExist() {
        long formOfEducationId = 1;

        when(formOfEducationRepository.findById(formOfEducationId)).thenReturn(Optional.empty());

        formOfEducationService.deleteById(formOfEducationId);

        verify(formOfEducationRepository).findById(formOfEducationId);
    }

}
