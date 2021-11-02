package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest requestDto);

    Optional<DepartmentResponse> findById(long id);

    List<DepartmentResponse> findAll(String page);

    List<DepartmentResponse> findAll();

    void edit(DepartmentRequest requestDto);

    boolean deleteById(long id);

}
