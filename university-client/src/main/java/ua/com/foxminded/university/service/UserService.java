package ua.com.foxminded.university.service;

import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.dto.UserResponse;
import java.util.List;
import java.util.Optional;

public interface UserService<REQUEST extends UserRequest, RESPONSE extends UserResponse> {

    RESPONSE register(REQUEST requestDto);

    RESPONSE findById(long id);

    List<RESPONSE> findAll(String page);

    List<RESPONSE> findAll();

    void edit(REQUEST requestDto);

    boolean deleteById(long id);

    Optional<RESPONSE> findByEmail(String email);

}
