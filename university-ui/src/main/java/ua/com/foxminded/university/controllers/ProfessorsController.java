package ua.com.foxminded.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.ProfessorService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import static ua.com.foxminded.university.controllers.ControllersUtility.getStringDateTimes;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/professor")
public class ProfessorsController {

    private final ProfessorService professorService;
    private final CourseService courseService;
    private final DepartmentService departmentService;
    private final LessonService lessonService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("professors", professorService.findAll(page));

        return "professor/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("professor", professorService.findById(id));
        model.addAttribute("professorsCourses", courseService.findByProfessorId(id));
        return "professor/show";
    }

    @GetMapping("/new")
    public String newProfessor(Model model, @ModelAttribute("professorRequest") ProfessorRequest professorRequest) {

        model.addAttribute("scienceDegrees", ScienceDegreeResponse.values());
        model.addAttribute("departments", departmentService.findAll());
        return "professor/add";
    }

    @PostMapping()
    public String create(Model model, @ModelAttribute("professorRequest") @Valid ProfessorRequest professorRequest,
                         BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("scienceDegrees", ScienceDegreeResponse.values());
            model.addAttribute("departments", departmentService.findAll());
            return "professor/add";
        }

        professorService.register(professorRequest);
        return "redirect:/professor";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        ProfessorResponse professorResponse = professorService.findById(id);
        ProfessorRequest professorRequest = new ProfessorRequest();
        professorRequest.setFirstName(professorResponse.getFirstName());
        professorRequest.setLastName(professorResponse.getLastName());
        professorRequest.setEmail(professorResponse.getEmail());

        List<CourseResponse> professorsCourses = courseService.findByProfessorId(id);
        List<CourseResponse> anotherCourses = courseService.findAll();
        anotherCourses.removeAll(professorsCourses);

        model.addAttribute("professorResponse", professorResponse);
        model.addAttribute("professorRequest", professorRequest);
        model.addAttribute("ScienceDegrees", ScienceDegreeResponse.values());
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("professorsCourses", professorsCourses);
        model.addAttribute("anotherCourses", anotherCourses);
        return "professor/edit";
    }

    @PatchMapping("/{id}")
    public String update(Model model, @ModelAttribute("professorRequest") @Valid ProfessorRequest professorRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            ProfessorResponse professorResponse = professorService.findById(professorRequest.getId());
            List<CourseResponse> professorsCourses = courseService.findByProfessorId(professorRequest.getId());
            List<CourseResponse> anotherCourses = courseService.findAll();
            anotherCourses.removeAll(professorsCourses);

            model.addAttribute("professorResponse", professorResponse);
            model.addAttribute("ScienceDegrees", ScienceDegreeResponse.values());
            model.addAttribute("departments", departmentService.findAll());
            model.addAttribute("professorsCourses", professorsCourses);
            model.addAttribute("anotherCourses", anotherCourses);
            return "professor/edit";
        }

        professorService.edit(professorRequest);
        return "redirect:/professor";
    }

    @GetMapping("/{id}/timetable")
    public String timetable(Model model, @PathVariable("id") long id) {
        List<LessonResponse> lessons = lessonService.formTimeTableForProfessor(id);
        Map<Long, String> stringDateTimes = getStringDateTimes(lessons);

        model.addAttribute("professor", professorService.findById(id));
        model.addAttribute("lessons", lessons);
        model.addAttribute("stringDateTimes", stringDateTimes);
        return "professor/timetable";
    }

    @PostMapping("/{id}/assign/course")
    public String addCourse(Model model, @PathVariable("id") long professorId, @RequestParam Integer idOfAddingCourse) {
        model.addAttribute("idOfAddingCourse", idOfAddingCourse);
        courseService.addCourseToProfessorCourseList(idOfAddingCourse, professorId);
        return "redirect:/professor/" + professorId + "/edit";
    }

    @PostMapping("/{id}/remove/course")
    public String removeCourse(Model model, @PathVariable("id") long professorId, @RequestParam Integer idOfRemovingCourse) {
        model.addAttribute("idOfRemovingCourse", idOfRemovingCourse);
        courseService.removeCourseFromProfessorCourseList(idOfRemovingCourse, professorId);
        return "redirect:/professor/" + professorId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        professorService.deleteById(id);
        return "redirect:/professor";
    }

    @ExceptionHandler(EntityDontExistException.class)
    public ModelAndView entityDontExistException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/entity not exist");

        return modelAndView;
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ModelAndView entityAlreadyExistException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/entity already exist");

        return modelAndView;
    }

}
