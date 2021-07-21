package ua.com.foxminded.university.service.interfaces;

import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.dto.UserResponse;
import java.util.Optional;

public interface UserService<REQUEST extends UserRequest, RESPONSE extends UserResponse>
        extends CrudService<REQUEST, RESPONSE> {

    RESPONSE register(REQUEST requestDto);

    boolean login(String email, String password);

    Optional<RESPONSE> findByEmail(String email);

}
