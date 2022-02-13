package ua.com.foxminded.university.dao.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.dao.FormOfEducationDao;
import ua.com.foxminded.university.dao.GroupDao;
import ua.com.foxminded.university.entity.FormOfEducation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertFormsOfEducation;

class FormOfEducationDaoImplTest {

    ApplicationContext context;
    FormOfEducationDao formOfEducationDao;
    GroupDao groupDao;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        formOfEducationDao = context.getBean(FormOfEducationDaoImpl.class);
        groupDao = context.getBean(GroupDaoImpl.class);
    }

    @Test
    void createAndReadShouldAddNewFormOfEducationToDatabaseIfArgumentIsFormOfEducation(){
        FormOfEducation addingFormOfEducation = FormOfEducation.builder().withName("evening").build();
        formOfEducationDao.save(addingFormOfEducation);
        FormOfEducation readingFormOfEducation = formOfEducationDao.findById(5L).get();

        assertFormsOfEducation(readingFormOfEducation, addingFormOfEducation);
    }

    @Test
    void createAndReadShouldAddNewListOfFormOfEducationToDBIfArgumentIsListOfFormOfEducation(){
        List<FormOfEducation> addingFormOfEducationEntities = Arrays.asList(FormOfEducation.builder().withName("Evening").build(),
                FormOfEducation.builder().withName("Morning").build());
        formOfEducationDao.saveAll(addingFormOfEducationEntities);
        List<FormOfEducation> readingFormOfEducationEntities =  Arrays.asList(formOfEducationDao.findById(5L).get(),
                formOfEducationDao.findById(6L).get());

        assertFormsOfEducation(readingFormOfEducationEntities, addingFormOfEducationEntities);
    }

    @Test
    void updateShouldUpdateDataOfStudentIfArgumentIsFormOfEducation(){
        FormOfEducation expected = FormOfEducation.builder().withId(2L).withName("new name").build();
        formOfEducationDao.update(expected);
        FormOfEducation actual = formOfEducationDao.findById(2L).get();

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void updateListShouldUpdateDataOfCourseIfArgumentIsFormOfEducation(){
        List<FormOfEducation> expected = Arrays.asList(FormOfEducation.builder().withId(1L).withName("day-time").build(),
                FormOfEducation.builder().withId(2L).withName("distance").build());
        formOfEducationDao.updateAll(expected);
        List<FormOfEducation> actual = Arrays.asList(formOfEducationDao.findById(1L).get(),
                formOfEducationDao.findById(2L).get());

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfEducationIfArgumentIsIdOfFormOfEducation(){
        Optional<FormOfEducation> expected = Optional.empty();
        formOfEducationDao.deleteById(3L);
        Optional<FormOfEducation> actual = formOfEducationDao.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void deleteSetShouldDeleteDataOfFormOfEducationIfArgumentIsIdOfFormOfEducation(){
        List<Optional<FormOfEducation>> expected = Arrays.asList(Optional.empty(), Optional.empty());
        Set<Long> idDeletingStudents = new HashSet<>();
        idDeletingStudents.add(3L);
        idDeletingStudents.add(4L);

        formOfEducationDao.deleteByIds(idDeletingStudents);
        List<Optional<FormOfEducation>> actual = Arrays.asList(formOfEducationDao.findById(3L),
                formOfEducationDao.findById(4L));

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllShouldReturnAllFormOfEducationNoArguments(){
        List<FormOfEducation> expected = Arrays.asList(FormOfEducation.builder().withName("full-time").build(),
                FormOfEducation.builder().withName("correspondence").build(),
                FormOfEducation.builder().withName("distance").build(),
                FormOfEducation.builder().withName("evening").build());
        List<FormOfEducation> actual = formOfEducationDao.findAll();

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void findAllShouldReturnFormOfEducationOfCurrentPageIfArgumentIsNumberOfPageAndCountOfElementsPerPage(){
        List<FormOfEducation> expected = Arrays.asList(FormOfEducation.builder().withName("distance").build(),
                FormOfEducation.builder().withName("evening").build());
        List<FormOfEducation> actual = formOfEducationDao.findAll(2, 2);

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void countShouldReturnCountOfGroupsWithNoArguments(){
        long expected = 4;
        long actual = formOfEducationDao.count();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByNameShouldReturnOptionalOfFormOfEducationIfArgumentIsName(){
        FormOfEducation expected = FormOfEducation.builder()
                .withName("full-time")
                .build();
        FormOfEducation actual = formOfEducationDao.findByName("full-time").get(0);

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<FormOfEducation> expected = Collections.emptyList();
        List<FormOfEducation> actual = formOfEducationDao.findByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
