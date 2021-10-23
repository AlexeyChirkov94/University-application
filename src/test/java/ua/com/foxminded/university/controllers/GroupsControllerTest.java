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
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.service.interfaces.DepartmentService;
import ua.com.foxminded.university.service.interfaces.FormOfEducationService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.StudentService;
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
public class GroupsControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    GroupService groupService;

    @Autowired
    FormOfEducationService formOfEducationService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    StudentService studentService;

    MockMvc mockMvc;

    GroupsController groupsController;

    @BeforeEach
    void setUp() {
        Mockito.reset(groupService);
        Mockito.reset(formOfEducationService);
        Mockito.reset(departmentService);
        Mockito.reset(studentService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        groupsController = webApplicationContext.getBean(GroupsController.class);
    }

    @Test
    void indexShouldAddGroupsToModelAndRenderIndexViewWherePreviousPageIsDisable() throws Exception {

        GroupResponse first = new GroupResponse();
        GroupResponse second = new GroupResponse();
        first.setName("Group 1");
        second.setName("Group 2");;
        when(groupService.findAll(null)).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/groups"))
                .andExpect(status().is(200))
                .andExpect(view().name("/groups/index"))
                .andExpect(forwardedUrl("/groups/index"))
                .andExpect(model().attribute("previousPageStatus", is("page-item disabled")))
                .andExpect(model().attribute("groups", hasSize(2)))
                .andExpect(model().attribute("groups", hasItem(
                        allOf(
                                hasProperty("name", is("Group 1"))
                        )
                )))
                .andExpect(model().attribute("groups", hasItem(
                        allOf(
                                hasProperty("name", is("Group 2"))
                        )
                )));

        verify(groupService).findAll(null);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void indexShouldAddGroupsToModelAndRenderIndexViewWherePreviousPageIsActive() throws Exception {

        GroupResponse first = new GroupResponse();
        GroupResponse second = new GroupResponse();
        first.setName("Group 1");
        second.setName("Group 2");;
        List<GroupResponse> groups = Arrays.asList(first, second);
        when(groupService.findAll("2")).thenReturn(groups);

        mockMvc.perform(get("/groups/?page=2"))
                .andExpect(status().is(200))
                .andExpect(view().name("/groups/index"))
                .andExpect(forwardedUrl("/groups/index"))
                .andExpect(model().attribute("previousPageStatus", is("page-item")))
                .andExpect(model().attribute("groups", hasSize(2)))
                .andExpect(model().attribute("groups", is(groups)));

        verify(groupService).findAll("2");
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void showShouldAddGroupToModelAndRenderShowView() throws Exception {

        GroupResponse group = new GroupResponse();
        group.setName("Group 1");

        when(groupService.findById(1L)).thenReturn(Optional.of(group));

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("/groups/show"))
                .andExpect(forwardedUrl("/groups/show"))
                .andExpect(model().attribute("group", is(group)));


        verify(groupService).findById(1L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void newShouldGetGroupFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/groups/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("/groups/add"))
                .andExpect(forwardedUrl("/groups/add"));
    }

    @Test
    void editShouldAddGroupToModelAndRenderShowEdit() throws Exception {
        DepartmentResponse department1 = new DepartmentResponse();
        DepartmentResponse department2 = new DepartmentResponse();
        department1.setName("Dep 1");
        department2.setName("Dep 2");
        List<DepartmentResponse> allDepartments = new ArrayList<>();
        allDepartments.add(department1);
        allDepartments.add(department2);
        List<DepartmentResponse> anotherDepartments = new ArrayList<>();
        anotherDepartments.add(department2);

        FormOfEducationResponse formOfEducation1 = new FormOfEducationResponse();
        FormOfEducationResponse formOfEducation2 = new FormOfEducationResponse();
        formOfEducation1.setName("Form 1");
        formOfEducation1.setName("Form 2");
        List<FormOfEducationResponse> allFormsOfEducation = new ArrayList<>();
        allFormsOfEducation.add(formOfEducation1);
        allFormsOfEducation.add(formOfEducation2);
        List<FormOfEducationResponse> anotherFormsOfEducation = new ArrayList<>();
        anotherFormsOfEducation.add(formOfEducation2);

        StudentResponse student1 = new StudentResponse();
        StudentResponse student2 = new StudentResponse();
        student1.setFirstName("Alex");
        student2.setFirstName("Bob");
        List<StudentResponse> allStudents = new ArrayList<>();
        allStudents.add(student1);
        allStudents.add(student2);
        List<StudentResponse> studentsAnotherGroups = new ArrayList<>();
        studentsAnotherGroups.add(student2);
        List<StudentResponse> studentsCurrentGroup = new ArrayList<>();
        studentsCurrentGroup.add(student1);

        GroupResponse group = new GroupResponse();
        group.setId(1L);
        group.setName("Group 1");
        group.setDepartmentResponse(department1);
        group.setFormOfEducationResponse(formOfEducation1);

        when(groupService.findById(1L)).thenReturn(Optional.of(group));
        when(departmentService.findAll()).thenReturn(allDepartments);
        when(formOfEducationService.findAll()).thenReturn(allFormsOfEducation);
        when(studentService.findAll()).thenReturn(allStudents);
        when(studentService.findByGroupId(1L)).thenReturn(studentsCurrentGroup);

        mockMvc.perform(get("/groups/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/groups/edit"))
                .andExpect(forwardedUrl("/groups/edit"))
                .andExpect(model().attribute("group", is(group)))
                .andExpect(model().attribute("anotherDepartments", is(anotherDepartments)))
                .andExpect(model().attribute("anotherFormsOfEducation", is(anotherFormsOfEducation)))
                .andExpect(model().attribute("studentsAnotherGroups", is(studentsAnotherGroups)))
                .andExpect(model().attribute("studentsCurrentGroup", is(studentsCurrentGroup)));

        verify(groupService).findById(1L);
        verify(departmentService).findAll();
        verify(formOfEducationService).findAll();
        verify(studentService).findAll();
        verify(studentService).findByGroupId(1L);
        verifyNoMoreInteractions(groupService);
        verifyNoMoreInteractions(departmentService);
        verifyNoMoreInteractions(formOfEducationService);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void createShouldGetGroupFromModelRenderIndexView() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName("Group 1");
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");

        when(groupService.register(groupRequest)).thenReturn(groupResponse);

        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", groupResponse.getId().toString())
                .param("name", groupResponse.getName())
                .param("formOfEducationId", "0")
                .param("departmentId", "0")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"))
                .andExpect(redirectedUrl("/groups?formOfEducationId=0&departmentId=0"))
                .andExpect(model().attribute("group", hasProperty("id", is(groupResponse.getId()))))
                .andExpect(model().attribute("group", hasProperty("name",is(groupResponse.getName()))))
                .andExpect(model().attribute("formOfEducationId", is(0L)))
                .andExpect(model().attribute("departmentId", is(0L)));

        verify(groupService).register(groupRequest);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void createShouldGetGroupFromModelAndFormOfEducationIdAndDepartmentIdFromUrlAndRenderIndexView() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName("Group 1");
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");

        when(groupService.register(groupRequest)).thenReturn(groupResponse);
        doNothing().when(groupService).changeFormOfEducation(1L, 1);
        doNothing().when(groupService).changeDepartment(1L, 1);

        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", groupResponse.getId().toString())
                .param("name", groupResponse.getName())
                .param("formOfEducationId", "1")
                .param("departmentId", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"))
                .andExpect(redirectedUrl("/groups?formOfEducationId=1&departmentId=1"))
                .andExpect(model().attribute("group", hasProperty("id", is(groupResponse.getId()))))
                .andExpect(model().attribute("group", hasProperty("name",is(groupResponse.getName()))))
                .andExpect(model().attribute("formOfEducationId", is(1L)))
                .andExpect(model().attribute("departmentId", is(1L)));

        verify(groupService).register(groupRequest);
        verify(groupService).changeFormOfEducation(1L, 1);
        verify(groupService).changeDepartment(1L, 1);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void updateShouldGetGroupFromModelAndRenderIndexView() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName("Group 1");
        doNothing().when(groupService).edit(groupRequest);

        mockMvc.perform(patch("/groups/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", groupRequest.getId().toString())
                .param("name", groupRequest.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"))
                .andExpect(redirectedUrl("/groups"))
                .andExpect(model().attribute("group", hasProperty("id", is(groupRequest.getId()))))
                .andExpect(model().attribute("group", hasProperty("name",is(groupRequest.getName()))));

        verify(groupService).edit(groupRequest);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void changeDepartmentShouldGetGroupIdFromUrlAndDepartmentIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(groupService).changeDepartment(1L, 2);

        mockMvc.perform(post("/groups/1/changeDepartment")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewDepartment", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups/1/edit"))
                .andExpect(redirectedUrl("/groups/1/edit?idNewDepartment=2"))
                .andExpect(model().attribute("idNewDepartment", is(2L)));

        verify(groupService).changeDepartment(1L, 2);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void changeFormOfEducationShouldGetGroupIdFromUrlAndFormOfEducationIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(groupService).changeFormOfEducation(1L, 2);

        mockMvc.perform(post("/groups/1/changeFormOfEducation")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewFormOfEducation", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups/1/edit"))
                .andExpect(redirectedUrl("/groups/1/edit?idNewFormOfEducation=2"))
                .andExpect(model().attribute("idNewFormOfEducation", is(2L)));

        verify(groupService).changeFormOfEducation(1L, 2);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void addStudentToGroupShouldGetGroupIdFromUrlAndStudentIdFromModelAndRenderEditView() throws Exception {

        when(studentService.enterGroup(2, 1)).thenReturn(true);

        mockMvc.perform(post("/groups/1/addStudentToGroup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewStudent", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups/1/edit"))
                .andExpect(redirectedUrl("/groups/1/edit?idNewStudent=2"))
                .andExpect(model().attribute("idNewStudent", is(2L)));

        verify(studentService).enterGroup(2, 1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void removeStudentFromGroupShouldGetGroupIdFromUrlAndStudentIdFromModelAndRenderEditView() throws Exception {

        when(studentService.leaveGroup(2)).thenReturn(true);

        mockMvc.perform(post("/groups/1/removeStudentFromGroup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idRemovingStudent", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups/1/edit"))
                .andExpect(redirectedUrl("/groups/1/edit?idRemovingStudent=2"))
                .andExpect(model().attribute("idRemovingStudent", is(2L)));

        verify(studentService).leaveGroup(2);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void deleteShouldGetGroupIdFromUrlAndRenderIndexView() throws Exception {

        when(groupService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/groups/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/groups"))
                .andExpect(redirectedUrl("/groups"));

        verify(groupService).deleteById(1L);
        verifyNoMoreInteractions(groupService);
    }

}
