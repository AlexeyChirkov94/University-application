package ua.com.foxminded.university.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.foxminded.university.dto.DepartmentResponse;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupRequest;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.dto.StudentResponse;
import ua.com.foxminded.university.service.interfaces.DepartmentService;
import ua.com.foxminded.university.service.interfaces.FormOfEducationService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import ua.com.foxminded.university.service.interfaces.StudentService;
import java.util.List;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/groups")
public class GroupsController {

    GroupService groupService;
    FormOfEducationService formOfEducationService;
    DepartmentService departmentService;
    StudentService studentService;

    @GetMapping()
    public String index(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("groups", groupService.findAll(page));

        return "/groups/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){
        model.addAttribute("group", groupService.findById(id).get());
        model.addAttribute("studentsOfGroup", studentService.findByGroupId(id));
        return "/groups/show";
    }

    @GetMapping("/new")
    public String newGroup(Model model, @ModelAttribute("group") GroupRequest groupRequest) {

        model.addAttribute("formsOfEducation", formOfEducationService.findAll());
        model.addAttribute("departments", departmentService.findAll());
        return "/groups/add";
    }

    @PostMapping()
    public String create(Model model, @ModelAttribute("group") GroupRequest groupRequest, @RequestParam long formOfEducationId,
                         @RequestParam long departmentId) {
        model.addAttribute("formOfEducationId", formOfEducationId);
        model.addAttribute("departmentId", departmentId);

        GroupResponse groupResponse = groupService.register(groupRequest);

        if(formOfEducationId != 0L){
            groupService.changeFormOfEducation(groupResponse.getId(), formOfEducationId);
        }
        if(departmentId != 0){
            groupService.changeDepartment(groupResponse.getId(), departmentId);
        }

        return "redirect:/groups";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        GroupResponse group = groupService.findById(id).get();
        List<DepartmentResponse> anotherDepartments = departmentService.findAll();
        anotherDepartments.remove(group.getDepartmentResponse());

        List<FormOfEducationResponse> anotherFormOfEducation = formOfEducationService.findAll();
        anotherFormOfEducation.remove(group.getFormOfEducationResponse());

        List<StudentResponse> studentsAnotherGroups = studentService.findAll();
        List<StudentResponse> studentsCurrentGroup = studentService.findByGroupId(id);
        studentsAnotherGroups.removeAll(studentsCurrentGroup);

        model.addAttribute("studentsAnotherGroups", studentsAnotherGroups);
        model.addAttribute("studentsCurrentGroup", studentsCurrentGroup);
        model.addAttribute("anotherFormsOfEducation", anotherFormOfEducation);
        model.addAttribute("anotherDepartments", anotherDepartments);
        model.addAttribute("group", group);

        return "/groups/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") GroupRequest groupRequest) {
        groupService.edit(groupRequest);
        return "redirect:/groups";
    }

    @PostMapping("/{id}/changeDepartment")
    public String changeDepartment(Model model, @PathVariable("id") long groupId, @RequestParam long idNewDepartment) {
        model.addAttribute("idNewDepartment", idNewDepartment);
        groupService.changeDepartment(groupId, idNewDepartment);
        return "redirect:/groups/" + groupId + "/edit";
    }

    @PostMapping("/{id}/changeFormOfEducation")
    public String changeFormOfEducation(Model model, @PathVariable("id") long groupId, @RequestParam long idNewFormOfEducation) {
        model.addAttribute("idNewFormOfEducation", idNewFormOfEducation);
        groupService.changeFormOfEducation(groupId, idNewFormOfEducation);
        return "redirect:/groups/" + groupId + "/edit";
    }

    @PostMapping("/{id}/addStudentToGroup")
    public String addStudentToGroup(Model model, @PathVariable("id") long groupId, @RequestParam long idNewStudent) {
        model.addAttribute("idNewStudent", idNewStudent);
        studentService.enterGroup(idNewStudent, groupId);
        return "redirect:/groups/" + groupId + "/edit";
    }

    @PostMapping("/{id}/removeStudentFromGroup")
    public String removeStudentFromGroup(Model model, @PathVariable("id") long groupId, @RequestParam long idRemovingStudent) {
        model.addAttribute("idRemovingStudent", idRemovingStudent);
        studentService.leaveGroup(idRemovingStudent);
        return "redirect:/groups/" + groupId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        groupService.deleteById(id);
        return "redirect:/groups";
    }

}
