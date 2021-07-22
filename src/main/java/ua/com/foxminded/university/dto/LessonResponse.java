package ua.com.foxminded.university.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LessonResponse {

    private Long id;
    private CourseResponse courseResponse;
    private LocalDateTime timeOfStartLesson;
    private GroupResponse groupResponse;
    private ProfessorResponse teacher;
    private FormOfLessonResponse formOfLessonResponse;

}
