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
import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.interfaces.FormOfEducationService;
import ua.com.foxminded.university.service.interfaces.GroupService;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import static ua.com.foxminded.university.controllers.ControllersUtility.setPagesValueAndStatus;

@AllArgsConstructor
@Controller
@RequestMapping("/education/form")
public class FormsOfEducationController {

    private final FormOfEducationService formOfEducationService;
    private final GroupService groupService;

    @GetMapping()
    public String showAll(Model model, @RequestParam(value="page", required = false) String page){
        setPagesValueAndStatus(page, model);
        model.addAttribute("formsOfEducation", formOfEducationService.findAll(page));

        return "/formOfEducation/all";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") long id, Model model){

        model.addAttribute("formOfEducation", formOfEducationService.findById(id));
        model.addAttribute("formOfEducationGroups", groupService.findByFormOfEducation(id));
        return "/formOfEducation/show";
    }

    @GetMapping("/new")
    public String newCourse(Model model, @ModelAttribute("formOfEducation") FormOfEducationRequest formOfEducationRequest) {
        return "/formOfEducation/add";
    }

    @PostMapping()
    public String create(@ModelAttribute("formOfEducation") FormOfEducationRequest formOfEducationRequest) {
        formOfEducationService.create(formOfEducationRequest);
        return "redirect:/education/form";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        FormOfEducationResponse formOfEducationResponse = formOfEducationService.findById(id);
        FormOfEducationRequest formOfEducationRequest = new FormOfEducationRequest();
        formOfEducationRequest.setId(formOfEducationResponse.getId());
        formOfEducationRequest.setName(formOfEducationResponse.getName());
        List<GroupResponse> notFormOfEducationGroups = groupService.findAll();
        List<GroupResponse> formOfEducationGroups = groupService.findByFormOfEducation(id);
        notFormOfEducationGroups.removeAll(formOfEducationGroups);

        model.addAttribute("formOfEducationRequest", formOfEducationRequest);

        model.addAttribute("notFormOfEducationGroups", notFormOfEducationGroups);
        model.addAttribute("formOfEducationGroups", formOfEducationGroups);

        return "/formOfEducation/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("formOfEducationRequest") FormOfEducationRequest formOfEducationRequest) {
        formOfEducationService.edit(formOfEducationRequest);
        return "redirect:/education/form";
    }

    @PostMapping("/{id}/assign/group")
    public String addGroupFromFormOfEducation(Model model, @PathVariable("id") long formOfEducationId, @RequestParam long idNewGroup) {
        model.addAttribute("idNewGroup", idNewGroup);
        groupService.changeFormOfEducation(idNewGroup, formOfEducationId);
        return "redirect:/education/form/" + formOfEducationId + "/edit";
    }

    @PostMapping("/{id}/remove/group")
    public String removeGroupFromFormOfEducation(Model model, @PathVariable("id") long formOfEducationId, @RequestParam long idRemovingGroup) {
        model.addAttribute("idRemovingGroup", idRemovingGroup);
        groupService.removeFormOfEducationFromGroup(idRemovingGroup);
        return "redirect:/education/form/" + formOfEducationId + "/edit";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        formOfEducationService.deleteById(id);
        return "redirect:/education/form";
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
