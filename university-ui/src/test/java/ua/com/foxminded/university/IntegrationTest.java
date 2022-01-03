package ua.com.foxminded.university;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ua.com.foxminded.university.dto.DepartmentResponse;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {IntegrationTestsContextConfiguration.class})
@WebAppConfiguration
public class IntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void showAllShouldAddDepartmentsToModelAndRenderIndexView() throws Exception {
        DepartmentResponse first = new DepartmentResponse();
        DepartmentResponse second = new DepartmentResponse();
        first.setId(1L);
        first.setName("Department of History");
        second.setId(2L);
        second.setName("Department of Math");
        List<DepartmentResponse> departmentsResponse = Arrays.asList(first, second);

        mockMvc.perform(get("/department"))
                .andExpect(status().is(200))
                .andExpect(view().name("/department/all"))
                .andExpect(model().attribute("departments", hasSize(2)))
                .andExpect(model().attribute("departments", is(departmentsResponse)));
    }

}
