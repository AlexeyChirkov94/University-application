package ua.com.foxminded.university.controllers;

import lombok.experimental.UtilityClass;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ControllersUtility {

    public static void setPagesValueAndStatus(String page, Model model){

        Map<String, Integer> pages = new HashMap<>();

        int currentIntegerPage;
        try {
            currentIntegerPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            currentIntegerPage = 1;
        }

        pages.put("currentPage", currentIntegerPage);
        pages.put("nextPage", currentIntegerPage + 1);
        pages.put("previousPage", currentIntegerPage - 1);

        if(pages.get("previousPage") < 1 ){
            model.addAttribute("previousPageStatus", "page-item disabled");
        } else {
            model.addAttribute("previousPageStatus", "page-item");
        }

        model.addAttribute("currentPage", pages.get("currentPage"));
        model.addAttribute("nextPage", pages.get("nextPage"));
        model.addAttribute("previousPage", pages.get("previousPage"));
    }

}
