package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.interfaces.FormOfLessonDao;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfLessonResponseMapper;
import java.util.Arrays;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class FormOfLessonServiceImplTest {

    @Mock
    private FormOfLessonDao formOfLessonDao;

    @Mock
    private FormOfLessonRequestMapper formOfLessonRequestMapper;

    @Mock
    private FormOfLessonResponseMapper formOfLessonResponseMapper;

    @InjectMocks
    private FormOfLessonServiceImpl formOfLessonService;

    @Test
    void registerShouldAddFormOfLessonToDBIfArgumentsIsFormOfLessonRequest() {
        String formOfLessonName= "FormOfLesson of Math";
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setName(formOfLessonName);

        when(formOfLessonDao.findByName(formOfLessonName)).thenReturn(Optional.empty());

        formOfLessonService.register(formOfLessonRequest);

        verify(formOfLessonDao).findByName(formOfLessonName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfFormOfLessonWithSameNameAlreadyExist() {
        String formOfLessonName= "FormOfLesson of Math";
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setName(formOfLessonName);

        when(formOfLessonDao.findByName(formOfLessonName)).thenReturn(Optional.of(FormOfLesson.builder().withName(formOfLessonName).build()));

        assertThatThrownBy(() -> formOfLessonService.register(formOfLessonRequest)).hasMessage("FormOfLesson with same name already exist");

        verify(formOfLessonDao).findByName(formOfLessonName);
    }

    @Test
    void findByIdShouldReturnOptionalOfFormOfLessonResponseIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;

        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.of(FormOfLesson.builder().withId((long)1).build()));

        formOfLessonService.findById(formOfLessonId);

        verify(formOfLessonDao).findById(formOfLessonId);
    }

    @Test
    void findAllIdShouldReturnListOfFormOfLessonResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(formOfLessonDao.count()).thenReturn((long)11);
        when(formOfLessonDao.findAll(2, 5)).thenReturn(Arrays.asList(FormOfLesson.builder().withId((long)1).build()));

        formOfLessonService.findAll(pageNumber);

        verify(formOfLessonDao).count();
        verify(formOfLessonDao).findAll(2, 5);
    }

    @Test
    void editShouldEditDataOfFormOfLessonIfArgumentNewFormOfLessonRequest() {
        FormOfLesson formOfLesson = FormOfLesson.builder().withId((long)1).build();
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId((long)1);

        when(formOfLessonRequestMapper.mapDtoToEntity(formOfLessonRequest)).thenReturn(formOfLesson);
        doNothing().when(formOfLessonDao).update(formOfLesson);

        formOfLessonService.edit(formOfLessonRequest);

        verify(formOfLessonRequestMapper).mapDtoToEntity(formOfLessonRequest);
        verify(formOfLessonDao).update(formOfLesson);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfLessonIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;

        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.of(FormOfLesson.builder().withId(formOfLessonId).build()));
        when(formOfLessonDao.deleteById(formOfLessonId)).thenReturn(true);

        formOfLessonService.deleteById(formOfLessonId);

        verify(formOfLessonDao).findById(formOfLessonId);
        verify(formOfLessonDao).deleteById(formOfLessonId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentFormOfLessonDontExist() {
        long formOfLessonId = 1;

        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.empty());

        formOfLessonService.deleteById(formOfLessonId);

        verify(formOfLessonDao).findById(formOfLessonId);
    }

}
