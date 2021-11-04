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
import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.exception.IncompatibilityCourseAndProfessorException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.CourseService;
import ua.com.foxminded.university.service.interfaces.FormOfLessonService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.LessonService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
class LessonsControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    LessonService lessonService;

    @Autowired
    ProfessorService professorService;

    @Autowired
    GroupService groupService;

    @Autowired
    FormOfLessonService formOfLessonService;

    @Autowired
    CourseService courseService;

    MockMvc mockMvc;

    LessonsController lessonsController;

    @BeforeEach
    void setUp() {
        Mockito.reset(lessonService);
        Mockito.reset(professorService);
        Mockito.reset(groupService);
        Mockito.reset(formOfLessonService);
        Mockito.reset(courseService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        lessonsController = webApplicationContext.getBean(LessonsController.class);
    }

    @Test
    void showAllShouldAddLessonsToModelAndRenderIndexView() throws Exception {
        LessonResponse first = new LessonResponse();
        LessonResponse second = new LessonResponse();
        first.setId(1L);
        second.setId(2L);
        List<LessonResponse> lessons = Arrays.asList(first, second);
        when(lessonService.findAll(null)).thenReturn(lessons);

        mockMvc.perform(get("/lesson"))
                .andExpect(status().is(200))
                .andExpect(view().name("/lesson/all"))
                .andExpect(forwardedUrl("/lesson/all"))
                .andExpect(model().attribute("lessons", hasSize(2)))
                .andExpect(model().attribute("lessons", is(lessons)));

        verify(lessonService).findAll(null);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void showShouldAddLessonToModelAndRenderShowView() throws Exception {
        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setId(1L);
        lessonResponse.setTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 00, 00));

        when(lessonService.findById(1L)).thenReturn(lessonResponse);

        mockMvc.perform(get("/lesson/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("/lesson/show"))
                .andExpect(forwardedUrl("/lesson/show"))
                .andExpect(model().attribute("lesson", is(lessonResponse)));

        verify(lessonService).findById(1L);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void newShouldGetLessonFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/lesson/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("/lesson/add"))
                .andExpect(forwardedUrl("/lesson/add"));
    }

    @Test
    void editShouldAddLessonToModelAndRenderShowEditLessonHaveNoProfessorAndCourse() throws Exception {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setName("not chosen");
        List<CourseResponse> availableCourses = Arrays.asList(courseResponse);
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setLastName("not chosen");
        List<ProfessorResponse> availableProfessors = Arrays.asList(professorResponse);
        GroupResponse groupResponse = new GroupResponse();
        List<GroupResponse> allGroups = Arrays.asList(groupResponse);
        FormOfLessonResponse formOfLessonResponse = new FormOfLessonResponse();
        List<FormOfLessonResponse> allFormsOfLesson = Arrays.asList(formOfLessonResponse);

        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 10, 00));
        lessonResponse.setCourseResponse(courseResponse);
        lessonResponse.setTeacher(professorResponse);
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setTimeOfStartLesson(lessonResponse.getTimeOfStartLesson());

        when(lessonService.findById(1L)).thenReturn(lessonResponse);
        when(groupService.findAll()).thenReturn(allGroups);
        when(formOfLessonService.findAll()).thenReturn(allFormsOfLesson);
        when(professorService.findAll()).thenReturn(availableProfessors);
        when(courseService.findAll()).thenReturn(availableCourses);

        mockMvc.perform(get("/lesson/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/lesson/edit"))
                .andExpect(forwardedUrl("/lesson/edit"))
                .andExpect(model().attribute("lessonResponse", is(lessonResponse)))
                .andExpect(model().attribute("lessonRequest", is(lessonRequest)))
                .andExpect(model().attribute("allGroups", is(allGroups)))
                .andExpect(model().attribute("allFormsOfLesson", is(allFormsOfLesson)))
                .andExpect(model().attribute("availableTeachers", is(availableProfessors)))
                .andExpect(model().attribute("availableCourses", is(availableCourses)));

        verify(lessonService).findById(1L);
        verify(groupService).findAll();
        verify(formOfLessonService).findAll();
        verify(professorService).findAll();
        verify(courseService).findAll();
        verifyNoMoreInteractions(lessonService);
        verifyNoMoreInteractions(groupService);
        verifyNoMoreInteractions(formOfLessonService);
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void editShouldAddLessonToModelAndRenderShowEditLessonHaveProfessorAndCourse() throws Exception {
        CourseResponse courseResponse = new CourseResponse();
        courseResponse.setId(1L);
        courseResponse.setName("Math");
        List<CourseResponse> availableCourses = Arrays.asList(courseResponse);
        ProfessorResponse professorResponse = new ProfessorResponse();
        professorResponse.setId(1L);
        professorResponse.setLastName("Jonson");
        List<ProfessorResponse> availableProfessors = Arrays.asList(professorResponse);
        GroupResponse groupResponse = new GroupResponse();
        List<GroupResponse> allGroups = Arrays.asList(groupResponse);
        FormOfLessonResponse formOfLessonResponse = new FormOfLessonResponse();
        List<FormOfLessonResponse> allFormsOfLesson = Arrays.asList(formOfLessonResponse);

        LessonResponse lessonResponse = new LessonResponse();
        lessonResponse.setTimeOfStartLesson(LocalDateTime.of(2020, 1, 10, 10, 10, 00));
        lessonResponse.setCourseResponse(courseResponse);
        lessonResponse.setTeacher(professorResponse);
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setTimeOfStartLesson(lessonResponse.getTimeOfStartLesson());

        when(lessonService.findById(1L)).thenReturn(lessonResponse);
        when(groupService.findAll()).thenReturn(allGroups);
        when(formOfLessonService.findAll()).thenReturn(allFormsOfLesson);
        when(professorService.findByCourseId(1L)).thenReturn(availableProfessors);
        when(courseService.findByProfessorId(1L)).thenReturn(availableCourses);

        mockMvc.perform(get("/lesson/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/lesson/edit"))
                .andExpect(forwardedUrl("/lesson/edit"))
                .andExpect(model().attribute("lessonResponse", is(lessonResponse)))
                .andExpect(model().attribute("lessonRequest", is(lessonRequest)))
                .andExpect(model().attribute("allGroups", is(allGroups)))
                .andExpect(model().attribute("allFormsOfLesson", is(allFormsOfLesson)))
                .andExpect(model().attribute("availableTeachers", is(availableProfessors)))
                .andExpect(model().attribute("availableCourses", is(availableCourses)));

        verify(lessonService).findById(1L);
        verify(groupService).findAll();
        verify(formOfLessonService).findAll();
        verify(professorService).findByCourseId(1L);
        verify(courseService).findByProfessorId(1L);
        verifyNoMoreInteractions(lessonService);
        verifyNoMoreInteractions(groupService);
        verifyNoMoreInteractions(formOfLessonService);
        verifyNoMoreInteractions(professorService);
        verifyNoMoreInteractions(courseService);
    }

    @Test
    void createShouldGetLessonFromModelAndRenderIndexView() throws Exception {
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setCourseId(2L);
        lessonRequest.setTeacherId(3L);
        lessonRequest.setGroupId(4L);
        lessonRequest.setFormOfLessonId(5L);

        LessonResponse lessonResponse = new LessonResponse();

        when(lessonService.create(lessonRequest)).thenReturn(lessonResponse);

        mockMvc.perform(post("/lesson")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", lessonRequest.getId().toString())
                .param("courseId", lessonRequest.getCourseId().toString())
                .param("groupId", lessonRequest.getGroupId().toString())
                .param("teacherId", lessonRequest.getTeacherId().toString())
                .param("formOfLessonId", lessonRequest.getFormOfLessonId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lesson"))
                .andExpect(redirectedUrl("/lesson"))
                .andExpect(model().attribute("lesson", hasProperty("id", is(lessonRequest.getId()))))
                .andExpect(model().attribute("lesson", hasProperty("courseId", is(lessonRequest.getCourseId()))))
                .andExpect(model().attribute("lesson", hasProperty("groupId", is(lessonRequest.getGroupId()))))
                .andExpect(model().attribute("lesson", hasProperty("teacherId", is(lessonRequest.getTeacherId()))))
                .andExpect(model().attribute("lesson", hasProperty("formOfLessonId", is(lessonRequest.getFormOfLessonId()))));

        verify(lessonService).create(lessonRequest);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void updateShouldGetLessonFromModelAndRenderIndexView() throws Exception {
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setId(1L);
        lessonRequest.setCourseId(2L);
        lessonRequest.setTeacherId(3L);
        lessonRequest.setGroupId(4L);
        lessonRequest.setFormOfLessonId(5L);

        doNothing().when(lessonService).edit(lessonRequest);

        mockMvc.perform(patch("/lesson/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", lessonRequest.getId().toString())
                .param("courseId", lessonRequest.getCourseId().toString())
                .param("groupId", lessonRequest.getGroupId().toString())
                .param("teacherId", lessonRequest.getTeacherId().toString())
                .param("formOfLessonId", lessonRequest.getFormOfLessonId().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lesson"))
                .andExpect(redirectedUrl("/lesson"))
                .andExpect(model().attribute("lesson", hasProperty("id", is(lessonRequest.getId()))))
                .andExpect(model().attribute("lesson", hasProperty("courseId", is(lessonRequest.getCourseId()))))
                .andExpect(model().attribute("lesson", hasProperty("groupId", is(lessonRequest.getGroupId()))))
                .andExpect(model().attribute("lesson", hasProperty("teacherId", is(lessonRequest.getTeacherId()))))
                .andExpect(model().attribute("lesson", hasProperty("formOfLessonId", is(lessonRequest.getFormOfLessonId()))));

        verify(lessonService).edit(lessonRequest);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void deleteShouldGetLessonIdFromUrlAndRenderIndexView() throws Exception {

        when(lessonService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/lesson/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lesson"))
                .andExpect(redirectedUrl("/lesson"));

        verify(lessonService).deleteById(1L);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void validateLessonCheckTimeTablesShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        LessonRequest notValidLesson = new LessonRequest();
        notValidLesson.setId(1L);
        Exception exception = new ValidateException("Lesson can`t be appointed on this time, cause time of lesson cross timetable of professor");

        when(lessonService.create(notValidLesson)).thenThrow(exception);

        mockMvc.perform(post("/lesson")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", notValidLesson.getId().toString())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/common creating error"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(lessonService).create(notValidLesson);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void validateLessonCheckIncompatibilityCourseAndProfessorShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        LessonRequest notValidLesson = new LessonRequest();
        notValidLesson.setId(1L);
        Exception exception = new IncompatibilityCourseAndProfessorException("Selected professor can`t teach this course");

        when(lessonService.create(notValidLesson)).thenThrow(exception);

        mockMvc.perform(post("/lesson")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", notValidLesson.getId().toString())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/lesson creating error incompatibility course and professor "))
                .andExpect(model().attribute("exception", is(exception)));

        verify(lessonService).create(notValidLesson);
        verifyNoMoreInteractions(lessonService);
    }


    @Test
    void showAllShouldAddFormsOfLessonToModelAndRenderIndexView() throws Exception {
        FormOfLessonResponse first = new FormOfLessonResponse();
        FormOfLessonResponse second = new FormOfLessonResponse();
        first.setId(1L);
        second.setId(2L);
        List<FormOfLessonResponse> formsOfLessonResponse = Arrays.asList(first, second);
        when(formOfLessonService.findAll(null)).thenReturn(formsOfLessonResponse);

        mockMvc.perform(get("/lesson/type"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfLesson/all"))
                .andExpect(forwardedUrl("/formOfLesson/all"))
                .andExpect(model().attribute("formsOfLesson", hasSize(2)))
                .andExpect(model().attribute("formsOfLesson", is(formsOfLessonResponse)));

        verify(formOfLessonService).findAll(null);
        verifyNoMoreInteractions(formOfLessonService);
    }

    @Test
    void showShouldAddFormOfLessonToModelAndRenderShowView() throws Exception {
        FormOfLessonResponse formOfLessonResponse = new FormOfLessonResponse();
        formOfLessonResponse.setId(1L);
        formOfLessonResponse.setName("lecture");

        when(formOfLessonService.findById(1L)).thenReturn(formOfLessonResponse);

        mockMvc.perform(get("/lesson/type/1"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfLesson/show"))
                .andExpect(forwardedUrl("/formOfLesson/show"))
                .andExpect(model().attribute("formOfLesson", is(formOfLessonResponse)));

        verify(formOfLessonService).findById(1L);
        verifyNoMoreInteractions(formOfLessonService);
    }

    @Test
    void newShouldGetFormOfLessonFromModelAndRenderNewView() throws Exception {

        mockMvc.perform(get("/lesson/type/new"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfLesson/add"))
                .andExpect(forwardedUrl("/formOfLesson/add"));
    }

    @Test
    void editShouldAddFormOfLessonToModelAndRenderEditView() throws Exception {
        FormOfLessonResponse formOfLessonResponse = new FormOfLessonResponse();
        formOfLessonResponse.setId(1L);
        formOfLessonResponse.setName("lecture");
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId(formOfLessonResponse.getId());
        formOfLessonRequest.setName(formOfLessonResponse.getName());

        when(formOfLessonService.findById(1L)).thenReturn(formOfLessonResponse);

        mockMvc.perform(get("/lesson/type/1/edit"))
                .andExpect(status().is(200))
                .andExpect(view().name("/formOfLesson/edit"))
                .andExpect(forwardedUrl("/formOfLesson/edit"))
                .andExpect(model().attribute("formOfLessonRequest", is(formOfLessonRequest)));

        verify(formOfLessonService).findById(1L);
        verifyNoMoreInteractions(formOfLessonService);
    }

    @Test
    void createShouldGetFormOfLessonFromModelAndRenderIndexView() throws Exception {
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId(1L);
        formOfLessonRequest.setName("lecture");
        formOfLessonRequest.setDuration(60);

        FormOfLessonResponse formOfLessonResponse = new FormOfLessonResponse();

        when(formOfLessonService.create(formOfLessonRequest)).thenReturn(formOfLessonResponse);

        mockMvc.perform(post("/lesson/type")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", formOfLessonRequest.getId().toString())
                .param("name", formOfLessonRequest.getName())
                .param("duration", formOfLessonRequest.getDuration().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lesson/type"))
                .andExpect(redirectedUrl("/lesson/type"))
                .andExpect(model().attribute("formOfLesson", hasProperty("id", is(formOfLessonRequest.getId()))))
                .andExpect(model().attribute("formOfLesson", hasProperty("name", is(formOfLessonRequest.getName()))))
                .andExpect(model().attribute("formOfLesson", hasProperty("duration", is(formOfLessonRequest.getDuration()))));

        verify(formOfLessonService).create(formOfLessonRequest);
        verifyNoMoreInteractions(formOfLessonService);
    }

    @Test
    void updateShouldGetFormOfLessonFromModelAndRenderIndexView() throws Exception {
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setId(1L);
        formOfLessonRequest.setName("lecture");
        formOfLessonRequest.setDuration(60);

        doNothing().when(formOfLessonService).edit(formOfLessonRequest);

        mockMvc.perform(patch("/lesson/type/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", formOfLessonRequest.getId().toString())
                .param("name", formOfLessonRequest.getName())
                .param("duration", formOfLessonRequest.getDuration().toString())
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lesson/type"))
                .andExpect(redirectedUrl("/lesson/type"))
                .andExpect(model().attribute("formOfLessonRequest", hasProperty("id", is(formOfLessonRequest.getId()))))
                .andExpect(model().attribute("formOfLessonRequest", hasProperty("name", is(formOfLessonRequest.getName()))))
                .andExpect(model().attribute("formOfLessonRequest", hasProperty("duration", is(formOfLessonRequest.getDuration()))));

        verify(formOfLessonService).edit(formOfLessonRequest);
        verifyNoMoreInteractions(formOfLessonService);
    }

    @Test
    void deleteShouldGetFormOfLessonIdFromUrlAndRenderIndexView() throws Exception {

        when(formOfLessonService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/lesson/type/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/lesson/type"))
                .andExpect(redirectedUrl("/lesson/type"));

        verify(formOfLessonService).deleteById(1L);
        verifyNoMoreInteractions(formOfLessonService);
    }

    @Test
    void entityDoNotExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        Exception exception = new EntityDontExistException("Lesson with id = 15 not exist");

        when(lessonService.findById(15L)).thenThrow(exception);

        mockMvc.perform(get("/lesson/15")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity not exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(lessonService).findById(15L);
        verifyNoMoreInteractions(lessonService);
    }

    @Test
    void entityAlreadyExistShouldGetExceptionFromModelAndRenderErrorView() throws Exception {
        FormOfLessonRequest formOfLessonRequest = new FormOfLessonRequest();
        formOfLessonRequest.setName("lecture");

        Exception exception = new EntityAlreadyExistException("Form of lesson with same name already exist");

        when(formOfLessonService.create(formOfLessonRequest)).thenThrow(exception);

        mockMvc.perform(post("/lesson/type")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", formOfLessonRequest.getName())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("errors handling/entity already exist"))
                .andExpect(model().attribute("exception", is(exception)));

        verify(formOfLessonService).create(formOfLessonRequest);
        verifyNoMoreInteractions(formOfLessonService);
    }

}
