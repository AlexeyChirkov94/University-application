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

    private static final String ATTRIBUTE_NAME_OF_CURRENT_PAGE = "currentPage";
    private static final String ATTRIBUTE_NAME_OF_NEXT_PAGE = "nextPage";
    private static final String ATTRIBUTE_NAME_OF_PREVIOUS_PAGE = "previousPage";

    public static void setPagesValueAndStatus(String page, Model model){

        Map<String, Integer> pages = new HashMap<>();

        int currentIntegerPage;
        try {
            currentIntegerPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            currentIntegerPage = 1;
        }

        pages.put(ATTRIBUTE_NAME_OF_CURRENT_PAGE, currentIntegerPage);
        pages.put(ATTRIBUTE_NAME_OF_NEXT_PAGE, currentIntegerPage + 1);
        pages.put(ATTRIBUTE_NAME_OF_PREVIOUS_PAGE, currentIntegerPage - 1);

        if(pages.get(ATTRIBUTE_NAME_OF_PREVIOUS_PAGE) < 1 ){
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

        model.addAttribute(ATTRIBUTE_NAME_OF_CURRENT_PAGE, pages.get(ATTRIBUTE_NAME_OF_CURRENT_PAGE));
        model.addAttribute(ATTRIBUTE_NAME_OF_NEXT_PAGE, pages.get(ATTRIBUTE_NAME_OF_NEXT_PAGE));
        model.addAttribute(ATTRIBUTE_NAME_OF_PREVIOUS_PAGE, pages.get(ATTRIBUTE_NAME_OF_PREVIOUS_PAGE));
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
            return "";
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
