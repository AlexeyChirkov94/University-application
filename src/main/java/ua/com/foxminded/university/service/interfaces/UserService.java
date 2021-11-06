package ua.com.foxminded.university.service.interfaces;

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

    boolean login(String email, String password);

    Optional<RESPONSE> findByEmail(String email);

}
