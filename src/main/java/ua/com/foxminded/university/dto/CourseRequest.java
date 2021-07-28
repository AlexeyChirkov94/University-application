package ua.com.foxminded.university.dto;

import lombok.Data;

@Data
public class CourseRequest {

    private Long id;
    private String name;
    private DepartmentRequest departmentRequest;

}
