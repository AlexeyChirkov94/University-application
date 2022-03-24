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
import ua.com.foxminded.university.dto.CourseRequest;
import ua.com.foxminded.university.dto.CourseResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.ProfessorService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/course")
public class CoursesController {

    private final CourseService courseService;
    private final ProfessorService professorService;
    private final DepartmentService departmentService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("courses", courseService.findAll(page));

        return "course/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("course", courseService.findById(id));
        model.addAttribute("teachersOfCourse", professorService.findByCourseId(id));

        return "course/show";
    }

    @GetMapping("/new")
    public String newCourse(Model model, @ModelAttribute("course") CourseRequest courseRequest) {

        model.addAttribute("departments", departmentService.findAll());
        return "course/add";
    }

    @PostMapping()
    public String create(Model model, @ModelAttribute("course") @Valid CourseRequest courseRequest, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "course/add";
        }

        courseService.create(courseRequest);
        return "redirect:/course";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {

        partiallyPrepareModelToEditView(model, id);

        CourseResponse courseResponse = courseService.findById(id);
        CourseRequest courseRequest = new CourseRequest();
        courseRequest.setName(courseResponse.getName());
        model.addAttribute("courseRequest", courseRequest);

        return "course/edit";
    }

    @PatchMapping("/{id}")
    public String update(Model model, @ModelAttribute("courseRequest") @Valid CourseRequest courseRequest,
                         BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            partiallyPrepareModelToEditView(model, courseRequest.getId());
            return "course/edit";
        }

        courseService.edit(courseRequest);
        return "redirect:/course";
    }

    @PostMapping("/{id}/assign/professor")
    public String addProfessorToCourse(Model model, @PathVariable("id") long courseId, @RequestParam long idNewProfessor) {
        model.addAttribute("idNewProfessor", idNewProfessor);
        courseService.addCourseToProfessorCourseList(courseId, idNewProfessor);
        return "redirect:/course/" + courseId + "/edit";
    }

    @PostMapping("/{id}/remove/professor")
    public String removeProfessorFromCourse(Model model, @PathVariable("id") long courseId, @RequestParam long idRemovingProfessor) {
        model.addAttribute("idRemovingProfessor", idRemovingProfessor);
        courseService.removeCourseFromProfessorCourseList(courseId, idRemovingProfessor);
        return "redirect:/course/" + courseId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        courseService.deleteById(id);
        return "redirect:/course";
    }

    @ExceptionHandler(EntityDontExistException.class)
    public ModelAndView entityDontExistException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/entity not exist");

        return modelAndView;
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ModelAndView entityAlreadyExistExceptionLesson(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/entity already exist");

        return modelAndView;
    }

    private void partiallyPrepareModelToEditView(Model model, long courseId) {

        CourseResponse courseResponse = courseService.findById(courseId);

        List<ProfessorResponse> notTeachersOfCourse = professorService.findAll();
        List<ProfessorResponse> teachersOfCourse = professorService.findByCourseId(courseId);
        notTeachersOfCourse.removeAll(teachersOfCourse);

        model.addAttribute("teachersOfCourse", teachersOfCourse);
        model.addAttribute("notTeachersOfCourse", notTeachersOfCourse);
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("courseResponse", courseResponse);
    }

}
