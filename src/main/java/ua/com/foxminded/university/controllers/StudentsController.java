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
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.ValidateException;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.StudentService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/student")
public class StudentsController {

    private final StudentService studentService;
    private final GroupService groupService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("students", studentService.findAll(page));

        return "/student/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("student", studentService.findById(id).get());
        return "/student/show";
    }

    @GetMapping("/new")
    public String newStudent(@ModelAttribute("student") StudentRequest studentRequest) {
        return "/student/add";
    }

    @PostMapping()
    public String create(@ModelAttribute("student") StudentRequest studentRequest) {

        studentService.register(studentRequest);
        return "redirect:/student";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        StudentResponse student = studentService.findById(id).get();
        long studentGroupId = student.getGroupResponse().getId();
        GroupResponse studentGroup;

        if(studentGroupId == 0){
            studentGroup = new GroupResponse();
            studentGroup.setId(0L);
            studentGroup.setName("not appointed");
        } else {
            studentGroup = groupService.findById(studentGroupId).get(); //was like
        }

        List<GroupResponse> anotherGroups = groupService.findAll();
        anotherGroups.remove(studentGroup);

        model.addAttribute("student", student);
        model.addAttribute("studentGroup", studentGroup);
        model.addAttribute("anotherGroups", anotherGroups);
        return "/student/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") StudentRequest studentRequest) {
                studentService.edit(studentRequest);
        return "redirect:/student";
    }

    @PatchMapping("/{id}/leaveGroup")
    public String leaveGroup(@PathVariable("id") long studentId) {
        studentService.leaveGroup(studentId);
        return "redirect:/student/" + studentId + "/edit";
    }

    @PostMapping("/{id}/enterGroup")
    public String enterGroup(Model model, @PathVariable("id") long studentId, @RequestParam Integer idNewGroup) {
        model.addAttribute("idNewGroup", idNewGroup);
        studentService.enterGroup(studentId, idNewGroup);
        return "redirect:/student/" + studentId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        studentService.deleteById(id);
        return "redirect:/student";
    }

    @ExceptionHandler(ValidateException.class)
    public ModelAndView validateStudentException(HttpServletRequest request, Exception ex) {
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
