package ua.com.foxminded.university.dao.interfaces;

import ua.com.foxminded.university.entity.User;
import java.util.Optional;

public interface UserDao<U extends User> extends CrudPageableDao<U>{

    Optional<U> findByEmail(String email);

    U save(U user);

}
