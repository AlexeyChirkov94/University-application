package ua.com.foxminded.university.repository.impl;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;
import ua.com.foxminded.university.PersistenceTestsContextConfiguration;
import ua.com.foxminded.university.repository.FormOfEducationRepository;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.entity.FormOfEducation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static ua.com.foxminded.university.testUtils.TestUtility.assertFormsOfEducation;

class FormOfEducationRepositoryImplTest {

    ApplicationContext context;
    FormOfEducationRepository formOfEducationRepository;
    GroupRepository groupRepository;

    {
        context = new AnnotationConfigApplicationContext(PersistenceTestsContextConfiguration.class);
        formOfEducationRepository = context.getBean(FormOfEducationRepository.class);
        groupRepository = context.getBean(GroupRepository.class);
    }

    @Test
    void createAndReadShouldAddNewFormOfEducationToDatabaseIfArgumentIsFormOfEducation(){
        FormOfEducation addingFormOfEducation = FormOfEducation.builder().withName("evening").build();
        formOfEducationRepository.save(addingFormOfEducation);
        FormOfEducation readingFormOfEducation = formOfEducationRepository.findById(5L).get();

        assertFormsOfEducation(readingFormOfEducation, addingFormOfEducation);
    }

    @Test
    void createAndReadShouldAddNewListOfFormOfEducationToDBIfArgumentIsListOfFormOfEducation(){
        List<FormOfEducation> addingFormOfEducationEntities = Arrays.asList(FormOfEducation.builder().withName("Evening").build(),
                FormOfEducation.builder().withName("Morning").build());
        formOfEducationRepository.saveAll(addingFormOfEducationEntities);
        List<FormOfEducation> readingFormOfEducationEntities =  Arrays.asList(formOfEducationRepository.findById(5L).get(),
                formOfEducationRepository.findById(6L).get());

        assertFormsOfEducation(readingFormOfEducationEntities, addingFormOfEducationEntities);
    }

    @Test
    void updateShouldUpdateDataOfStudentIfArgumentIsFormOfEducation(){
        FormOfEducation expected = FormOfEducation.builder().withId(2L).withName("new name").build();
        formOfEducationRepository.save(expected);
        FormOfEducation actual = formOfEducationRepository.findById(2L).get();

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void updateListShouldUpdateDataOfCourseIfArgumentIsFormOfEducation(){
        List<FormOfEducation> expected = Arrays.asList(FormOfEducation.builder().withId(1L).withName("day-time").build(),
                FormOfEducation.builder().withId(2L).withName("distance").build());
        formOfEducationRepository.saveAll(expected);
        List<FormOfEducation> actual = Arrays.asList(formOfEducationRepository.findById(1L).get(),
                formOfEducationRepository.findById(2L).get());

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void deleteShouldDeleteDataOfFormOfEducationIfArgumentIsIdOfFormOfEducation(){
        Optional<FormOfEducation> expected = Optional.empty();
        formOfEducationRepository.deleteById(3L);
        Optional<FormOfEducation> actual = formOfEducationRepository.findById(3L);

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void deleteSetShouldDeleteDataOfFormOfEducationIfArgumentIsIdOfFormOfEducation(){
        List<Optional<FormOfEducation>> expected = Arrays.asList(Optional.empty(), Optional.empty());
        Set<Long> idDeletingStudents = new HashSet<>();
        idDeletingStudents.add(3L);
        idDeletingStudents.add(4L);

        formOfEducationRepository.deleteAllById(idDeletingStudents);
        List<Optional<FormOfEducation>> actual = Arrays.asList(formOfEducationRepository.findById(3L),
                formOfEducationRepository.findById(4L));

        assertThat(expected).isEqualTo(actual);
    }

    @Test
    void findAllShouldReturnAllFormOfEducationNoArguments(){
        List<FormOfEducation> expected = Arrays.asList(FormOfEducation.builder().withName("full-time").build(),
                FormOfEducation.builder().withName("correspondence").build(),
                FormOfEducation.builder().withName("distance").build(),
                FormOfEducation.builder().withName("evening").build());
        List<FormOfEducation> actual = formOfEducationRepository.findAll();

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void findAllShouldReturnFormOfEducationOfCurrentPageIfArgumentIsNumberOfPageAndCountOfElementsPerPage(){
        List<FormOfEducation> expected = Arrays.asList(FormOfEducation.builder().withName("distance").build(),
                FormOfEducation.builder().withName("evening").build());
        List<FormOfEducation> actual = formOfEducationRepository.findAll(PageRequest.of(1, 2)).stream().collect(Collectors.toList());

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void countShouldReturnCountOfGroupsWithNoArguments(){
        long expected = 4;
        long actual = formOfEducationRepository.count();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findByNameShouldReturnOptionalOfFormOfEducationIfArgumentIsName(){
        FormOfEducation expected = FormOfEducation.builder()
                .withName("full-time")
                .build();
        FormOfEducation actual = formOfEducationRepository.findAllByName("full-time").get(0);

        assertFormsOfEducation(actual, expected);
    }

    @Test
    void findByNameShouldReturnOptionalEmptyIfArgumentIsNameAndItDontExist(){
        List<FormOfEducation> expected = Collections.emptyList();
        List<FormOfEducation> actual = formOfEducationRepository.findAllByName("unknown name");

        assertThat(expected).isEqualTo(actual);
    }

}
