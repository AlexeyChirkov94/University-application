package ua.com.foxminded.university.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ua.com.foxminded.university.dao.RoleDao;
import ua.com.foxminded.university.dao.UserDao;
import ua.com.foxminded.university.dto.UserRequest;
import ua.com.foxminded.university.dto.UserResponse;
import ua.com.foxminded.university.service.exception.EntityAlreadyExistException;
import ua.com.foxminded.university.service.exception.EntityDontExistException;
import ua.com.foxminded.university.service.UserService;
import ua.com.foxminded.university.service.validator.UserValidator;

@AllArgsConstructor
@Transactional(transactionManager = "hibernateTransactionManager")
public abstract class AbstractUserServiceImpl<REQUEST extends UserRequest, RESPONSE extends UserResponse>
        extends AbstractPageableCrudService implements UserService<REQUEST, RESPONSE> {

    protected final PasswordEncoder passwordEncoder;
    protected final UserDao userDao;
    protected final RoleDao roleDao;
    protected final UserValidator userValidator;

    @Override
    public RESPONSE register(REQUEST userDto) {
        userValidator.validate(userDto);
        if (!userDao.findByEmail(userDto.getEmail()).isEmpty()) {
            throw new EntityAlreadyExistException("This email already registered");
        } else {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            return registerCertainUser(userDto);
        }
    }

    public void addRoleToUser (long userId, long addingRoleId) {
        checkThatUserExist(userId);
        checkThatRoleExist(addingRoleId);
        userDao.addRoleToUser(userId, addingRoleId);
    }

    public void removeRoleFromUser(long userId, long removingRoleId) {
        checkThatUserExist(userId);
        checkThatRoleExist(removingRoleId);
        userDao.removeRoleFromUser(userId, removingRoleId);
    }

    protected void removeAllRolesFromUser(long userId){
        checkThatUserExist(userId);
        roleDao.findByUserId(userId).forEach(role -> userDao.removeRoleFromUser(userId, role.getId()));
    }

    protected void checkThatUserExist(long userId){
        if (!userDao.findById(userId).isPresent()) {
            throw new EntityDontExistException("There no user with id: " + userId);
        }
    }

    protected void checkThatRoleExist(long roleId){
        if (!roleDao.findById(roleId).isPresent()) {
            throw new EntityDontExistException("There no role with id: " + roleId);
        }
    }

    protected abstract RESPONSE registerCertainUser(REQUEST userDto);

}
