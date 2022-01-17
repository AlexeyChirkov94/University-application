package ua.com.foxminded.university.dao;

import ua.com.foxminded.university.entity.User;

import java.util.List;

public interface UserDao<U extends User> extends CrudPageableDao<U>{

    List<U> findByEmail(String email);

    U save(U user);

    void addRoleToUser(long userId, long addingRoleId);

    void removeRoleFromUser(long userId, long removingRoleId);

}
