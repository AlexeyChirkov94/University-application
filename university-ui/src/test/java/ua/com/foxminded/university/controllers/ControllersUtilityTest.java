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
import ua.com.foxminded.university.ControllerTestsContextConfiguration;
import ua.com.foxminded.university.dto.LessonResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static ua.com.foxminded.university.controllers.ControllersUtility.getStringDateTimes;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ControllerTestsContextConfiguration.class})
@WebAppConfiguration
@ExtendWith(MockitoExtension.class)
class ControllersUtilityTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void setPagesValueAndStatusShouldSetPreviousPageStatusDisableIfPreviousPageLessThanOne() throws Exception  {

        mockMvc.perform(get("/group"))
            .andExpect(model().attribute("liPreviousPageLabelStatus", is("page-item disabled")))
            .andExpect(model().attribute("aPreviousPageLabelStatus", is("page-link")))
            .andExpect(model().attribute("liPreviousPageNumberStatus", is("invisible")))
            .andExpect(model().attribute("aPreviousPageNumberStatus", is("invisible")));
    }

    @Test
    void setPagesValueAndStatusShouldSetPreviousPageStatusDisableIfPreviousPageLessMoreOne() throws Exception  {

        mockMvc.perform(get("/group?page=2"))
                .andExpect(model().attribute("liPreviousPageLabelStatus", is("page-item")))
                .andExpect(model().attribute("aPreviousPageLabelStatus", is("page-link")))
                .andExpect(model().attribute("liPreviousPageNumberStatus", is("page-item")))
                .andExpect(model().attribute("aPreviousPageNumberStatus", is("page-link")));
    }

    @Test
    void getStringDateTimeShouldReturnMapWithLessonIdAndStringIfArgumentIsListOfLessonRequest() throws Exception  {
        LessonResponse first = new LessonResponse();
        LessonResponse second = new LessonResponse();
        LessonResponse third = new LessonResponse();
        first.setId(1L);
        first.setTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 10, 00));
        second.setId(2L);
        second.setTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 8, 8, 00));
        third.setId(3L);
        List<LessonResponse> lessons = Arrays.asList(first, second, third);

        Map<Long, String> actual = getStringDateTimes(lessons);
        Map<Long, String> expected = new HashMap<>();
        expected.put(1L, "2020 JANUARY 10 10:10");
        expected.put(2L, "2020 JANUARY 10 08:08");
        expected.put(3L, "");

        assertThat(actual).isEqualTo(expected);
    }

}
