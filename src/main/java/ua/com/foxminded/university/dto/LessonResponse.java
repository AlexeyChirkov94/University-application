package ua.com.foxminded.university.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
public class LessonResponse {

    private Long id;
    private CourseResponse courseResponse;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime timeOfStartLesson;

    private GroupResponse groupResponse;
    private ProfessorResponse teacher;
    private FormOfLessonResponse formOfLessonResponse;

}
