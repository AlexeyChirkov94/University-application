package ua.com.foxminded.university.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.CourseService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestsContextConfiguration.class})
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
public class ProfessorsControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    ProfessorService professorService;

    @Autowired
    CourseService courseService;

    MockMvc mockMvc;

    ProfessorsController professorsController;

    @BeforeEach
    void setUp() {
        Mockito.reset(professorService);
        Mockito.reset(courseService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        professorsController = webApplicationContext.getBean(ProfessorsController.class);
    }

    @Test
    void index_ShouldAddProfessorsToModelAndRenderIndexViewWherePreviousPageIsDisable() throws Exception {

        ProfessorResponse first = new ProfessorResponse();
        ProfessorResponse second = new ProfessorResponse();
        first.setFirstName("Alexey");
        first.setLastName("Chirkov");
        second.setFirstName("Bob");
        second.setLastName("Stivenson");
        when(professorService.findAll(null)).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/professors"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professors/index"))
                .andExpect(forwardedUrl("/professors/index"))
                .andExpect(model().attribute("previousPageStatus", is("page-item disabled")))
                .andExpect(model().attribute("professors", hasSize(2)))
                .andExpect(model().attribute("professors", hasItem(
                        allOf(
                                hasProperty("firstName", is("Alexey")),
                                hasProperty("lastName", is("Chirkov"))
                        )
                )))
                .andExpect(model().attribute("professors", hasItem(
                        allOf(
                                hasProperty("firstName", is("Bob")),
                                hasProperty("lastName", is("Stivenson"))
                        )
                )));

        verify(professorService).findAll(null);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void index_ShouldAddProfessorsToModelAndRenderIndexViewWherePreviousPageIsActive() throws Exception {

        ProfessorResponse first = new ProfessorResponse();
        ProfessorResponse second = new ProfessorResponse();
        first.setFirstName("Alexey");
        first.setLastName("Chirkov");
        second.setFirstName("Bob");
        second.setLastName("Stivenson");
        List<ProfessorResponse> professors = Arrays.asList(first, second);
        when(professorService.findAll("2")).thenReturn(professors);

        mockMvc.perform(get("/professors/?page=2"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professors/index"))
                .andExpect(forwardedUrl("/professors/index"))
                .andExpect(model().attribute("previousPageStatus", is("page-item")))
                .andExpect(model().attribute("professors", hasSize(2)))
                .andExpect(model().attribute("professors", is(professors)));

        verify(professorService).findAll("2");
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void show_ShouldAddProfessorToModelAndRenderShowView() throws Exception {

        ProfessorResponse professor = new ProfessorResponse();
        professor.setId(1L);
        professor.setFirstName("Alexey");
        professor.setLastName("Chirkov");
        professor.setEmail("chirkov@gamil.com");
        professor.setPassword("1234");
        when(professorService.findById(1L)).thenReturn(Optional.of(professor));

        mockMvc.perform(get("/professors/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professors/show"))
                .andExpect(forwardedUrl("/professors/show"))
                .andExpect(model().attribute("professor", is(professor)));


        verify(professorService).findById(1L);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void new_ShouldGetProfessorFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/professors/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professors/add"))
                .andExpect(forwardedUrl("/professors/add"));
    }

    @Test
    void edit_ShouldAddProfessorToModelAndRenderShowEdit() throws Exception {
        CourseResponse course1 = new CourseResponse();
        course1.setId(1L);
        course1.setName("Course 1");
        CourseResponse course2 = new CourseResponse();
        course2.setId(2L);
        course2.setName("Course 2");
        CourseResponse course3 = new CourseResponse();
        course2.setId(3L);
        course2.setName("Course 3");

        List<CourseResponse> allCourses = new ArrayList<>();
        allCourses.add(course1);
        allCourses.add(course2);
        allCourses.add(course3);
        List<CourseResponse> professorCourses = new ArrayList<>();
        professorCourses.add(course1);
        professorCourses.add(course2);
        List<CourseResponse> anotherCourses = new ArrayList<>();
        anotherCourses.add(course3);

        ProfessorResponse professor = new ProfessorResponse();
        professor.setId(1L);
        professor.setFirstName("Alexey");
        professor.setLastName("Chirkov");
        professor.setEmail("chirkov@gamil.com");
        professor.setPassword("1234");
        professor.setCoursesResponse(professorCourses);
        professor.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);

        when(professorService.findById(1L)).thenReturn(Optional.of(professor));
        when(courseService.findByProfessorId(1L)).thenReturn(professorCourses);
        when(courseService.findAll()).thenReturn(allCourses);

        mockMvc.perform(get("/professors/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professors/edit"))
                .andExpect(forwardedUrl("/professors/edit"))
                .andExpect(model().attribute("professor", is(professor)))
                .andExpect(model().attribute("professorsCourses", is(professorCourses)))
                .andExpect(model().attribute("anotherCourses", is(anotherCourses)))
                .andExpect(model().attribute("ScienceDegree", is(ScienceDegreeResponse.GRADUATE)))
                .andExpect(model().attribute("ScienceDegrees", is(ScienceDegreeResponse.values())));

        verify(professorService).findById(1L);
        verify(courseService).findByProfessorId(1L);
        verify(courseService).findAll();
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void create_ShouldGetProfessorFromModelAndRenderIndexView() throws Exception {
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setFirstName("Alex");
        professorRequest.setLastName("Chirkov");
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        professorResponse.setFirstName("Alex");
        professorResponse.setLastName("Chirkov");

        when(professorService.register(professorRequest)).thenReturn(professorResponse);

        mockMvc.perform(post("/professors")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", professorResponse.getId().toString())
                .param("firstName", professorResponse.getFirstName())
                .param("lastName", professorResponse.getLastName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professors"))
                .andExpect(redirectedUrl("/professors"))
                .andExpect(model().attribute("professor", hasProperty("id", is(professorResponse.getId()))))
                .andExpect(model().attribute("professor", hasProperty("firstName",is(professorResponse.getFirstName()))))
                .andExpect(model().attribute("professor", hasProperty("lastName",is(professorResponse.getLastName()))));

        verify(professorService).register(professorRequest);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void update_ShouldGetProfessorFromModelAndRenderIndexView() throws Exception {
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setFirstName("Alex");
        professorRequest.setLastName("Chirkov");

        doNothing().when(professorService).edit(professorRequest);

        mockMvc.perform(patch("/professors/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", professorRequest.getId().toString())
                .param("firstName", professorRequest.getFirstName())
                .param("lastName", professorRequest.getLastName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professors"))
                .andExpect(redirectedUrl("/professors"))
                .andExpect(model().attribute("professor", hasProperty("id", is(professorRequest.getId()))))
                .andExpect(model().attribute("professor", hasProperty("firstName",is(professorRequest.getFirstName()))))
                .andExpect(model().attribute("professor", hasProperty("lastName",is(professorRequest.getLastName()))));

        verify(professorService).edit(professorRequest);
        verifyNoMoreInteractions(professorService);
    }


    @Test
    void changeScienceDegree_ShouldGetProfessorIdFromUrlAndScienceDegreeIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(professorService).changeScienceDegree(1L, 2);

        mockMvc.perform(post("/professors/1/changeScienceDegree")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewScienceDegree", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professors/1/edit"))
                .andExpect(redirectedUrl("/professors/1/edit?idNewScienceDegree=2"))
                .andExpect(model().attribute("idNewScienceDegree", is(2)));

        verify(professorService).changeScienceDegree(1L, 2);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void addCourse_ShouldGetProfessorIdFromUrlAndIdOfNewCourseFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).addCourseToProfessorCourseList(2, 1L);

        mockMvc.perform(post("/professors/1/addCourse")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idOfAddingCourse", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professors/1/edit"))
                .andExpect(redirectedUrl("/professors/1/edit?idOfAddingCourse=2"))
                .andExpect(model().attribute("idOfAddingCourse", is(2)));

        verify(courseService).addCourseToProfessorCourseList(2, 1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void removeCourse_ShouldGetProfessorIdFromUrlAndIdOfRemovingCourseFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).removeCourseFromProfessorCourseList(2, 1L);

        mockMvc.perform(post("/professors/1/removeCourse")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idOfRemovingCourse", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professors/1/edit"))
                .andExpect(redirectedUrl("/professors/1/edit?idOfRemovingCourse=2"))
                .andExpect(model().attribute("idOfRemovingCourse", is(2)));

        verify(courseService).removeCourseFromProfessorCourseList(2, 1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void delete_ShouldGetProfessorIdFromUrlAndRenderIndexView() throws Exception {

        when(professorService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/professors/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professors"))
                .andExpect(redirectedUrl("/professors"));

        verify(professorService).deleteById(1L);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void validateProfessor_ShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        ProfessorRequest notValidProfessor = new ProfessorRequest();
        notValidProfessor.setFirstName("Alex");
        Exception exception = new ValidateException("password don`t match the pattern");

        when(professorService.register(notValidProfessor)).thenThrow(exception);

        mockMvc.perform(post("/professors")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", notValidProfessor.getFirstName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/creating user error"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(professorService).register(notValidProfessor);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void entityAlreadyExist_ShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        ProfessorRequest repeatableProfessor = new ProfessorRequest();
        repeatableProfessor.setFirstName("Alex");
        Exception exception = new EntityAlreadyExistException("Student with same email already exist");

        when(professorService.register(repeatableProfessor)).thenThrow(exception);

        mockMvc.perform(post("/professors")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", repeatableProfessor.getFirstName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/creating user error"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(professorService).register(repeatableProfessor);
        verifyNoMoreInteractions(professorService);
    }

}
