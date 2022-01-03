package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(setterPrefix = "with")
public class FormOfLesson {

    Long id;
    String name;
    Integer duration;

}
