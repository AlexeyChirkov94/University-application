package ua.com.foxminded.university.service.impl;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.com.foxminded.university.dao.FormOfLessonDao;
import ua.com.foxminded.university.dao.LessonDao;
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.entity.FormOfLesson;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.mapper.FormOfLessonMapper;

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
class FormOfLessonServiceImplTest {

    @Mock
    FormOfLessonDao formOfLessonDao;

    @Mock
    LessonDao lessonDao;

    @Mock
    FormOfLessonMapper formOfLessonMapper;

    @InjectMocks
    FormOfLessonServiceImpl formOfLessonService;

    @Test
    void registerShouldAddFormOfLessonToDBIfArgumentsIsFormOfLessonRequest() {
        String formOfLessonName= "FormOfLesson of Math";
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setName(formOfLessonName);

        when(formOfLessonDao.findByName(formOfLessonName)).thenReturn(Collections.emptyList());

        formOfLessonService.create(formOfLessonRequest);

        verify(formOfLessonDao).findByName(formOfLessonName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfFormOfLessonWithSameNameAlreadyExist() {
        String formOfLessonName= "FormOfLesson of Math";
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setName(formOfLessonName);

        when(formOfLessonDao.findByName(formOfLessonName)).thenReturn(Arrays.asList(FormOfLesson.builder().withName(formOfLessonName).build()));

        assertThatThrownBy(() -> formOfLessonService.create(formOfLessonRequest)).hasMessage("Form of lesson with same name already exist");

        verify(formOfLessonDao).findByName(formOfLessonName);
    }

    @Test
    void findByIdShouldReturnFormOfLessonResponseIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;

        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.of(FormOfLesson.builder().withId(1L).build()));

        formOfLessonService.findById(formOfLessonId);

        verify(formOfLessonDao).findById(formOfLessonId);
    }

    @Test
    void findByIdShouldThrowExceptionIfFormOfLessonNotExistIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;

        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> formOfLessonService.findById(formOfLessonId)).hasMessage("There no form of lesson with id: 1");

        verify(formOfLessonDao).findById(formOfLessonId);
    }

    @Test
    void findAllIdShouldReturnListOfFormOfLessonResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(formOfLessonDao.count()).thenReturn(11L);
        when(formOfLessonDao.findAll(2, 5)).thenReturn(Arrays.asList(FormOfLesson.builder().withId(1L).build()));

        formOfLessonService.findAll(pageNumber);

        verify(formOfLessonDao).count();
        verify(formOfLessonDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfFormOfLessonResponseNoArguments() {
        when(formOfLessonDao.findAll()).thenReturn(Arrays.asList(FormOfLesson.builder().withId(1L).build()));

        formOfLessonService.findAll();

        verify(formOfLessonDao).findAll();
    }

    @Test
    void editShouldEditDataOfFormOfLessonIfArgumentNewFormOfLessonRequest() {
        FormOfLesson formOfLesson = FormOfLesson.builder().withId(1L).build();
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId(1L);

        when(formOfLessonMapper.mapDtoToEntity(formOfLessonRequest)).thenReturn(formOfLesson);
        doNothing().when(formOfLessonDao).update(formOfLesson);

        formOfLessonService.edit(formOfLessonRequest);

        verify(formOfLessonMapper).mapDtoToEntity(formOfLessonRequest);
        verify(formOfLessonDao).update(formOfLesson);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfLessonIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> formOfLessonLessons = Arrays.asList(lesson1, lesson2);

        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.of(FormOfLesson.builder().withId(formOfLessonId).build()));
        when(lessonDao.findByFormOfLessonId(1L)).thenReturn(formOfLessonLessons);
        doNothing().when(lessonDao).removeFormOfLessonFromLesson(1L);
        doNothing().when(lessonDao).removeFormOfLessonFromLesson(2L);
        doNothing().when(formOfLessonDao).deleteById(formOfLessonId);

        formOfLessonService.deleteById(formOfLessonId);

        verify(formOfLessonDao).findById(formOfLessonId);
        verify(lessonDao).findByFormOfLessonId(1L);
        verify(lessonDao).removeFormOfLessonFromLesson(1L);
        verify(lessonDao).removeFormOfLessonFromLesson(2L);
        verify(formOfLessonDao).deleteById(formOfLessonId);
        verifyNoMoreInteractions(formOfLessonDao);
        verifyNoMoreInteractions(lessonDao);
    }

    @Test
    void deleteShouldDoNothingIfArgumentFormOfLessonDontExist() {
        long formOfLessonId = 1;

        when(formOfLessonDao.findById(formOfLessonId)).thenReturn(Optional.empty());

        formOfLessonService.deleteById(formOfLessonId);

        verify(formOfLessonDao).findById(formOfLessonId);
    }

}
