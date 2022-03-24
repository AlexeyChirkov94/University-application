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
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.ProfessorService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
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
class DepartmentsControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    ProfessorService professorService;

    @Autowired
    CourseService courseService;

    @Autowired
    GroupService groupService;

    MockMvc mockMvc;

    DepartmentsController departmentsController;

    @BeforeEach
    void setUp() {
        Mockito.reset(departmentService);
        Mockito.reset(professorService);
        Mockito.reset(courseService);
        Mockito.reset(groupService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(new DepartmentsController(departmentService, professorService, courseService, groupService))
                .build();
        departmentsController = webApplicationContext.getBean(DepartmentsController.class);
    }

    @Test
    void showAllShouldAddFormsOfEducationToModelAndRenderIndexView() throws Exception {
        DepartmentResponse first = new DepartmentResponse();
        DepartmentResponse second = new DepartmentResponse();
        first.setId(1L);
        second.setId(2L);
        List<DepartmentResponse> departmentsResponse = Arrays.asList(first, second);
        when(departmentService.findAll(null)).thenReturn(departmentsResponse);

        mockMvc.perform(get("/department"))
                .andExpect(status().is(200))
                .andExpect(view().name("department/all"))
                .andExpect(forwardedUrl("department/all"))
                .andExpect(model().attribute("departments", hasSize(2)))
                .andExpect(model().attribute("departments", is(departmentsResponse)));

        verify(departmentService).findAll(null);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    void showShouldAddDepartmentToModelAndRenderShowView() throws Exception {
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("full-time");
        ProfessorResponse professor1 = new ProfessorResponse();
        ProfessorResponse professor2 = new ProfessorResponse();
        List<ProfessorResponse> departmentProfessors = Arrays.asList(professor1, professor2);
        CourseResponse course1 = new CourseResponse();
        CourseResponse course2 = new CourseResponse();
        List<CourseResponse> departmentCourse = Arrays.asList(course1, course2);
        GroupResponse groupResponse1 = new GroupResponse();
        GroupResponse groupResponse2 = new GroupResponse();
        List<GroupResponse> departmentGroups = Arrays.asList(groupResponse1, groupResponse2);

        when(departmentService.findById(1L)).thenReturn(departmentResponse);
        when(professorService.findByDepartmentId(1L)).thenReturn(departmentProfessors);
        when(courseService.findByDepartmentId(1L)).thenReturn(departmentCourse);
        when(groupService.findByDepartmentId(1L)).thenReturn(departmentGroups);

        mockMvc.perform(get("/department/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("department/show"))
                .andExpect(forwardedUrl("department/show"))
                .andExpect(model().attribute("department", is(departmentResponse)))
                .andExpect(model().attribute("professors", is(departmentProfessors)))
                .andExpect(model().attribute("courses", is(departmentCourse)))
                .andExpect(model().attribute("groups", is(departmentGroups)));

        verify(departmentService).findById(1L);
        verify(professorService).findByDepartmentId(1L);
        verify(courseService).findByDepartmentId(1L);
        verify(groupService).findByDepartmentId(1L);
        verifyNoMoreInteractions(departmentService);
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(courseService);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void newShouldGetDepartmentFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/department/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("department/add"))
                .andExpect(forwardedUrl("department/add"));
    }

    @Test
    void editShouldAddDepartmentToModelAndRenderEditView() throws Exception {
        DepartmentResponse departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("Department of  Math");
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(departmentResponse.getId());
        departmentRequest.setName(departmentResponse.getName());

        ProfessorResponse professorResponse2 = new ProfessorResponse();
        ProfessorResponse professorResponse1 = new ProfessorResponse();
        professorResponse1.setId(1L);
        professorResponse2.setId(2L);
        List<ProfessorResponse> allProfessors = new ArrayList<>();
        allProfessors.add(professorResponse1);
        allProfessors.add(professorResponse2);
        List<ProfessorResponse> departmentProfessors = new ArrayList<>();
        departmentProfessors.add(professorResponse1);
        List<ProfessorResponse> anotherProfessors = new ArrayList<>();
        anotherProfessors.add(professorResponse2);

        CourseResponse courseResponse2 = new CourseResponse();
        CourseResponse courseResponse1 = new CourseResponse();
        courseResponse1.setId(1L);
        courseResponse2.setId(2L);
        List<CourseResponse> allCourses = new ArrayList<>();
        allCourses.add(courseResponse1);
        allCourses.add(courseResponse2);
        List<CourseResponse> departmentCourses = new ArrayList<>();
        departmentCourses.add(courseResponse1);
        List<CourseResponse> anotherCourses = new ArrayList<>();
        anotherCourses.add(courseResponse2);

        GroupResponse groupResponse2 = new GroupResponse();
        GroupResponse groupResponse1 = new GroupResponse();
        groupResponse1.setId(1L);
        groupResponse2.setId(2L);
        List<GroupResponse> allGroups = new ArrayList<>();
        allGroups.add(groupResponse1);
        allGroups.add(groupResponse2);
        List<GroupResponse> departmentGroups = new ArrayList<>();
        departmentGroups.add(groupResponse1);
        List<GroupResponse> anotherGroups = new ArrayList<>();
        anotherGroups.add(groupResponse2);

        when(departmentService.findById(1L)).thenReturn(departmentResponse);
        when(professorService.findByDepartmentId(1L)).thenReturn(departmentProfessors);
        when(professorService.findAll()).thenReturn(allProfessors);
        when(courseService.findByDepartmentId(1L)).thenReturn(departmentCourses);
        when(courseService.findAll()).thenReturn(allCourses);
        when(groupService.findByDepartmentId(1L)).thenReturn(departmentGroups);
        when(groupService.findAll()).thenReturn(allGroups);

        mockMvc.perform(get("/department/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("department/edit"))
                .andExpect(forwardedUrl("department/edit"))
                .andExpect(model().attribute("departmentRequest", is(departmentRequest)))
                .andExpect(model().attribute("departmentProfessors", is(departmentProfessors)))
                .andExpect(model().attribute("anotherProfessors", is(anotherProfessors)))
                .andExpect(model().attribute("departmentCourses", is(departmentCourses)))
                .andExpect(model().attribute("anotherCourses", is(anotherCourses)))
                .andExpect(model().attribute("departmentGroups", is(departmentGroups)))
                .andExpect(model().attribute("anotherGroups", is(anotherGroups)));

        verify(departmentService).findById(1L);
        verify(professorService).findByDepartmentId(1L);
        verify(professorService).findAll();
        verify(courseService).findByDepartmentId(1L);
        verify(courseService).findAll();
        verify(groupService).findByDepartmentId(1L);
        verify(groupService).findAll();
        verifyNoMoreInteractions(departmentService);
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(courseService);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void createShouldGetDepartmentFromModelAndRenderIndexView() throws Exception {
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(1L);
        departmentRequest.setName("full-time");

        DepartmentResponse departmentResponse = new DepartmentResponse();

        when(departmentService.create(departmentRequest)).thenReturn(departmentResponse);

        mockMvc.perform(post("/department")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", departmentRequest.getId().toString())
                .param("name", departmentRequest.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department"))
                .andExpect(redirectedUrl("/department"))
                .andExpect(model().attribute("department", hasProperty("id", is(departmentRequest.getId()))))
                .andExpect(model().attribute("department", hasProperty("name", is(departmentRequest.getName()))));

        verify(departmentService).create(departmentRequest);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    void createNotValidProfessorShouldReturnProfessorNewView() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/department")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "s"))
                .andExpect(model().attributeHasFieldErrorCode(
                        "department","name","Size")).
                andExpect(view().name("department/add")).
                andExpect(status().isOk());
    }

    @Test
    void updateShouldGetDepartmentFromModelAndRenderIndexView() throws Exception {
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(1L);
        departmentRequest.setName("full-time");

        doNothing().when(departmentService).edit(departmentRequest);

        mockMvc.perform(patch("/department/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", departmentRequest.getId().toString())
                .param("name", departmentRequest.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department"))
                .andExpect(redirectedUrl("/department"))
                .andExpect(model().attribute("departmentRequest", hasProperty("id", is(departmentRequest.getId()))))
                .andExpect(model().attribute("departmentRequest", hasProperty("name", is(departmentRequest.getName()))));

        verify(departmentService).edit(departmentRequest);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    void updateNotValidProfessorShouldReturnProfessorNewView() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.patch("/department/1")
                .accept(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "s"))
                .andExpect(model().attributeHasFieldErrorCode(
                        "departmentRequest","name","Size")).
                andExpect(view().name("department/edit")).
                andExpect(status().isOk());
    }

    @Test
    void deleteShouldGetDepartmentIdFromUrlAndRenderIndexView() throws Exception {

        doNothing().when(departmentService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/department/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department"))
                .andExpect(redirectedUrl("/department"));

        verify(departmentService).deleteById(1L);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    void addProfessorToDepartmentShouldGetFromDepartmentIdIdFromUrlAndProfessorIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(professorService).changeDepartment(2L, 1L);

        mockMvc.perform(post("/department/1/assign/professor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewProfessor", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department/1/edit"))
                .andExpect(redirectedUrl("/department/1/edit?idNewProfessor=2"))
                .andExpect(model().attribute("idNewProfessor", is(2L)));

        verify(professorService).changeDepartment(2L, 1L);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void removeProfessorFromDepartmentShouldGetFromDepartmentIdIdFromUrlAndProfessorIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(professorService).removeDepartmentFromProfessor(2L);

        mockMvc.perform(post("/department/1/remove/professor")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idRemovingProfessor", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department/1/edit"))
                .andExpect(redirectedUrl("/department/1/edit?idRemovingProfessor=2"))
                .andExpect(model().attribute("idRemovingProfessor", is(2L)));

        verify(professorService).removeDepartmentFromProfessor(2L);
        verifyNoMoreInteractions(professorService);
    }

    @Test
    void addCourseToDepartmentShouldGetFromDepartmentIdIdFromUrlAndCourseIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).changeDepartment(2L, 1L);

        mockMvc.perform(post("/department/1/assign/course")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewCourse", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department/1/edit"))
                .andExpect(redirectedUrl("/department/1/edit?idNewCourse=2"))
                .andExpect(model().attribute("idNewCourse", is(2L)));

        verify(courseService).changeDepartment(2L, 1L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void removeCourseFromDepartmentShouldGetFromDepartmentIdIdFromUrlAndCourseIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(courseService).removeDepartmentFromCourse(2L);

        mockMvc.perform(post("/department/1/remove/course")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idRemovingCourse", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department/1/edit"))
                .andExpect(redirectedUrl("/department/1/edit?idRemovingCourse=2"))
                .andExpect(model().attribute("idRemovingCourse", is(2L)));

        verify(courseService).removeDepartmentFromCourse(2L);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void addGroupToDepartmentShouldGetFromDepartmentIdIdFromUrlAndGroupIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(groupService).changeDepartment(2L, 1L);

        mockMvc.perform(post("/department/1/assign/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewGroup", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department/1/edit"))
                .andExpect(redirectedUrl("/department/1/edit?idNewGroup=2"))
                .andExpect(model().attribute("idNewGroup", is(2L)));

        verify(groupService).changeDepartment(2L, 1L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void removeGroupFromDepartmentShouldGetFromDepartmentIdIdFromUrlAndGroupIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(groupService).removeDepartmentFromGroup(2L);

        mockMvc.perform(post("/department/1/remove/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idRemovingGroup", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/department/1/edit"))
                .andExpect(redirectedUrl("/department/1/edit?idRemovingGroup=2"))
                .andExpect(model().attribute("idRemovingGroup", is(2L)));

        verify(groupService).removeDepartmentFromGroup(2L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void entityDoNotExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        Exception exception = new EntityDontExistException("Department with id = 15 not exist");

        when(departmentService.findById(15L)).thenThrow(exception);

        mockMvc.perform(get("/department/15")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity not exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(departmentService).findById(15L);
        verifyNoMoreInteractions(departmentService);
    }

    @Test
    void entityAlreadyExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setName("lecture");

        Exception exception = new EntityAlreadyExistException("Department with same name already exist");

        when(departmentService.create(departmentRequest)).thenThrow(exception);

        mockMvc.perform(post("/department")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", departmentRequest.getName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity already exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(departmentService).create(departmentRequest);
        verifyNoMoreInteractions(departmentService);
    }

}
