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
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.CourseService;
import ua.com.foxminded.university.service.interfaces.DepartmentService;
import ua.com.foxminded.university.service.interfaces.LessonService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
import static ua.com.foxminded.university.controllers.ControllersUtility.getStringDateTimes;

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
    DepartmentService departmentService;

    @Autowired
    LessonService lessonService;

    @Autowired
    CourseService courseService;

    MockMvc mockMvc;

    ProfessorsController professorsController;

    @BeforeEach
    void setUp() {
        Mockito.reset(professorService);
        Mockito.reset(courseService);
        Mockito.reset(departmentService);
        Mockito.reset(lessonService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        professorsController = webApplicationContext.getBean(ProfessorsController.class);
    }

    @Test
    void showAllShouldAddProfessorsToModelAndRenderIndexView() throws Exception {

        ProfessorResponse first = new ProfessorResponse();
        ProfessorResponse second = new ProfessorResponse();
        first.setFirstName("Alexey");
        first.setLastName("Chirkov");
        second.setFirstName("Bob");
        second.setLastName("Stivenson");
        when(professorService.findAll(null)).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/professor"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professor/all"))
                .andExpect(forwardedUrl("/professor/all"))
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
    void showShouldAddProfessorToModelAndRenderShowView() throws Exception {

        ProfessorResponse professor = new ProfessorResponse();
        professor.setId(1L);
        professor.setFirstName("Alexey");
        professor.setLastName("Chirkov");
        professor.setEmail("chirkov@gamil.com");
        professor.setPassword("1234");
        when(professorService.findById(1L)).thenReturn(professor);

        mockMvc.perform(get("/professor/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professor/show"))
                .andExpect(forwardedUrl("/professor/show"))
                .andExpect(model().attribute("professor", is(professor)));


        verify(professorService).findById(1L);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void newShouldGetProfessorFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/professor/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professor/add"))
                .andExpect(forwardedUrl("/professor/add"));
    }

    @Test
    void editShouldAddProfessorToModelAndRenderShowEdit() throws Exception {
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

        DepartmentResponse department1 = new DepartmentResponse();
        DepartmentResponse department2 = new DepartmentResponse();
        department1.setId(1L);
        department2.setId(2L);

        List<DepartmentResponse> allDepartments = new ArrayList<>();
        allDepartments.add(department1);
        allDepartments.add(department2);

        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        professorResponse.setFirstName("Alexey");
        professorResponse.setLastName("Chirkov");
        professorResponse.setEmail("chirkov@gamil.com");
        professorResponse.setPassword("1234");
        professorResponse.setCoursesResponse(professorCourses);
        professorResponse.setScienceDegreeResponse(ScienceDegreeResponse.GRADUATE);

        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setFirstName(professorResponse.getFirstName());
        professorRequest.setLastName(professorResponse.getLastName());
        professorRequest.setEmail(professorResponse.getEmail());

        when(professorService.findById(1L)).thenReturn(professorResponse);
        when(courseService.findByProfessorId(1L)).thenReturn(professorCourses);
        when(courseService.findAll()).thenReturn(allCourses);
        when(departmentService.findAll()).thenReturn(allDepartments);

        mockMvc.perform(get("/professor/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professor/edit"))
                .andExpect(forwardedUrl("/professor/edit"))
                .andExpect(model().attribute("professorResponse", is(professorResponse)))
                .andExpect(model().attribute("professorRequest", is(professorRequest)))
                .andExpect(model().attribute("ScienceDegrees", is(ScienceDegreeResponse.values())))
                .andExpect(model().attribute("departments", is(allDepartments)))
                .andExpect(model().attribute("professorsCourses", is(professorCourses)))
                .andExpect(model().attribute("anotherCourses", is(anotherCourses)));

        verify(professorService).findById(1L);
        verify(courseService).findByProfessorId(1L);
        verify(courseService).findAll();
        verify(departmentService).findAll();
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(courseService);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    void createShouldGetProfessorFromModelAndRenderIndexView() throws Exception {
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setFirstName("Alex");
        professorRequest.setLastName("Chirkov");
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        professorResponse.setFirstName("Alex");
        professorResponse.setLastName("Chirkov");

        when(professorService.register(professorRequest)).thenReturn(professorResponse);

        mockMvc.perform(post("/professor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", professorResponse.getId().toString())
                .param("firstName", professorResponse.getFirstName())
                .param("lastName", professorResponse.getLastName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professor"))
                .andExpect(redirectedUrl("/professor"))
                .andExpect(model().attribute("professor", hasProperty("id", is(professorResponse.getId()))))
                .andExpect(model().attribute("professor", hasProperty("firstName",is(professorResponse.getFirstName()))))
                .andExpect(model().attribute("professor", hasProperty("lastName",is(professorResponse.getLastName()))));

        verify(professorService).register(professorRequest);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void updateShouldGetProfessorFromModelAndRenderIndexView() throws Exception {
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setId(1L);
        professorRequest.setFirstName("Alex");
        professorRequest.setLastName("Chirkov");

        doNothing().when(professorService).edit(professorRequest);

        mockMvc.perform(patch("/professor/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", professorRequest.getId().toString())
                .param("firstName", professorRequest.getFirstName())
                .param("lastName", professorRequest.getLastName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professor"))
                .andExpect(redirectedUrl("/professor"))
                .andExpect(model().attribute("professor", hasProperty("id", is(professorRequest.getId()))))
                .andExpect(model().attribute("professor", hasProperty("firstName",is(professorRequest.getFirstName()))))
                .andExpect(model().attribute("professor", hasProperty("lastName",is(professorRequest.getLastName()))));

        verify(professorService).edit(professorRequest);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void timetableShouldAddLessonsToModelAndRenderTimetableView() throws Exception {
        ProfessorResponse professor = new ProfessorResponse();
        professor.setId(1L);
        professor.setFirstName("Alex");
        LessonResponse lesson1 = new LessonResponse();
        LessonResponse lesson2 = new LessonResponse();
        lesson1.setId(1L);
        lesson2.setId(2L);
        List<LessonResponse> lessons = Arrays.asList(lesson1, lesson2);

        Map<Long, String> stringDateTimes = getStringDateTimes(lessons);

        when(professorService.findById(1L)).thenReturn(professor);
        when(lessonService.formTimeTableForProfessor(1L)).thenReturn(lessons);

        mockMvc.perform(get("/professor/1/timetable"))
                .andExpect(status().is(200))
                .andExpect(view().name("/professor/timetable"))
                .andExpect(forwardedUrl("/professor/timetable"))
                .andExpect(model().attribute("professor", is(professor)))
                .andExpect(model().attribute("lessons", is(lessons)))
                .andExpect(model().attribute("stringDateTimes", is(stringDateTimes)));

        verify(professorService).findById(1L);
        verify(lessonService).formTimeTableForProfessor(1L);
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void addCourseShouldGetProfessorIdFromUrlAndIdOfNewCourseFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).addCourseToProfessorCourseList(2, 1L);

        mockMvc.perform(post("/professor/1/assign/course")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idOfAddingCourse", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professor/1/edit"))
                .andExpect(redirectedUrl("/professor/1/edit?idOfAddingCourse=2"))
                .andExpect(model().attribute("idOfAddingCourse", is(2)));

        verify(courseService).addCourseToProfessorCourseList(2, 1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void removeCourseShouldGetProfessorIdFromUrlAndIdOfRemovingCourseFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).removeCourseFromProfessorCourseList(2, 1L);

        mockMvc.perform(post("/professor/1/remove/course")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idOfRemovingCourse", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professor/1/edit"))
                .andExpect(redirectedUrl("/professor/1/edit?idOfRemovingCourse=2"))
                .andExpect(model().attribute("idOfRemovingCourse", is(2)));

        verify(courseService).removeCourseFromProfessorCourseList(2, 1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void deleteShouldGetProfessorIdFromUrlAndRenderIndexView() throws Exception {

        when(professorService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/professor/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/professor"))
                .andExpect(redirectedUrl("/professor"));

        verify(professorService).deleteById(1L);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void validateProfessorShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        ProfessorRequest notValidProfessor = new ProfessorRequest();
        notValidProfessor.setFirstName("Alex");
        Exception exception = new ValidateException("password don`t match the pattern");

        when(professorService.register(notValidProfessor)).thenThrow(exception);

        mockMvc.perform(post("/professor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", notValidProfessor.getFirstName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/common creating error"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(professorService).register(notValidProfessor);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void entityAlreadyExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        ProfessorRequest repeatableProfessor = new ProfessorRequest();
        repeatableProfessor.setFirstName("Alex");
        Exception exception = new EntityAlreadyExistException("Student with same email already exist");

        when(professorService.register(repeatableProfessor)).thenThrow(exception);

        mockMvc.perform(post("/professor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", repeatableProfessor.getFirstName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity already exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(professorService).register(repeatableProfessor);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void entityDoNotExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        Exception exception = new EntityDontExistException("Professor with id = 15 not exist");

        when(professorService.findById(15L)).thenThrow(exception);

        mockMvc.perform(get("/professor/15")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity not exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(professorService).findById(15L);
        verifyNoMoreInteractions(professorService);
    }

}
