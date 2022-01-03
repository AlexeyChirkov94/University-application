package ua.com.foxminded.university.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProfessorRequest extends UserRequest {

    private Long departmentId;
    private Integer scienceDegreeId;

}
