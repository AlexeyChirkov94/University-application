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
import ua.com.foxminded.university.repository.FormOfLessonRepository;
import ua.com.foxminded.university.repository.LessonRepository;
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
    FormOfLessonRepository formOfLessonRepository;

    @Mock
    LessonRepository lessonRepository;

    @Mock
    FormOfLessonMapper formOfLessonMapper;

    @InjectMocks
    FormOfLessonServiceImpl formOfLessonService;

    @Test
    void registerShouldAddFormOfLessonToDBIfArgumentsIsFormOfLessonRequest() {
        String formOfLessonName= "FormOfLesson of Math";
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setName(formOfLessonName);

        when(formOfLessonRepository.findAllByName(formOfLessonName)).thenReturn(Collections.emptyList());

        formOfLessonService.create(formOfLessonRequest);

        verify(formOfLessonRepository).findAllByName(formOfLessonName);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfFormOfLessonWithSameNameAlreadyExist() {
        String formOfLessonName= "FormOfLesson of Math";
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setName(formOfLessonName);

        when(formOfLessonRepository.findAllByName(formOfLessonName)).thenReturn(Arrays.asList(FormOfLesson.builder().withName(formOfLessonName).build()));

        assertThatThrownBy(() -> formOfLessonService.create(formOfLessonRequest)).hasMessage("Form of lesson with same name already exist");

        verify(formOfLessonRepository).findAllByName(formOfLessonName);
    }

    @Test
    void findByIdShouldReturnFormOfLessonResponseIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;

        when(formOfLessonRepository.findById(formOfLessonId)).thenReturn(Optional.of(FormOfLesson.builder().withId(1L).build()));

        formOfLessonService.findById(formOfLessonId);

        verify(formOfLessonRepository).findById(formOfLessonId);
    }

    @Test
    void findByIdShouldThrowExceptionIfFormOfLessonNotExistIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;

        when(formOfLessonRepository.findById(formOfLessonId)).thenReturn(Optional.empty());

        AssertionsForClassTypes.assertThatThrownBy(() -> formOfLessonService.findById(formOfLessonId)).hasMessage("There no form of lesson with id: 1");

        verify(formOfLessonRepository).findById(formOfLessonId);
    }

    @Test
    void findAllIdShouldReturnListOfFormOfLessonResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(formOfLessonRepository.count()).thenReturn(11L);
        when(formOfLessonRepository.findAll(PageRequest.of(1, 5, Sort.by("id")))).thenReturn(new PageImpl(Collections.singletonList(FormOfLesson.builder().withId(1L).build())));

        formOfLessonService.findAll(pageNumber);

        verify(formOfLessonRepository).count();
        verify(formOfLessonRepository).findAll(PageRequest.of(1, 5, Sort.by("id")));
    }

    @Test
    void findAllIdShouldReturnListOfFormOfLessonResponseNoArguments() {
        when(formOfLessonRepository.findAll(Sort.by("id"))).thenReturn(Arrays.asList(FormOfLesson.builder().withId(1L).build()));

        formOfLessonService.findAll();

        verify(formOfLessonRepository).findAll(Sort.by("id"));
    }

    @Test
    void editShouldEditDataOfFormOfLessonIfArgumentNewFormOfLessonRequest() {
        FormOfLesson formOfLesson = FormOfLesson.builder().withId(1L).build();
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId(1L);

        when(formOfLessonMapper.mapDtoToEntity(formOfLessonRequest)).thenReturn(formOfLesson);
        when(formOfLessonRepository.save(formOfLesson)).thenReturn(formOfLesson);

        formOfLessonService.edit(formOfLessonRequest);

        verify(formOfLessonMapper).mapDtoToEntity(formOfLessonRequest);
        verify(formOfLessonRepository).save(formOfLesson);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfLessonIfArgumentIsFormOfLessonId() {
        long formOfLessonId = 1;
        Lesson lesson1 = Lesson.builder().withId(1L).build();
        Lesson lesson2 = Lesson.builder().withId(2L).build();
        List<Lesson> formOfLessonLessons = Arrays.asList(lesson1, lesson2);

        when(formOfLessonRepository.findById(formOfLessonId)).thenReturn(Optional.of(FormOfLesson.builder().withId(formOfLessonId).build()));
        when(lessonRepository.findAllByFormOfLessonIdOrderByTimeOfStartLesson(1L)).thenReturn(formOfLessonLessons);
        doNothing().when(lessonRepository).removeFormOfLessonFromLesson(1L);
        doNothing().when(lessonRepository).removeFormOfLessonFromLesson(2L);
        doNothing().when(formOfLessonRepository).deleteById(formOfLessonId);

        formOfLessonService.deleteById(formOfLessonId);

        verify(formOfLessonRepository).findById(formOfLessonId);
        verify(lessonRepository).findAllByFormOfLessonIdOrderByTimeOfStartLesson(1L);
        verify(lessonRepository).removeFormOfLessonFromLesson(1L);
        verify(lessonRepository).removeFormOfLessonFromLesson(2L);
        verify(formOfLessonRepository).deleteById(formOfLessonId);
        verifyNoMoreInteractions(formOfLessonRepository);
        verifyNoMoreInteractions(lessonRepository);
    }

    @Test
    void deleteShouldDoNothingIfArgumentFormOfLessonDontExist() {
        long formOfLessonId = 1;

        when(formOfLessonRepository.findById(formOfLessonId)).thenReturn(Optional.empty());

        formOfLessonService.deleteById(formOfLessonId);

        verify(formOfLessonRepository).findById(formOfLessonId);
    }

}
