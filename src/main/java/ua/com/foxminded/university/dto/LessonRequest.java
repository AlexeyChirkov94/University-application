package ua.com.foxminded.university.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
public class LessonRequest {

    private Long id;
    private Long courseId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime timeOfStartLesson;

    private Long groupId;
    private Long teacherId;
    private Long formOfLessonId;

}
