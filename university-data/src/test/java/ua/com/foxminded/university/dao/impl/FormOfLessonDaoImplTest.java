package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.dao.FormOfLessonDao;
import ua.com.foxminded.university.entity.FormOfLesson;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertFormsOfLesson;

class FormOfLessonDaoImplTest {

    ApplicationContext context;
    FormOfLessonDao formOfLessonDao;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        formOfLessonDao = context.getBean(FormOfLessonDaoImpl.class);
    }

    @Test
    void createAndReadShouldAddNewFormOfLessonToDatabaseIfArgumentIsFormOfLesson(){
        FormOfLesson addingFormOfLesson = FormOfLesson.builder().withName("new Form").withDuration(100).build();
        formOfLessonDao.save(addingFormOfLesson);
        FormOfLesson readingFormOfLesson = formOfLessonDao.findById(4L).get();

        assertFormsOfLesson(readingFormOfLesson, addingFormOfLesson);
    }

    @Test
    void createAndReadShouldAddListOfNewFormOfLessonsToDatabaseIfArgumentIsListOfFormOfLessons(){
        List<FormOfLesson> addingFormOfLessonEntities = Arrays.asList (FormOfLesson.builder().withName("new Form")
                .withDuration(100).build());
        formOfLessonDao.saveAll(addingFormOfLessonEntities);
        List<FormOfLesson> readingFormOfLessonEntities = Arrays.asList(formOfLessonDao.findById(4L).get());

        assertFormsOfLesson(readingFormOfLessonEntities, addingFormOfLessonEntities);
    }

    @Test
    void updateShouldUpdateDataOfFormOfLessonIfArgumentIsFormOfLesson(){
        FormOfLesson expected = FormOfLesson.builder()
                .withId(1L)
                .withName("new Form")
                .withDuration(100)
                .build();

        formOfLessonDao.update(expected);
        FormOfLesson actual = formOfLessonDao.findById(1L).get();

        assertFormsOfLesson(actual, expected);
    }

    @Test
    void updateAllShouldUpdateDataOfFormOfLessonsIfArgumentIsListOfFormOfLessons() {
        List<FormOfLesson> expected = Arrays.asList(FormOfLesson.builder()
                .withId(1L)
                .withName("new Form")
                .withDuration(100)
                .build());
        formOfLessonDao.updateAll(expected);
        List<FormOfLesson> actual = Arrays.asList(formOfLessonDao.findById(1L).get());

        assertFormsOfLesson(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfLessonIfArgumentIsIdOfFormOfLesson(){
        Optional<FormOfLesson> expected = Optional.empty();
        formOfLessonDao.deleteById(3L);
        Optional<FormOfLesson> actual = formOfLessonDao.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findByNameShouldReturnOptionalOfFormOfLessonIfArgumentIsName(){
        FormOfLesson expected = FormOfLesson.builder()
                .withName("lecture")
                .withDuration(60)
                .build();
        FormOfLesson actual = formOfLessonDao.findByName("lecture").get(0);

        assertFormsOfLesson(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<FormOfLesson> expected = Collections.emptyList();
        List<FormOfLesson> actual = formOfLessonDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
