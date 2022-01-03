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
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.DepartmentService;
import ua.com.foxminded.university.service.FormOfEducationService;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.LessonService;
import ua.com.foxminded.university.service.StudentService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static ua.com.foxminded.university.controllers.ControllersUtility.getStringDateTimes;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/group")
public class GroupsController {

    private final GroupService groupService;
    private final FormOfEducationService formOfEducationService;
    private final DepartmentService departmentService;
    private final StudentService studentService;
    private final LessonService lessonService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("groups", groupService.findAll(page));

        return "/group/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("group", groupService.findById(id));
        model.addAttribute("studentsOfGroup", studentService.findByGroupId(id));
        return "/group/show";
    }

    @GetMapping("/new")
    public String newGroup(Model model, @ModelAttribute("group") GroupRequest groupRequest) {

        model.addAttribute("formsOfEducation", formOfEducationService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "/group/add";
    }

    @PostMapping()
    public String create(@ModelAttribute("group") GroupRequest groupRequest) {
        groupService.create(groupRequest);
        return "redirect:/group";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        GroupResponse groupResponse = groupService.findById(id);
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setName(groupResponse.getName());

        List<DepartmentResponse> departments = departmentService.findAll();
        List<FormOfEducationResponse> formsOfEducation = formOfEducationService.findAll();

        List<StudentResponse> studentsAnotherGroups = studentService.findAll();
        List<StudentResponse> studentsCurrentGroup = studentService.findByGroupId(id);
        studentsAnotherGroups.removeAll(studentsCurrentGroup);

        model.addAttribute("studentsAnotherGroups", studentsAnotherGroups);
        model.addAttribute("studentsCurrentGroup", studentsCurrentGroup);
        model.addAttribute("formsOfEducation", formsOfEducation);
        model.addAttribute("departments", departments);
        model.addAttribute("groupResponse", groupResponse);
        model.addAttribute("groupRequest", groupRequest);

        return "/group/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") GroupRequest groupRequest) {
        groupService.edit(groupRequest);
        return "redirect:/group";
    }

    @GetMapping("/{id}/timetable")
    public String timetable(Model model, @PathVariable("id") long id) {
        List<LessonResponse> lessons = lessonService.formTimeTableForGroup(id);
        Map<Long, String> stringDateTimes = getStringDateTimes(lessons);

        model.addAttribute("group", groupService.findById(id));
        model.addAttribute("lessons", lessons);
        model.addAttribute("stringDateTimes", stringDateTimes);
        return "/group/timetable";
    }

    @PostMapping("/{id}/add/student")
    public String addStudentToGroup(Model model, @PathVariable("id") long groupId, @RequestParam long idNewStudent) {
        model.addAttribute("idNewStudent", idNewStudent);
        studentService.changeGroup(idNewStudent, groupId);
        return "redirect:/group/" + groupId + "/edit";
    }

    @PostMapping("/{id}/remove/student")
    public String removeStudentFromGroup(Model model, @PathVariable("id") long groupId, @RequestParam long idRemovingStudent) {
        model.addAttribute("idRemovingStudent", idRemovingStudent);
        studentService.leaveGroup(idRemovingStudent);
        return "redirect:/group/" + groupId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        groupService.deleteById(id);
        return "redirect:/group";
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

}
