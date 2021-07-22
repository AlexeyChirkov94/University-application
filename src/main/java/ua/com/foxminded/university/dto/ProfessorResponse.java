package ua.com.foxminded.university.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class ProfessorResponse extends UserResponse {

    private DepartmentResponse departmentResponse;
    private ScienceDegreeResponse scienceDegreeResponse;
    private List<CourseResponse> coursesResponse;

}
