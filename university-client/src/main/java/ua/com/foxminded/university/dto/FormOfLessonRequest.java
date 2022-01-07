package ua.com.foxminded.university.dto;

import lombok.Data;

@Data
public class FormOfLessonRequest {

    private Long id;
    private String name;
    private Integer duration;

}
