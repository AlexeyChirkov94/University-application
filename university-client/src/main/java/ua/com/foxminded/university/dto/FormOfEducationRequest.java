package ua.com.foxminded.university.dto;

import lombok.Data;
import javax.validation.constraints.Size;

@Data
public class FormOfEducationRequest {

    private Long id;

    @Size(min = 2, max = 50)
    private String name;

}
