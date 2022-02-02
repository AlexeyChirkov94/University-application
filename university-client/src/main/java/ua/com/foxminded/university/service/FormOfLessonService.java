package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.FormOfLessonRequest;
import ua.com.foxminded.university.dto.FormOfLessonResponse;
import java.util.List;

public interface FormOfLessonService {

    FormOfLessonResponse create(FormOfLessonRequest requestDto);

    FormOfLessonResponse findById(long id);

    List<FormOfLessonResponse> findAll(String page);

    List<FormOfLessonResponse> findAll();

    void edit(FormOfLessonRequest requestDto);

    void deleteById(long id);

}
