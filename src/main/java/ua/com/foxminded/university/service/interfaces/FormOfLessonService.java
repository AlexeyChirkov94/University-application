package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import java.util.List;
import java.util.Optional;

public interface FormOfLessonService {

    FormOfLessonResponse create(FormOfLessonRequest requestDto);

    Optional<FormOfLessonResponse> findById(long id);

    List<FormOfLessonResponse> findAll(String page);

    List<FormOfLessonResponse> findAll();

    void edit(FormOfLessonRequest requestDto);

    boolean deleteById(long id);

}
