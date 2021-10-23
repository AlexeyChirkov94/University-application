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
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.StudentService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.times;
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
class StudentsControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    StudentService studentService;

    @Autowired
    GroupService groupService;

    MockMvc mockMvc;

    StudentsController studentsController;

    @BeforeEach
    void setUp() {
        Mockito.reset(studentService);
        Mockito.reset(groupService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        studentsController = webApplicationContext.getBean(StudentsController.class);
    }

    @Test
    void showAllShouldAddStudentsToModelAndRenderIndexView() throws Exception {

        StudentResponse first = new StudentResponse();
        StudentResponse second = new StudentResponse();
        first.setFirstName("Alexey");
        first.setLastName("Chirkov");
        second.setFirstName("Bob");
        second.setLastName("Stivenson");
        when(studentService.findAll(null)).thenReturn(Arrays.asList(first, second));

        mockMvc.perform(get("/student"))
                .andExpect(status().is(200))
                .andExpect(view().name("/student/all"))
                .andExpect(forwardedUrl("/student/all"))
                .andExpect(model().attribute("students", hasSize(2)))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("firstName", is("Alexey")),
                                hasProperty("lastName", is("Chirkov"))
                        )
                )))
                .andExpect(model().attribute("students", hasItem(
                        allOf(
                                hasProperty("firstName", is("Bob")),
                                hasProperty("lastName", is("Stivenson"))
                        )
                )));

        verify(studentService).findAll(null);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void showShouldAddStudentToModelAndRenderShowView() throws Exception {
        GroupResponse group = new GroupResponse();
        group.setId(1L);
        group.setName("Group 1");

        StudentResponse student = new StudentResponse();
        student.setId(1L);
        student.setFirstName("Alexey");
        student.setLastName("Chirkov");
        student.setEmail("chirkov@gamil.com");
        student.setPassword("1234");
        student.setGroupResponse(group);
        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/student/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("/student/show"))
                .andExpect(forwardedUrl("/student/show"))
                .andExpect(model().attribute("student", is(student)));


        verify(studentService).findById(1L);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void newShouldGetStudentFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/student/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("/student/add"))
                .andExpect(forwardedUrl("/student/add"));
    }

    @Test
    void editIfStudentHaveAGroupShouldAddStudentToModelAndRenderShowEdit() throws Exception {
        GroupResponse group1 = new GroupResponse();
        group1.setId(1L);
        group1.setName("Group 1");
        GroupResponse group2 = new GroupResponse();
        group2.setId(2L);
        group2.setName("Group 2");
        List<GroupResponse> allGroups = new ArrayList<>();
        allGroups.add(group1);
        allGroups.add(group2);
        List<GroupResponse> anotherGroups = new ArrayList<>();
        anotherGroups.add(group2);

        StudentResponse student = new StudentResponse();
        student.setId(1L);
        student.setFirstName("Alexey");
        student.setLastName("Chirkov");
        student.setEmail("chirkov@gamil.com");
        student.setPassword("1234");
        student.setGroupResponse(group1);

        when(studentService.findById(1L)).thenReturn(Optional.of(student));
        when(groupService.findById(1L)).thenReturn(Optional.of(group1));
        when(groupService.findAll()).thenReturn(allGroups);

        mockMvc.perform(get("/student/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/student/edit"))
                .andExpect(forwardedUrl("/student/edit"))
                .andExpect(model().attribute("student", is(student)))
                .andExpect(model().attribute("studentGroup", is(group1)))
                .andExpect(model().attribute("anotherGroups", is(anotherGroups)));

        verify(studentService).findById(1L);
        verify(groupService).findById(1L);
        verify(groupService).findAll();
        verifyNoMoreInteractions(studentService);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void editIfStudentHaveNoGroupShouldAddStudentToModelAndRenderShowEdit() throws Exception {
        GroupResponse studentGroup = new GroupResponse();
        studentGroup = new GroupResponse();
        studentGroup.setId(0L);
        studentGroup.setName("not appointed");
        GroupResponse group1 = new GroupResponse();
        group1.setId(1L);
        group1.setName("Group 1");
        GroupResponse group2 = new GroupResponse();
        group2.setId(2L);
        group2.setName("Group 2");
        List<GroupResponse> allGroups = new ArrayList<>();
        allGroups.add(group1);
        allGroups.add(group2);
        StudentResponse student = new StudentResponse();
        student.setId(1L);
        student.setFirstName("Alexey");
        student.setLastName("Chirkov");
        student.setEmail("chirkov@gamil.com");
        student.setPassword("1234");
        student.setGroupResponse(studentGroup);


        when(studentService.findById(1L)).thenReturn(Optional.of(student));
        when(groupService.findById(1L)).thenReturn(Optional.of(group1));
        when(groupService.findAll()).thenReturn(allGroups);

        mockMvc.perform(get("/student/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/student/edit"))
                .andExpect(forwardedUrl("/student/edit"))
                .andExpect(model().attribute("student", is(student)))
                .andExpect(model().attribute("studentGroup", is(studentGroup)))
                .andExpect(model().attribute("anotherGroups", is(allGroups)));

        verify(studentService).findById(1L);
        verify(groupService).findAll();
        verifyNoMoreInteractions(studentService);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void createShouldGetStudentFromModelAndRenderIndexView() throws Exception {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1L);
        studentRequest.setFirstName("Alex");
        studentRequest.setLastName("Chirkov");
        StudentResponse studentResponse = new StudentResponse();
        studentResponse.setId(1L);
        studentResponse.setFirstName("Alex");
        studentResponse.setLastName("Chirkov");

        when(studentService.register(studentRequest)).thenReturn(studentResponse);

        mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", studentResponse.getId().toString())
                .param("firstName", studentResponse.getFirstName())
                .param("lastName", studentResponse.getLastName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/student"))
                .andExpect(redirectedUrl("/student"))
                .andExpect(model().attribute("student", hasProperty("id", is(studentResponse.getId()))))
                .andExpect(model().attribute("student", hasProperty("firstName",is(studentResponse.getFirstName()))))
                .andExpect(model().attribute("student", hasProperty("lastName",is(studentResponse.getLastName()))));

        verify(studentService).register(studentRequest);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void updateShouldGetStudentFromModelAndRenderIndexView() throws Exception {
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setId(1L);
        studentRequest.setFirstName("Alex");
        studentRequest.setLastName("Chirkov");

        doNothing().when(studentService).edit(studentRequest);

        mockMvc.perform(patch("/student/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", studentRequest.getId().toString())
                .param("firstName", studentRequest.getFirstName())
                .param("lastName", studentRequest.getLastName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/student"))
                .andExpect(redirectedUrl("/student"))
                .andExpect(model().attribute("student", hasProperty("id", is(studentRequest.getId()))))
                .andExpect(model().attribute("student", hasProperty("firstName",is(studentRequest.getFirstName()))))
                .andExpect(model().attribute("student", hasProperty("lastName",is(studentRequest.getLastName()))));

        verify(studentService).edit(studentRequest);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void leaveGroupShouldGetStudentIdFromUrlAndGroupIdFromModelAndRenderEditView() throws Exception {

        when(studentService.leaveGroup(1L)).thenReturn(true);

        mockMvc.perform(patch("/student/1/leaveGroup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/student/1/edit"))
                .andExpect(redirectedUrl("/student/1/edit"));

        verify(studentService).leaveGroup(1L);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void enterGroupShouldGetStudentIdFromUrlAndGroupIdFromModelAndRenderEditView() throws Exception {

        when(studentService.enterGroup(1L, 2L)).thenReturn(true);

        mockMvc.perform(post("/student/1/enterGroup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewGroup", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/student/1/edit"))
                .andExpect(redirectedUrl("/student/1/edit?idNewGroup=2"))
                .andExpect(model().attribute("idNewGroup", is(2)));

        verify(studentService).enterGroup(1L, 2L);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void deleteShouldGetStudentIdFromUrlAndRenderIndexView() throws Exception {

        when(studentService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/student"))
                .andExpect(redirectedUrl("/student"));

        verify(studentService).deleteById(1L);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void validateStudentShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        StudentRequest notValidStudent = new StudentRequest();
        notValidStudent.setFirstName("Alex");
        Exception exception = new ValidateException("password don`t match the pattern");

        when(studentService.register(notValidStudent)).thenThrow(exception);

        mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", notValidStudent.getFirstName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/common creating error"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(studentService).register(notValidStudent);
        verifyNoMoreInteractions(studentService);
    }

    @Test
    void entityAlreadyExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        StudentRequest repeatableStudent = new StudentRequest();
        repeatableStudent.setFirstName("Alex");
        Exception exception = new EntityAlreadyExistException("Student with same email already exist");

        when(studentService.register(repeatableStudent)).thenThrow(exception);

        mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", repeatableStudent.getFirstName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/common creating error"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(studentService).register(repeatableStudent);
        verifyNoMoreInteractions(studentService);
    }

}