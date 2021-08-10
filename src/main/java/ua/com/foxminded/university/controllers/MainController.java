package ua.com.foxminded.university.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/features")
    public String features(){
        return "featuresView";
    }

    @GetMapping("/about")
    public String about(){
        return "aboutView";
    }

    @GetMapping("/registration")
    public String registration(){
        return "registrationView";
    }

    @GetMapping("/login")
    public String login(){
        return "loginView";
    }

}
