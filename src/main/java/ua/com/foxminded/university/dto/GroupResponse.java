package ua.com.foxminded.university.dto;

import lombok.Data;

@Data
public class GroupResponse {

    private Long id;
    private String name;
    private DepartmentResponse departmentResponse;
    private FormOfEducationResponse formOfEducationResponse;

}
