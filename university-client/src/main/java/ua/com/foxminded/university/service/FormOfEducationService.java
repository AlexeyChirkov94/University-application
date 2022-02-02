package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import java.util.List;

public interface FormOfEducationService {

    FormOfEducationResponse create(FormOfEducationRequest requestDto);

    FormOfEducationResponse findById(long id);

    List<FormOfEducationResponse> findAll(String page);

    List<FormOfEducationResponse> findAll();

    void edit(FormOfEducationRequest requestDto);

    void deleteById(long id);

}
