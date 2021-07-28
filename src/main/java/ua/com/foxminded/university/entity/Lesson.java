package ua.com.foxminded.university.entity;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;

@Value
@Builder(setterPrefix = "with")
public class Lesson {

    Long id;
    Course course;
    LocalDateTime timeOfStartLesson;
    Group group;
    Professor teacher;
    FormOfLesson formOfLesson;

}
