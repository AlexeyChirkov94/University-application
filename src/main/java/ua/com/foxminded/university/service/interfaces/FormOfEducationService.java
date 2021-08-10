package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.FormOfEducationRequest;
import ua.com.foxminded.university.dto.FormOfEducationResponse;
import java.util.List;
import java.util.Optional;

public interface FormOfEducationService {

    FormOfEducationResponse register(FormOfEducationRequest requestDto);

    Optional<FormOfEducationResponse> findById(long id);

    List<FormOfEducationResponse> findAll(String page);

    List<FormOfEducationResponse> findAll();

    void edit(FormOfEducationRequest requestDto);

    boolean deleteById(long id);

}
