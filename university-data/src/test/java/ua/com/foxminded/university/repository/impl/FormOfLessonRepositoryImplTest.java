package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.FormOfLessonRepository;
import ua.com.foxminded.university.entity.FormOfLesson;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertFormsOfLesson;

class FormOfLessonRepositoryImplTest {

    ApplicationContext context;
    FormOfLessonRepository formOfLessonRepository;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        formOfLessonRepository = context.getBean(FormOfLessonRepository.class);
    }

    @Test
    void createAndReadShouldAddNewFormOfLessonToDatabaseIfArgumentIsFormOfLesson(){
        FormOfLesson addingFormOfLesson = FormOfLesson.builder().withName("new Form").withDuration(100).build();
        formOfLessonRepository.save(addingFormOfLesson);
        FormOfLesson readingFormOfLesson = formOfLessonRepository.findById(4L).get();

        assertFormsOfLesson(readingFormOfLesson, addingFormOfLesson);
    }

    @Test
    void createAndReadShouldAddListOfNewFormOfLessonsToDatabaseIfArgumentIsListOfFormOfLessons(){
        List<FormOfLesson> addingFormOfLessonEntities = Arrays.asList (FormOfLesson.builder().withName("new Form")
                .withDuration(100).build());
        formOfLessonRepository.saveAll(addingFormOfLessonEntities);
        List<FormOfLesson> readingFormOfLessonEntities = Arrays.asList(formOfLessonRepository.findById(4L).get());

        assertFormsOfLesson(readingFormOfLessonEntities, addingFormOfLessonEntities);
    }

    @Test
    void updateShouldUpdateDataOfFormOfLessonIfArgumentIsFormOfLesson(){
        FormOfLesson expected = FormOfLesson.builder()
                .withId(1L)
                .withName("new Form")
                .withDuration(100)
                .build();

        formOfLessonRepository.save(expected);
        FormOfLesson actual = formOfLessonRepository.findById(1L).get();

        assertFormsOfLesson(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfFormOfLessonsIfArgumentIsListOfFormOfLessons() {
        List<FormOfLesson> expected = Arrays.asList(FormOfLesson.builder()
                .withId(1L)
                .withName("new Form")
                .withDuration(100)
                .build());
        formOfLessonRepository.saveAll(expected);
        List<FormOfLesson> actual = Arrays.asList(formOfLessonRepository.findById(1L).get());

        assertFormsOfLesson(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfLessonIfArgumentIsIdOfFormOfLesson(){
        Optional<FormOfLesson> expected = Optional.empty();
        formOfLessonRepository.deleteById(3L);
        Optional<FormOfLesson> actual = formOfLessonRepository.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        FormOfLesson expected = FormOfLesson.builder()
                .withName("lecture")
                .withDuration(60)
                .build();
        FormOfLesson actual = formOfLessonRepository.findAllByName("lecture").get(0);

        assertFormsOfLesson(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<FormOfLesson> expected = Collections.emptyList();
        List<FormOfLesson> actual = formOfLessonRepository.findAllByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
