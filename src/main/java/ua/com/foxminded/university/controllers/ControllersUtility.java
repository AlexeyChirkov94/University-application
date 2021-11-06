package ua.com.foxminded.university.controllers;

import lombok.experimental.UtilityClass;
import org.springframework.ui.Model;
import ua.com.foxminded.university.dto.LessonResponse;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
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
            model.addAttribute("liPreviousPageLabelStatus", "page-item disabled");
            model.addAttribute("aPreviousPageLabelStatus", "page-link");
            model.addAttribute("liPreviousPageNumberStatus", "invisible");
            model.addAttribute("aPreviousPageNumberStatus", "invisible");
        } else {
            model.addAttribute("liPreviousPageLabelStatus", "page-item");
            model.addAttribute("aPreviousPageLabelStatus", "page-link");
            model.addAttribute("liPreviousPageNumberStatus", "page-item");
            model.addAttribute("aPreviousPageNumberStatus", "page-link");
        }

        model.addAttribute("currentPage", pages.get("currentPage"));
        model.addAttribute("nextPage", pages.get("nextPage"));
        model.addAttribute("previousPage", pages.get("previousPage"));
    }


    public static Map<Long, String> getStringDateTimes(List<LessonResponse> lessons){

        Map<Long, String> times = new HashMap<>();

        for(LessonResponse lesson : lessons) {
            times.put(lesson.getId(), getStringDateTime(lesson));
        }

        return times;
    }

    public static String getStringDateTime(LessonResponse lesson){

        StringBuilder stringBuilder = new StringBuilder();

        if (lesson.getTimeOfStartLesson() == null) {
            return "not appointed";
        } else {
            int intYear = lesson.getTimeOfStartLesson().getYear();
            Month month = lesson.getTimeOfStartLesson().getMonth();
            int intDay = lesson.getTimeOfStartLesson().getDayOfMonth();
            int intHour = lesson.getTimeOfStartLesson().getHour();
            int intMinutes = lesson.getTimeOfStartLesson().getMinute();

            String stringYear = String.valueOf(intYear);
            String stringMonth = String.valueOf(month);
            String stringDay = String.valueOf(intDay);
            String stringHour;
            String stringMinutes;

            if (intHour < 10) {
                stringHour = "0" + intHour;
            } else {
                stringHour = String.valueOf(intHour);
            }

            if (intMinutes < 10) {
                stringMinutes = "0" + intMinutes;
            } else {
                stringMinutes = String.valueOf(intMinutes);
            }

            stringBuilder.append(stringYear)
                    .append(" ")
                    .append(stringMonth)
                    .append(" ")
                    .append(stringDay)
                    .append(" ")
                    .append(stringHour)
                    .append(":")
                    .append(stringMinutes);

            return stringBuilder.toString();
        }
    }

}
