package ua.com.foxminded.university.dto;

import lombok.Data;

@Data
public class CourseResponse {

    private Long id;
    private String name;
    private DepartmentResponse departmentResponse;

}
