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
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.FormOfEducationService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.StudentService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
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
import static ua.com.foxminded.university.controllers.ControllersUtility.getStringDateTimes;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestsContextConfiguration.class})
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

    @Autowired
    LessonService lessonService;

    MockMvc mockMvc;

    GroupsController groupsController;

    @BeforeEach
    void setUp() {
        Mockito.reset(groupService);
        Mockito.reset(formOfEducationService);
        Mockito.reset(departmentService);
        Mockito.reset(studentService);
        Mockito.reset(lessonService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        groupsController = webApplicationContext.getBean(GroupsController.class);
    }

    @Test
    void showAllShouldAddGroupsToModelAndRenderIndexView() throws Exception {

        GroupResponse first = new GroupResponse();
        GroupResponse second = new GroupResponse();
        first.setName("Group 1");
        second.setName("Group 2");;
        when(groupService.findAll(null)).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/group"))
                .andExpect(status().is(200))
                .andExpect(view().name("group/all"))
                .andExpect(forwardedUrl("group/all"))
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
    void showShouldAddGroupToModelAndRenderShowView() throws Exception {

        GroupResponse group = new GroupResponse();
        group.setName("Group 1");

        when(groupService.findById(1L)).thenReturn(group);

        mockMvc.perform(get("/group/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("group/show"))
                .andExpect(forwardedUrl("group/show"))
                .andExpect(model().attribute("group", is(group)));


        verify(groupService).findById(1L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void newShouldGetGroupFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/group/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("group/add"))
                .andExpect(forwardedUrl("group/add"));
    }

    @Test
    void editShouldAddGroupToModelAndRenderShowEdit() throws Exception {
        DepartmentResponse department1 = new DepartmentResponse();
        DepartmentResponse department2 = new DepartmentResponse();
        department1.setName("Dep 1");
        department2.setName("Dep 2");
        List<DepartmentResponse> departments = new ArrayList<>();
        departments.add(department1);
        departments.add(department2);

        FormOfEducationResponse formOfEducation1 = new FormOfEducationResponse();
        FormOfEducationResponse formOfEducation2 = new FormOfEducationResponse();
        formOfEducation1.setName("Form 1");
        formOfEducation1.setName("Form 2");
        List<FormOfEducationResponse> formsOfEducation = new ArrayList<>();
        formsOfEducation.add(formOfEducation1);
        formsOfEducation.add(formOfEducation2);

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

        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setId(1L);
        groupResponse.setName("Group 1");
        groupResponse.setDepartmentResponse(department1);
        groupResponse.setFormOfEducationResponse(formOfEducation1);
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupResponse.getName());

        when(groupService.findById(1L)).thenReturn(groupResponse);
        when(departmentService.findAll()).thenReturn(departments);
        when(formOfEducationService.findAll()).thenReturn(formsOfEducation);
        when(studentService.findAll()).thenReturn(allStudents);
        when(studentService.findByGroupId(1L)).thenReturn(studentsCurrentGroup);

        mockMvc.perform(get("/group/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("group/edit"))
                .andExpect(forwardedUrl("group/edit"))
                .andExpect(model().attribute("groupResponse", is(groupResponse)))
                .andExpect(model().attribute("groupRequest", is(groupRequest)))
                .andExpect(model().attribute("departments", is(departments)))
                .andExpect(model().attribute("formsOfEducation", is(formsOfEducation)))
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

        when(groupService.create(groupRequest)).thenReturn(groupResponse);

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", groupResponse.getId().toString())
                .param("name", groupResponse.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/group"))
                .andExpect(redirectedUrl("/group"))
                .andExpect(model().attribute("group", hasProperty("id", is(groupResponse.getId()))))
                .andExpect(model().attribute("group", hasProperty("name",is(groupResponse.getName()))));

        verify(groupService).create(groupRequest);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void updateShouldGetGroupFromModelAndRenderIndexView() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setId(1L);
        groupRequest.setName("Group 1");
        doNothing().when(groupService).edit(groupRequest);

        mockMvc.perform(patch("/group/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", groupRequest.getId().toString())
                .param("name", groupRequest.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/group"))
                .andExpect(redirectedUrl("/group"))
                .andExpect(model().attribute("group", hasProperty("id", is(groupRequest.getId()))))
                .andExpect(model().attribute("group", hasProperty("name",is(groupRequest.getName()))));

        verify(groupService).edit(groupRequest);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void timetableShouldAddLessonsToModelAndRenderTimetableView() throws Exception {
        GroupResponse group = new GroupResponse();
        group.setId(1L);
        group.setName("group");
        LessonResponse lesson1 = new LessonResponse();
        LessonResponse lesson2 = new LessonResponse();
        lesson1.setId(1L);
        lesson2.setId(2L);
        List<LessonResponse> lessons = Arrays.asList(lesson1, lesson2);

        Map<Long, String> stringDateTimes = getStringDateTimes(lessons);

        when(groupService.findById(1L)).thenReturn(group);
        when(lessonService.formTimeTableForGroup(1L)).thenReturn(lessons);

        mockMvc.perform(get("/group/1/timetable"))
                .andExpect(status().is(200))
                .andExpect(view().name("group/timetable"))
                .andExpect(forwardedUrl("group/timetable"))
                .andExpect(model().attribute("group", is(group)))
                .andExpect(model().attribute("lessons", is(lessons)))
                .andExpect(model().attribute("stringDateTimes", is(stringDateTimes)));

        verify(groupService).findById(1L);
        verify(lessonService).formTimeTableForGroup(1L);
        verifyNoMoreInteractions(groupService);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void addStudentToGroupShouldGetGroupIdFromUrlAndStudentIdFromModelAndRenderEditView() throws Exception {

        when(studentService.changeGroup(2, 1)).thenReturn(true);

        mockMvc.perform(post("/group/1/add/student")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewStudent", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/group/1/edit"))
                .andExpect(redirectedUrl("/group/1/edit?idNewStudent=2"))
                .andExpect(model().attribute("idNewStudent", is(2L)));

        verify(studentService).changeGroup(2, 1);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void removeStudentFromGroupShouldGetGroupIdFromUrlAndStudentIdFromModelAndRenderEditView() throws Exception {

        when(studentService.leaveGroup(2)).thenReturn(true);

        mockMvc.perform(post("/group/1/remove/student")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idRemovingStudent", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/group/1/edit"))
                .andExpect(redirectedUrl("/group/1/edit?idRemovingStudent=2"))
                .andExpect(model().attribute("idRemovingStudent", is(2L)));

        verify(studentService).leaveGroup(2);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void deleteShouldGetGroupIdFromUrlAndRenderIndexView() throws Exception {

        doNothing().when(groupService).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/group/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/group"))
                .andExpect(redirectedUrl("/group"));

        verify(groupService).deleteById(1L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void entityDoNotExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        Exception exception = new EntityDontExistException("Group with id = 15 not exist");

        when(groupService.findById(15L)).thenThrow(exception);

        mockMvc.perform(get("/group/15")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity not exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(groupService).findById(15L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void entityAlreadyExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName("Group1");

        Exception exception = new EntityAlreadyExistException("Group with same name already exist");

        when(groupService.create(groupRequest)).thenThrow(exception);

        mockMvc.perform(post("/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", groupRequest.getName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity already exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(groupService).create(groupRequest);
        verifyNoMoreInteractions(groupService);
    }

}
