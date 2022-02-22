package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.repository.RoleRepository;
import ua.com.foxminded.university.repository.UserRepository;
import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.dto.UserResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.UserService;
import ua.com.foxminded.university.service.validator.UserValidator;

@AllArgsConstructor
@Transactional
public abstract class AbstractUserServiceImpl<REQUEST extends UserRequest, RESPONSE extends UserResponse>
        extends AbstractPageableCrudService implements UserService<REQUEST, RESPONSE> {

    protected final PasswordEncoder passwordEncoder;
    protected final UserRepository userRepository;
    protected final RoleRepository roleRepository;
    protected final UserValidator userValidator;

    @Override
    public RESPONSE register(REQUEST userDto) {
        userValidator.validate(userDto);
        if (!userRepository.findAllByEmail(userDto.getEmail()).isEmpty()) {
            throw new EntityAlreadyExistException("This email already registered");
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            return registerCertainUser(userDto);
        }
    }

    public void addRoleToUser (long userId, long addingRoleId) {
        checkThatUserExist(userId);
        checkThatRoleExist(addingRoleId);
        userRepository.addRoleToUser(userId, addingRoleId);
    }

    public void removeRoleFromUser(long userId, long removingRoleId) {
        checkThatUserExist(userId);
        checkThatRoleExist(removingRoleId);
        userRepository.removeRoleFromUser(userId, removingRoleId);
    }

    protected void removeAllRolesFromUser(long userId){
        checkThatUserExist(userId);
        roleRepository.findAllByUserId(userId).forEach(role -> userRepository.removeRoleFromUser(userId, role.getId()));
    }

    protected void checkThatUserExist(long userId){
        if (!userRepository.findById(userId).isPresent()) {
            throw new EntityDontExistException("There no user with id: " + userId);
        }
    }

    protected void checkThatRoleExist(long roleId){
        if (!roleRepository.findById(roleId).isPresent()) {
            throw new EntityDontExistException("There no role with id: " + roleId);
        }
    }

    protected abstract RESPONSE registerCertainUser(REQUEST userDto);

}
