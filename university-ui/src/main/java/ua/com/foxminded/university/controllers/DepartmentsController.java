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
import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.ProfessorResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.CourseService;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.ProfessorService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/department")
public class DepartmentsController {

    private final DepartmentService departmentService;
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final GroupService groupService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("departments", departmentService.findAll(page));

        return "department/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("department", departmentService.findById(id));
        model.addAttribute("professors", professorService.findByDepartmentId(id));
        model.addAttribute("courses", courseService.findByDepartmentId(id));
        model.addAttribute("groups", groupService.findByDepartmentId(id));
        return "department/show";
    }

    @GetMapping("/new")
    public String newCourse(Model model, @ModelAttribute("department") DepartmentRequest departmentRequest) {
        return "department/add";
    }

    @PostMapping()
    public String create(@ModelAttribute("department") @Valid DepartmentRequest departmentRequest,
            BindingResult bindingResult) {

        if(bindingResult.hasErrors()) return "department/add";

        departmentService.create(departmentRequest);
        return "redirect:/department";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {

        partiallyPrepareModelToEditView(model, id);

        DepartmentResponse departmentResponse = departmentService.findById(id);
        DepartmentRequest departmentRequest = new DepartmentRequest();
        departmentRequest.setId(departmentResponse.getId());
        departmentRequest.setName(departmentResponse.getName());
        model.addAttribute("departmentRequest", departmentRequest);

        return "department/edit";
    }

    @PatchMapping("/{id}")
    public String update(Model model, @ModelAttribute("departmentRequest") @Valid DepartmentRequest departmentRequest,
                         BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            partiallyPrepareModelToEditView(model, departmentRequest.getId());
            return "department/edit";
        }

        departmentService.edit(departmentRequest);
        return "redirect:/department";
    }

    @PostMapping("/{id}/assign/professor")
    public String addProfessorToDepartment(Model model, @PathVariable("id") long departmentId, @RequestParam long idNewProfessor) {
        model.addAttribute("idNewProfessor", idNewProfessor);
        professorService.changeDepartment(idNewProfessor, departmentId);
        return "redirect:/department/" + departmentId + "/edit";
    }

    @PostMapping("/{id}/remove/professor")
    public String removeProfessorFromDepartment(Model model, @PathVariable("id") long departmentId, @RequestParam long idRemovingProfessor) {
        model.addAttribute("idRemovingProfessor", idRemovingProfessor);
        professorService.removeDepartmentFromProfessor(idRemovingProfessor);
        return "redirect:/department/" + departmentId + "/edit";
    }

    @PostMapping("/{id}/assign/course")
    public String addCourseToDepartment(Model model, @PathVariable("id") long departmentId, @RequestParam long idNewCourse) {
        model.addAttribute("idNewCourse", idNewCourse);
        courseService.changeDepartment(idNewCourse, departmentId);
        return "redirect:/department/" + departmentId + "/edit";
    }

    @PostMapping("/{id}/remove/course")
    public String removeCourseFromDepartment(Model model, @PathVariable("id") long departmentId, @RequestParam long idRemovingCourse) {
        model.addAttribute("idRemovingCourse", idRemovingCourse);
        courseService.removeDepartmentFromCourse(idRemovingCourse);
        return "redirect:/department/" + departmentId + "/edit";
    }

    @PostMapping("/{id}/assign/group")
    public String addGroupToDepartment(Model model, @PathVariable("id") long departmentId, @RequestParam long idNewGroup) {
        model.addAttribute("idNewGroup", idNewGroup);
        groupService.changeDepartment(idNewGroup, departmentId);
        return "redirect:/department/" + departmentId + "/edit";
    }

    @PostMapping("/{id}/remove/group")
    public String removeGroupFromDepartment(Model model, @PathVariable("id") long departmentId, @RequestParam long idRemovingGroup) {
        model.addAttribute("idRemovingGroup", idRemovingGroup);
        groupService.removeDepartmentFromGroup(idRemovingGroup);
        return "redirect:/department/" + departmentId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        departmentService.deleteById(id);
        return "redirect:/department";
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

    private void partiallyPrepareModelToEditView(Model model, long id) {

        List<ProfessorResponse> departmentProfessors = professorService.findByDepartmentId(id);
        List<ProfessorResponse> anotherProfessors = professorService.findAll();
        anotherProfessors.removeAll(departmentProfessors);

        List<CourseResponse> departmentCourses = courseService.findByDepartmentId(id);
        List<CourseResponse> anotherCourses = courseService.findAll();
        anotherCourses.removeAll(departmentCourses);

        List<GroupResponse> departmentGroups = groupService.findByDepartmentId(id);
        List<GroupResponse> anotherGroups = groupService.findAll();
        anotherGroups.removeAll(departmentGroups);

        model.addAttribute("departmentProfessors", departmentProfessors);
        model.addAttribute("anotherProfessors", anotherProfessors);
        model.addAttribute("departmentCourses", departmentCourses);
        model.addAttribute("anotherCourses", anotherCourses);
        model.addAttribute("departmentGroups", departmentGroups);
        model.addAttribute("anotherGroups", anotherGroups);
    }

}
