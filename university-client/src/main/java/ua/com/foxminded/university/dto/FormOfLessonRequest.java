package ua.com.foxminded.university.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class FormOfLessonRequest {

    private Long id;

    @Size(min = 2, max = 50)
    private String name;

    @Range(min = 15, max = 180)
    @NotNull
    private Integer duration;

}
