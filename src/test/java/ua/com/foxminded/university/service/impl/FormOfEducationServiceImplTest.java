package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.FormOfEducationDao;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.entity.FormOfEducation;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationResponseMapper;
import java.util.Arrays;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class FormOfEducationServiceImplTest {

    @Mock
    private FormOfEducationDao formOfEducationDao;

    @Mock
    private FormOfEducationRequestMapper formOfEducationRequestMapper;

    @Mock
    private FormOfEducationResponseMapper formOfEducationResponseMapper;

    @InjectMocks
    private FormOfEducationServiceImpl formOfEducationService;

    @Test
    void registerShouldAddFormOfEducationToDBIfArgumentsIsFormOfEducationRequest() {
        String formOfEducationName= "FormOfEducation of Math";
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setName(formOfEducationName);

        when(formOfEducationDao.findByName(formOfEducationName)).thenReturn(Optional.empty());

        formOfEducationService.register(formOfEducationRequest);

        verify(formOfEducationDao).findByName(formOfEducationName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfFormOfEducationWithSameNameAlreadyExist() {
        String formOfEducationName= "FormOfEducation of Math";
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setName(formOfEducationName);

        when(formOfEducationDao.findByName(formOfEducationName)).thenReturn(Optional.of(FormOfEducation.builder()
                .withName(formOfEducationName).build()));

        assertThatThrownBy(() -> formOfEducationService.register(formOfEducationRequest)).hasMessage("FormOfEducation with same name already exist");

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

        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.of(FormOfEducation.builder().withId(formOfEducationId).build()));
        when(formOfEducationDao.deleteById(formOfEducationId)).thenReturn(true);

        formOfEducationService.deleteById(formOfEducationId);

        verify(formOfEducationDao).findById(formOfEducationId);
        verify(formOfEducationDao).deleteById(formOfEducationId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentFormOfEducationDontExist() {
        long formOfEducationId = 1;

        when(formOfEducationDao.findById(formOfEducationId)).thenReturn(Optional.empty());

        formOfEducationService.deleteById(formOfEducationId);

        verify(formOfEducationDao).findById(formOfEducationId);
    }

}
