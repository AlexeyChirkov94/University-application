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
import ua.com.foxminded.university.dto.ProfessorRequest;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.dto.ScienceDegreeResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.CourseService;
import ua.com.foxminded.university.service.interfaces.ProfessorService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/professor")
public class ProfessorsController {

    private final ProfessorService professorService;
    private final CourseService courseService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("professors", professorService.findAll(page));

        return "/professor/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("professor", professorService.findById(id).get());
        model.addAttribute("professorsCourses", courseService.findByProfessorId(id));
        return "/professor/show";
    }

    @GetMapping("/new")
    public String newProfessor(@ModelAttribute("professor") ProfessorRequest professorRequest) {
        return "/professor/add";
    }

    @PostMapping()
    public String create(@ModelAttribute("professor") ProfessorRequest professorRequest) {
        professorService.register(professorRequest);
        return "redirect:/professor";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        List<CourseResponse> professorsCourses = courseService.findByProfessorId(id);
        List<CourseResponse> anotherCourses = courseService.findAll();
        anotherCourses.removeAll(professorsCourses);
        ProfessorResponse professor = professorService.findById(id).get();

        model.addAttribute("professor", professor);
        model.addAttribute("ScienceDegree", professor.getScienceDegreeResponse());
        model.addAttribute("ScienceDegrees", ScienceDegreeResponse.values());
        model.addAttribute("professorsCourses", professorsCourses);
        model.addAttribute("anotherCourses", anotherCourses);
        return "/professor/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("professor") ProfessorRequest professorRequest) {
        professorService.edit(professorRequest);
        return "redirect:/professor";
    }

    @PostMapping("/{id}/changeScienceDegree")
    public String changeScienceDegree(Model model, @PathVariable("id") long professorId, @RequestParam Integer idNewScienceDegree) {
        model.addAttribute("idNewScienceDegree", idNewScienceDegree);
        professorService.changeScienceDegree(professorId, idNewScienceDegree);
        return "redirect:/professor/" + professorId + "/edit";
    }

    @PostMapping("/{id}/addCourse")
    public String addCourse(Model model, @PathVariable("id") long professorId, @RequestParam Integer idOfAddingCourse) {
        model.addAttribute("idOfAddingCourse", idOfAddingCourse);
        courseService.addCourseToProfessorCourseList(idOfAddingCourse, professorId);
        return "redirect:/professor/" + professorId + "/edit";
    }

    @PostMapping("/{id}/removeCourse")
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

    @ExceptionHandler(ValidateException.class)
    public ModelAndView validateProfessorException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/common creating error");

        return modelAndView;
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ModelAndView entityAlreadyExistException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/common creating error");

        return modelAndView;
    }

}
