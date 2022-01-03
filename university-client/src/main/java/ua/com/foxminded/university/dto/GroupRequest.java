package ua.com.foxminded.university.dto;

import lombok.Data;

@Data
public class GroupRequest {

    private Long id;
    private String name;
    private Long departmentId;
    private Long formOfEducationId;

}
