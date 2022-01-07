package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.DepartmentRequest;
import ua.com.foxminded.university.dto.DepartmentResponse;
import java.util.List;

public interface DepartmentService {

    DepartmentResponse create(DepartmentRequest requestDto);

    DepartmentResponse findById(long id);

    List<DepartmentResponse> findAll(String page);

    List<DepartmentResponse> findAll();

    void edit(DepartmentRequest requestDto);

    boolean deleteById(long id);

}
