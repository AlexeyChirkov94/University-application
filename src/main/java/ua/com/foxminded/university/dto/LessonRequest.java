package ua.com.foxminded.university.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LessonRequest {

    private Long id;
    private CourseRequest courseRequest;
    private LocalDateTime timeOfStartLesson;
    private GroupRequest groupRequest;
    private ProfessorRequest teacher;
    private FormOfLessonRequest formOfLessonRequest;

}
