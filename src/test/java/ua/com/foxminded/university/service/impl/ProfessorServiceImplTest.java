package ua.com.foxminded.university.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.university.dao.interfaces.GroupDao;
import ua.com.foxminded.university.dao.interfaces.ProfessorDao;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Professor;
import ua.com.foxminded.university.entity.ScienceDegree;
import ua.com.foxminded.university.mapper.interfaces.ProfessorRequestMapper;
import ua.com.foxminded.university.mapper.interfaces.ProfessorResponseMapper;
import ua.com.foxminded.university.service.validator.ScienceDegreeValidator;
import ua.com.foxminded.university.service.validator.UserValidator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith( MockitoExtension.class)
class ProfessorServiceImplTest {

    @Mock
    ProfessorDao professorDao;

    @Mock
    GroupDao groupDao;

    @Mock
    UserValidator userValidator;

    @Mock
    ScienceDegreeValidator scienceDegreeValidator;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    ProfessorRequestMapper professorRequestMapper;

    @Mock
    ProfessorResponseMapper professorResponseMapper;

    @InjectMocks
    ProfessorServiceImpl professorService;

    @Test
    void changeScienceDegreeShouldChangeScienceDegreeOfProfessorIfArgumentIsProfessorIdAndScienceDegreeId() {
        long professorId = 1;
        int newScienceDegreeId = 2;

        doNothing().when(scienceDegreeValidator).validate(ScienceDegree.getById(newScienceDegreeId));
        doNothing().when(professorDao).changeScienceDegree(professorId, newScienceDegreeId);

        professorService.changeScienceDegree(professorId, newScienceDegreeId);

        verify(scienceDegreeValidator).validate(ScienceDegree.getById(newScienceDegreeId));
        verify(professorDao).changeScienceDegree(professorId, newScienceDegreeId);
    }

    @Test
    void findByCourseIdShouldReturnListOfProfessorIfArgumentIsCourseId() {
        long courseId = 1;
        List<Professor> professors = Arrays.asList(Professor.builder().withId(1L).build());
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        List<ProfessorResponse> professorResponses = Arrays.asList(professorResponse);

        when(professorResponseMapper.mapEntityToDto(professors.get(0))).thenReturn(professorResponse);
        when(professorDao.findByCourseId(courseId)).thenReturn(professors);

        assertThat(professorService.findByCourseId(courseId)).isEqualTo(professorResponses);

        verify(professorResponseMapper).mapEntityToDto(professors.get(0));
        verify(professorDao).findByCourseId(courseId);
    }

    @Test
    void findByEmailShouldReturnOptionalOfProfessorResponseIfArgumentIsEmail() {
        String email= "Alexey94@gamil.com";

        when(professorDao.findByEmail(email)).thenReturn(Optional.of(Professor.builder().withId(1L).build()));

        professorService.findByEmail(email);

        verify(professorDao).findByEmail(email);
    }

    @Test
    void registerShouldAddProfessorToDBIfArgumentsIsProfessorRequest() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorDao.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(password);

        professorService.register(professorRequest);

        verify(userValidator).validate(professorRequest);
        verify(professorDao).findByEmail(email);
        verify(passwordEncoder).encode(password);
    }

    @Test
    void registerShouldThrowEntityAlreadyExistExceptionIfProfessorWithSameNameAlreadyExist() {
        String email= "Alexey94@gamil.com";
        String password= "12345";
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setEmail(email);
        professorRequest.setPassword(password);

        doNothing().when(userValidator).validate(professorRequest);
        when(professorDao.findByEmail(email)).thenReturn(Optional.of(Professor.builder().withEmail(email).build()));

        assertThatThrownBy(() -> professorService.register(professorRequest)).hasMessage("This email already registered");

        verify(userValidator).validate(professorRequest);
        verify(professorDao).findByEmail(email);
    }

    @Test
    void findByIdShouldReturnOptionalOfProfessorResponseIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(1L).build()));

        professorService.findById(professorId);

        verify(professorDao).findById(professorId);
    }

    @Test
    void findAllIdShouldReturnListOfProfessorResponseIfArgumentIsPageNumber() {
        String pageNumber = "2";

        when(professorDao.count()).thenReturn(11L);
        when(professorDao.findAll(2, 5)).thenReturn(Arrays.asList(Professor.builder().withId(1L).build()));

        professorService.findAll(pageNumber);

        verify(professorDao).count();
        verify(professorDao).findAll(2, 5);
    }

    @Test
    void findAllIdShouldReturnListOfProfessorResponseNoArguments() {
        when(professorDao.findAll()).thenReturn(Arrays.asList(Professor.builder().withId(1L).build()));

        professorService.findAll();

        verify(professorDao).findAll();
    }

    @Test
    void editShouldEditDataOfProfessorIfArgumentNewProfessorRequest() {
        Professor professor = Professor.builder().withId(1L).build();
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);

        when(professorRequestMapper.mapDtoToEntity(professorRequest)).thenReturn(professor);
        doNothing().when(professorDao).update(professor);

        professorService.edit(professorRequest);

        verify(professorRequestMapper).mapDtoToEntity(professorRequest);
        verify(professorDao).update(professor);
    }

    @Test
    void deleteShouldDeleteDataOfProfessorIfArgumentIsProfessorId() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.of(Professor.builder().withId(professorId).build()));
        when(professorDao.deleteById(professorId)).thenReturn(true);

        professorService.deleteById(professorId);

        verify(professorDao).findById(professorId);
        verify(professorDao).deleteById(professorId);
    }

    @Test
    void deleteShouldDoNothingIfArgumentProfessorDontExist() {
        long professorId = 1;

        when(professorDao.findById(professorId)).thenReturn(Optional.empty());

        professorService.deleteById(professorId);

        verify(professorDao).findById(professorId);
    }

}
