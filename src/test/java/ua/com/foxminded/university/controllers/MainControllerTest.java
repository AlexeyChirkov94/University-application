package ua.com.foxminded.university.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.TestsContextConfiguration;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.StudentService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestsContextConfiguration.class})
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    StudentService studentService;

    @Autowired
    GroupService groupService;

    MockMvc mockMvc;

    MainController mainController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mainController = webApplicationContext.getBean(MainController.class);
    }

    @Test
    void indexShouldRenderFeaturesView() throws Exception {

        mockMvc.perform(get("/features"))
                .andExpect(status().is(200))
                .andExpect(view().name("featuresView"));
    }

    @Test
    void indexShouldRenderAboutView() throws Exception {

        mockMvc.perform(get("/about"))
                .andExpect(status().is(200))
                .andExpect(view().name("aboutView"));
    }

    @Test
    void indexShouldRenderRegistrationView() throws Exception {

        mockMvc.perform(get("/registration"))
                .andExpect(status().is(200))
                .andExpect(view().name("registrationView"));
    }

    @Test
    void indexShouldRenderLoginView() throws Exception {

        mockMvc.perform(get("/login"))
                .andExpect(status().is(200))
                .andExpect(view().name("loginView"));
    }

}
