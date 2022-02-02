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
import ua.com.foxminded.university.dto.LessonResponse;
import ua.com.foxminded.university.dto.StudentRequest;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.exception.TimeTableStudentWithoutGroupException;
import ua.com.foxminded.university.service.exception.ValidateException;
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
@RequestMapping("/student")
public class StudentsController {

    private final StudentService studentService;
    private final GroupService groupService;
    private final LessonService lessonService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("students", studentService.findAll(page));

        return "student/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("student", studentService.findById(id));
        return "student/show";
    }

    @GetMapping("/new")
    public String newStudent(Model model, @ModelAttribute("student") StudentRequest studentRequest) {

        model.addAttribute("groups", groupService.findAll());
        return "student/add";
    }

    @PostMapping()
    public String create(@ModelAttribute("student") StudentRequest studentRequest) {

        studentService.register(studentRequest);
        return "redirect:/student";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        StudentResponse studentResponse = studentService.findById(id);
        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setFirstName(studentResponse.getFirstName());
        studentRequest.setLastName(studentResponse.getLastName());
        studentRequest.setEmail(studentResponse.getEmail());

        model.addAttribute("studentResponse", studentResponse);
        model.addAttribute("studentRequest", studentRequest);
        model.addAttribute("groups", groupService.findAll());
        return "student/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") StudentRequest studentRequest) {
        studentService.edit(studentRequest);
        return "redirect:/student";
    }


    @GetMapping("/{id}/timetable")
    public String timetable(Model model, @PathVariable("id") long id) {
        List<LessonResponse> lessons = lessonService.formTimeTableForStudent(id);
        Map<Long, String> stringDateTimes = getStringDateTimes(lessons);

        model.addAttribute("student", studentService.findById(id));
        model.addAttribute("lessons", lessons);
        model.addAttribute("stringDateTimes", stringDateTimes);
        return "student/timetable";
    }

    @PatchMapping("/{id}/remove/group")
    public String leaveGroup(@PathVariable("id") long studentId) {
        studentService.leaveGroup(studentId);
        return "redirect:/student/" + studentId + "/edit";
    }

    @PostMapping("/{id}/assign/group")
    public String enterGroup(Model model, @PathVariable("id") long studentId, @RequestParam Integer idNewGroup) {
        model.addAttribute("idNewGroup", idNewGroup);
        studentService.changeGroup(studentId, idNewGroup);
        return "redirect:/student/" + studentId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        studentService.deleteById(id);
        return "redirect:/student";
    }

    @ExceptionHandler(TimeTableStudentWithoutGroupException.class)
    public ModelAndView timetableException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/timetable show error");

        return modelAndView;
    }

    @ExceptionHandler(ValidateException.class)
    public ModelAndView validateStudentException(HttpServletRequest request, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", ex);
        modelAndView.setViewName("errors handling/common creating error");

        return modelAndView;
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
