package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public class FormOfEducation {

    Long id;
    String name;

}
