package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public class Group {

    Long id;
    String name;
    Department department;
    FormOfEducation formOfEducation;

}
