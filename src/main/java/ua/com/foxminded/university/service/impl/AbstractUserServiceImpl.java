package ua.com.foxminded.university.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import ua.com.foxminded.university.dao.interfaces.UserDao;
import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.dto.UserResponse;
import ua.com.foxminded.university.entity.User;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.interfaces.UserService;
import ua.com.foxminded.university.service.validator.UserValidator;
import java.util.Optional;

public abstract class AbstractUserServiceImpl<REQUEST extends UserRequest, RESPONSE extends UserResponse>
        extends AbstractPageableCrudService implements UserService<REQUEST, RESPONSE> {

    protected final PasswordEncoder passwordEncoder;
    protected final UserDao userDao;
    protected final UserValidator userValidator;

    public AbstractUserServiceImpl(PasswordEncoder passwordEncoder, UserDao userDao, UserValidator userValidator) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.userValidator = userValidator;
    }

    @Override
    public boolean login(String email, String password){
        Optional<User> user = userDao.findByEmail(email);
        return user.map(User::getPassword)
                .filter(passwordFromDB -> passwordEncoder.matches(password, passwordFromDB))
                .isPresent();
    }

    @Override
    public RESPONSE register(REQUEST userDto) {
        userValidator.validate(userDto);
        if (userDao.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EntityAlreadyExistException("This email already registered");
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            return registerCertainUser(userDto);
        }
    }

    protected abstract RESPONSE registerCertainUser(REQUEST userDto);

}
