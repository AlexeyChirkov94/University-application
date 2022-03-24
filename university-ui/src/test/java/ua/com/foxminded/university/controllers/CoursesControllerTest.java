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
import ua.com.foxminded.university.ControllerTestsContextConfiguration;
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.ProfessorService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestsContextConfiguration.class})
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class CoursesControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    CourseService courseService;

    @Autowired
    ProfessorService professorService;

    @Autowired
    DepartmentService departmentService;

    MockMvc mockMvc;

    CoursesController coursesController;

    @BeforeEach
    void setUp() {
        Mockito.reset(courseService);
        Mockito.reset(professorService);
        Mockito.reset(departmentService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CoursesController(courseService, professorService, departmentService))
                .build();
        coursesController = webApplicationContext.getBean(CoursesController.class);
    }

    @Test
    void showAllShouldAddCourseToModelAndRenderIndexView() throws Exception {
        CourseResponse first = new CourseResponse();
        CourseResponse second = new CourseResponse();
        first.setId(1L);
        second.setId(2L);
        List<CourseResponse> courses = Arrays.asList(first, second);
        when(courseService.findAll(null)).thenReturn(courses);

        mockMvc.perform(get("/course"))
                .andExpect(status().is(200))
                .andExpect(view().name("course/all"))
                .andExpect(forwardedUrl("course/all"))
                .andExpect(model().attribute("courses", hasSize(2)))
                .andExpect(model().attribute("courses", is(courses)));

        verify(courseService).findAll(null);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void showShouldAddCourseToModelAndRenderShowView() throws Exception {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(1L);
        courseResponse.setName("Math");

        when(courseService.findById(1L)).thenReturn(courseResponse);

        mockMvc.perform(get("/course/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("course/show"))
                .andExpect(forwardedUrl("course/show"))
                .andExpect(model().attribute("course", is(courseResponse)));


        verify(courseService).findById(1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void newShouldGetCourseFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/course/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("course/add"))
                .andExpect(forwardedUrl("course/add"));
    }

    @Test
    void editShouldAddCourseToModelAndRenderShowEditLessonHaveNoProfessorAndCourse() throws Exception {
        CourseResponse courseResponse = new CourseResponse();
        CourseRequest courseRequest = new CourseRequest();
        courseResponse.setId(1L);
        courseResponse.setName("Math");
        courseRequest.setName(courseResponse.getName());
        DepartmentResponse department = new DepartmentResponse();
        department.setName("MathDepartment");
        List<DepartmentResponse> departments = Arrays.asList(department);
        ProfessorResponse professorMath = new ProfessorResponse();
        ProfessorResponse professorNotMath = new ProfessorResponse();
        List<ProfessorResponse> allProfessors = new ArrayList<>();
        allProfessors.add(professorMath);
        allProfessors.add(professorNotMath);
        List<ProfessorResponse> mathProfessors = new ArrayList<>();
        mathProfessors.add(professorMath);
        allProfessors.removeAll(mathProfessors);

        when(courseService.findById(1L)).thenReturn(courseResponse);
        when(professorService.findAll()).thenReturn(allProfessors);
        when(professorService.findByCourseId(1L)).thenReturn(mathProfessors);
        when(departmentService.findAll()).thenReturn(departments);

        mockMvc.perform(get("/course/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("course/edit"))
                .andExpect(forwardedUrl("course/edit"))
                .andExpect(model().attribute("teachersOfCourse", is(mathProfessors)))
                .andExpect(model().attribute("notTeachersOfCourse", is(allProfessors)))
                .andExpect(model().attribute("courseRequest", is(courseRequest)))
                .andExpect(model().attribute("departments", is(departments)))
                .andExpect(model().attribute("courseResponse", is(courseResponse)));

        verify(courseService, times(2)).findById(1L);
        verify(professorService).findAll();
        verify(professorService).findByCourseId(1L);
        verify(departmentService).findAll();
        verifyNoMoreInteractions(courseService);
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    void createShouldGetCourseFromModelAndRenderIndexView() throws Exception {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setName("Math");
        courseRequest.setDepartmentId(2L);

        CourseResponse courseResponse = new CourseResponse();

        when(courseService.create(courseRequest)).thenReturn(courseResponse);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", courseRequest.getId().toString())
                .param("name", courseRequest.getName())
                .param("departmentId", courseRequest.getDepartmentId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/course"))
                .andExpect(redirectedUrl("/course"))
                .andExpect(model().attribute("course", hasProperty("id", is(courseRequest.getId()))))
                .andExpect(model().attribute("course", hasProperty("name", is(courseRequest.getName()))))
                .andExpect(model().attribute("course", hasProperty("departmentId", is(courseRequest.getDepartmentId()))));

        verify(courseService).create(courseRequest);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void createNotValidCourseShouldReturnProfessorNewView() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/course")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "s"))
                .andExpect(model().attributeHasFieldErrorCode(
                        "course","name","Size")).
                andExpect(view().name("course/add")).
                andExpect(status().isOk());
    }

    @Test
    void updateShouldGetCourseFromModelAndRenderIndexView() throws Exception {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setId(1L);
        courseRequest.setName("Math");
        courseRequest.setDepartmentId(2L);

        doNothing().when(courseService).edit(courseRequest);

        mockMvc.perform(patch("/course/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", courseRequest.getId().toString())
                .param("name", courseRequest.getName())
                .param("departmentId", courseRequest.getDepartmentId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/course"))
                .andExpect(redirectedUrl("/course"))
                .andExpect(model().attribute("courseRequest", hasProperty("id", is(courseRequest.getId()))))
                .andExpect(model().attribute("courseRequest", hasProperty("name", is(courseRequest.getName()))))
                .andExpect(model().attribute("courseRequest", hasProperty("departmentId", is(courseRequest.getDepartmentId()))));

        verify(courseService).edit(courseRequest);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void updateNotValidCourseShouldReturnProfessorNewView() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/course/1")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "s"))
                .andExpect(model().attributeHasFieldErrorCode(
                        "courseRequest","name","Size")).
                andExpect(view().name("course/edit")).
                andExpect(status().isOk());
    }

    @Test
    void deleteShouldGetCourseIdFromUrlAndRenderIndexView() throws Exception {

        doNothing().when(courseService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/course/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/course"))
                .andExpect(redirectedUrl("/course"));

        verify(courseService).deleteById(1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addProfessorToCourseShouldGetCourseIdIdFromUrlAndProfessorIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).addCourseToProfessorCourseList(1L, 2L);

        mockMvc.perform(post("/course/1/assign/professor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewProfessor", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/course/1/edit"))
                .andExpect(redirectedUrl("/course/1/edit?idNewProfessor=2"))
                .andExpect(model().attribute("idNewProfessor", is(2L)));

        verify(courseService).addCourseToProfessorCourseList(1L, 2);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void removeProfessorFromCourseShouldGetCourseIdIdFromUrlAndProfessorIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).removeCourseFromProfessorCourseList(1L, 2L);

        mockMvc.perform(post("/course/1/remove/professor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idRemovingProfessor", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/course/1/edit"))
                .andExpect(redirectedUrl("/course/1/edit?idRemovingProfessor=2"))
                .andExpect(model().attribute("idRemovingProfessor", is(2L)));

        verify(courseService).removeCourseFromProfessorCourseList(1L, 2);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void entityDoNotExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        Exception exception = new EntityDontExistException("Course with id = 15 not exist");

        when(courseService.findById(15L)).thenThrow(exception);

        mockMvc.perform(get("/course/15")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity not exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(courseService).findById(15L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void entityAlreadyExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName("Course 1");
        courseRequest.setDepartmentId(0L);

        Exception exception = new EntityAlreadyExistException("Course with same name already exist");

        when(courseService.create(courseRequest)).thenThrow(exception);

        mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Course 1")
                .param("departmentId", "0")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity already exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(courseService).create(courseRequest);
        verifyNoMoreInteractions(courseService);
    }

}
