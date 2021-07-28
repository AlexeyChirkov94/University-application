package ua.com.foxminded.university.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
public class ProfessorRequest extends UserRequest {

    private DepartmentRequest departmentRequest;
    private ScienceDegreeRequest scienceDegreeRequest;
    private List<CourseRequest> coursesRequest;

}
