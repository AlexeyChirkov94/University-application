package ua.com.foxminded.university.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.interfaces.FormOfEducationService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
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
@ContextConfiguration(classes = {TestsContextConfiguration.class})
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class FormsOfEducationControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    FormOfEducationService formOfEducationService;

    @Autowired
    GroupService groupService;

    MockMvc mockMvc;

    FormsOfEducationController formsOfEducationController;

    @BeforeEach
    void setUp() {
        Mockito.reset(formOfEducationService);
        Mockito.reset(groupService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        formsOfEducationController = webApplicationContext.getBean(FormsOfEducationController.class);
    }

    @Test
    void showAllShouldAddFormsOfEducationToModelAndRenderIndexView() throws Exception {
        FormOfEducationResponse first = new FormOfEducationResponse();
        FormOfEducationResponse second = new FormOfEducationResponse();
        first.setId(1L);
        second.setId(2L);
        List<FormOfEducationResponse> formsOfEducationResponse = Arrays.asList(first, second);
        when(formOfEducationService.findAll(null)).thenReturn(formsOfEducationResponse);

        mockMvc.perform(get("/education/form"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfEducation/all"))
                .andExpect(forwardedUrl("/formOfEducation/all"))
                .andExpect(model().attribute("formsOfEducation", hasSize(2)))
                .andExpect(model().attribute("formsOfEducation", is(formsOfEducationResponse)));

        verify(formOfEducationService).findAll(null);
        verifyNoMoreInteractions(formOfEducationService);
    }

    @Test
    void showShouldAddFormOfEducationToModelAndRenderShowView() throws Exception {
        FormOfEducationResponse formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId(1L);
        formOfEducationResponse.setName("full-time");
        GroupResponse groupResponse1 = new GroupResponse();
        GroupResponse groupResponse2 = new GroupResponse();
        List<GroupResponse> formOfEducationGroups = Arrays.asList(groupResponse1, groupResponse2);

        when(formOfEducationService.findById(1L)).thenReturn(formOfEducationResponse);
        when(groupService.findByFormOfEducation(1L)).thenReturn(formOfEducationGroups);

        mockMvc.perform(get("/education/form/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfEducation/show"))
                .andExpect(forwardedUrl("/formOfEducation/show"))
                .andExpect(model().attribute("formOfEducation", is(formOfEducationResponse)))
                .andExpect(model().attribute("formOfEducationGroups", is(formOfEducationGroups)));

        verify(formOfEducationService).findById(1L);
        verifyNoMoreInteractions(formOfEducationService);
    }

    @Test
    void newShouldGetFormOfEducationFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/education/form/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfEducation/add"))
                .andExpect(forwardedUrl("/formOfEducation/add"));
    }

    @Test
    void editShouldAddFormOfEducationToModelAndRenderEditView() throws Exception {
        FormOfEducationResponse formOfEducationResponse = new FormOfEducationResponse();
        formOfEducationResponse.setId(1L);
        formOfEducationResponse.setName("full-time");
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(formOfEducationResponse.getId());
        formOfEducationRequest.setName(formOfEducationResponse.getName());
        GroupResponse groupResponse1 = new GroupResponse();
        groupResponse1.setId(1L);
        GroupResponse groupResponse2 = new GroupResponse();
        groupResponse2.setId(2L);
        List<GroupResponse> formOfEducationGroups = new ArrayList<>();
        formOfEducationGroups.add(groupResponse1);
        List<GroupResponse> notFormOfEducationGroups = new ArrayList<>();
        notFormOfEducationGroups.add(groupResponse2);
        List<GroupResponse> allGroups = new ArrayList<>();
        allGroups.add(groupResponse1);
        allGroups.add(groupResponse2);

        when(formOfEducationService.findById(1L)).thenReturn(formOfEducationResponse);
        when(groupService.findAll()).thenReturn(allGroups);
        when(groupService.findByFormOfEducation(1L)).thenReturn(formOfEducationGroups);

        mockMvc.perform(get("/education/form/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfEducation/edit"))
                .andExpect(forwardedUrl("/formOfEducation/edit"))
                .andExpect(model().attribute("formOfEducationRequest", is(formOfEducationRequest)))
                .andExpect(model().attribute("formOfEducationGroups", is(formOfEducationGroups)))
                .andExpect(model().attribute("notFormOfEducationGroups", is(notFormOfEducationGroups)));

        verify(formOfEducationService).findById(1L);
        verify(groupService).findAll();
        verify(groupService).findByFormOfEducation(1L);
        verifyNoMoreInteractions(formOfEducationService);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void createShouldGetFormOfEducationFromModelAndRenderIndexView() throws Exception {
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(1L);
        formOfEducationRequest.setName("full-time");

        FormOfEducationResponse formOfEducationResponse = new FormOfEducationResponse();

        when(formOfEducationService.create(formOfEducationRequest)).thenReturn(formOfEducationResponse);

        mockMvc.perform(post("/education/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", formOfEducationRequest.getId().toString())
                .param("name", formOfEducationRequest.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/education/form"))
                .andExpect(redirectedUrl("/education/form"))
                .andExpect(model().attribute("formOfEducation", hasProperty("id", is(formOfEducationRequest.getId()))))
                .andExpect(model().attribute("formOfEducation", hasProperty("name", is(formOfEducationRequest.getName()))));

        verify(formOfEducationService).create(formOfEducationRequest);
        verifyNoMoreInteractions(formOfEducationService);
    }

    @Test
    void updateShouldGetFormOfEducationFromModelAndRenderIndexView() throws Exception {
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(1L);
        formOfEducationRequest.setName("full-time");

        doNothing().when(formOfEducationService).edit(formOfEducationRequest);

        mockMvc.perform(patch("/education/form/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", formOfEducationRequest.getId().toString())
                .param("name", formOfEducationRequest.getName())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/education/form"))
                .andExpect(redirectedUrl("/education/form"))
                .andExpect(model().attribute("formOfEducationRequest", hasProperty("id", is(formOfEducationRequest.getId()))))
                .andExpect(model().attribute("formOfEducationRequest", hasProperty("name", is(formOfEducationRequest.getName()))));

        verify(formOfEducationService).edit(formOfEducationRequest);
        verifyNoMoreInteractions(formOfEducationService);
    }

    @Test
    void deleteShouldGetFormOfEducationIdFromUrlAndRenderIndexView() throws Exception {

        when(formOfEducationService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/education/form/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/education/form"))
                .andExpect(redirectedUrl("/education/form"));

        verify(formOfEducationService).deleteById(1L);
        verifyNoMoreInteractions(formOfEducationService);
    }

    @Test
    void addGroupFromFormOfEducationShouldGetFromFormOfEducationIdIdFromUrlAndGroupIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(groupService).changeFormOfEducation(2L, 1L);

        mockMvc.perform(post("/education/form/1/assign/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idNewGroup", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/education/form/1/edit"))
                .andExpect(redirectedUrl("/education/form/1/edit?idNewGroup=2"))
                .andExpect(model().attribute("idNewGroup", is(2L)));

        verify(groupService).changeFormOfEducation(2L, 1L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void removeGroupFromFormOfEducationShouldGetFromFormOfEducationIdIdFromUrlAndGroupIdFromModelAndRenderEditView() throws Exception {

        doNothing().when(groupService).removeFormOfEducationFromGroup(2L);

        mockMvc.perform(post("/education/form/1/remove/group")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("idRemovingGroup", "2")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/education/form/1/edit"))
                .andExpect(redirectedUrl("/education/form/1/edit?idRemovingGroup=2"))
                .andExpect(model().attribute("idRemovingGroup", is(2L)));

        verify(groupService).removeFormOfEducationFromGroup(2L);
        verifyNoMoreInteractions(groupService);
    }

    @Test
    void entityDoNotExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        Exception exception = new EntityDontExistException("Form Of Education with id = 15 not exist");

        when(formOfEducationService.findById(15L)).thenThrow(exception);

        mockMvc.perform(get("/education/form/15")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity not exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(formOfEducationService).findById(15L);
        verifyNoMoreInteractions(formOfEducationService);
    }

    @Test
    void entityAlreadyExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setName("lecture");

        Exception exception = new EntityAlreadyExistException("Form Of Education with same name already exist");

        when(formOfEducationService.create(formOfEducationRequest)).thenThrow(exception);

        mockMvc.perform(post("/education/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", formOfEducationRequest.getName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity already exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(formOfEducationService).create(formOfEducationRequest);
        verifyNoMoreInteractions(formOfEducationService);
    }

}
