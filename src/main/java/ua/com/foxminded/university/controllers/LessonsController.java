package ua.com.foxminded.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonRequest;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.service.exception.IncompatibilityCourseAndProfessorException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.CourseService;
import ua.com.foxminded.university.service.interfaces.FormOfLessonService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.LessonService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import static ua.com.foxminded.university.controllers.ControllersUtility.getStringDateTime;
import static ua.com.foxminded.university.controllers.ControllersUtility.getStringDateTimes;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/lesson")
public class LessonsController {

    private final LessonService lessonService;
    private final ProfessorService professorService;
    private final GroupService groupService;
    private final FormOfLessonService formOfLessonService;
    private final CourseService courseService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        List<LessonResponse> lessons = lessonService.findAll(page);
        Map<Long, String> stringDateTimes = getStringDateTimes(lessons);

        model.addAttribute("lessons", lessons);
        model.addAttribute("stringDateTimes", stringDateTimes);

        return "/lesson/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        LessonResponse lesson = lessonService.findById(id).get();

        model.addAttribute("lesson", lesson);
        model.addAttribute("stringDateTimes", getStringDateTime(lesson));
        return "/lesson/show";
    }

    @GetMapping("/new")
    public String newLesson(Model model, @ModelAttribute("lesson") LessonRequest lessonRequest) {

        model.addAttribute("professors", professorService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("formsOfLesson", formOfLessonService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "/lesson/add";
    }


    @PostMapping()
    public String create(@ModelAttribute("lesson") LessonRequest lessonRequest) {

        lessonService.create(lessonRequest);
        return "redirect:/lesson";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        LessonResponse lessonResponse = lessonService.findById(id).get();
        LessonRequest lessonRequest = new LessonRequest();
        lessonRequest.setTimeOfStartLesson(lessonResponse.getTimeOfStartLesson());
        CourseResponse course = lessonResponse.getCourseResponse();
        List<GroupResponse> allGroups = groupService.findAll();
        List<FormOfLessonResponse> allFormsOfLesson = formOfLessonService.findAll();
        ProfessorResponse teacher = lessonResponse.getTeacher();
        List<ProfessorResponse> availableTeachers;
        List<CourseResponse> availableCourses;
        availableTeachers = !course.getName().equals("not chosen") ? professorService.findByCourseId(course.getId()) : professorService.findAll();
        availableCourses = !teacher.getLastName().equals("not chosen") ? courseService.findByProfessorId(teacher.getId()) : courseService.findAll();

        model.addAttribute("lessonResponse", lessonResponse);
        model.addAttribute("lessonRequest", lessonRequest);
        model.addAttribute("stringDateTime", getStringDateTime(lessonResponse));
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("allFormsOfLesson", allFormsOfLesson);
        model.addAttribute("availableTeachers", availableTeachers);
        model.addAttribute("availableCourses", availableCourses);

        return "/lesson/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("lesson") LessonRequest lessonRequest) {
        lessonService.edit(lessonRequest);
        return "redirect:/lesson";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        lessonService.deleteById(id);
        return "redirect:/lesson";
    }

    @ExceptionHandler(ValidateException.class)
    public ModelAndView incompatibilityTimeTablesException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/common creating error");

        return modelAndView;
    }

    @ExceptionHandler(IncompatibilityCourseAndProfessorException.class)
    public ModelAndView incompatibilityCourseAndProfessor(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/lesson creating error incompatibility course and professor ");

        return modelAndView;
    }

}
